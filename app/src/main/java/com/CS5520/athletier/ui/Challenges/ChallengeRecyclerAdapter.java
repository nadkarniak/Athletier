package com.CS5520.athletier.ui.Challenges;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.ResultStatus;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.ChallengeButtonAction;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

public class ChallengeRecyclerAdapter extends
        RecyclerView.Adapter<ChallengeRecyclerAdapter.ChallengeViewHolder> {

    private List<Challenge> challenges;
    private boolean asHost;
    private WeakReference<Context> contextRef;
    private DatabaseReference databaseReference;
    private MutableLiveData<Pair<Challenge, ChallengeButtonAction>> challengeAndAction;
    private MutableLiveData<String> userImageClickEvent;

    ChallengeRecyclerAdapter(List<Challenge> challenges,
                             boolean asHost,
                             Context context) {
        this.challenges = challenges;
        this.asHost = asHost;
        this.contextRef = new WeakReference<>(context);
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.challengeAndAction = new MutableLiveData<>();
        this.userImageClickEvent = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View challengeCell = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.challenge_cell, parent, false);
        return new ChallengeViewHolder(challengeCell);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChallengeViewHolder holder, int position) {
        Context context = contextRef.get();
        final Challenge challenge = challenges.get(position);
        holder.displayUserInfo(asHost ? challenge.getOpponentId() : challenge.getHostId());
        if (context != null) {
            Sport sport = Sport.fromString(challenge.getSport());
            if (sport != null) {
                holder.setSportChipText(sport, context);
            }
        }
        holder.dateText.setText(challenge.getFormattedDate());
        holder.addressText.setText(challenge.getFormattedAddress());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = view.getTag();
                if (tag != null) {
                    // Pass the tag of the imageView to the userImageClickEvent stream
                    userImageClickEvent.postValue(tag.toString());
                }
            }
        });

        // Remove any previously existing on click listeners
        holder.leftButton.setOnClickListener(null);
        holder.rightButton.setOnClickListener(null);
        holder.leftButton.setEnabled(true);
        holder.rightButton.setEnabled(true);

        // Display or hide winner depending on resultStatus of challenge
        setResultDisplay(holder, challenge);

        // Configure buttons based on acceptance status of the challenge
        switch (AcceptanceStatus.valueOf(challenge.getAcceptanceStatus())) {
            case ACCEPTED:
                configureAcceptedChallengeHolder(holder, challenge);
                break;
            case PENDING:
                configurePendingChallengeHolder(holder, challenge);
                break;
            case COMPLETE:
               configureCompletedChallengeHolder(holder, challenge);
        }
    }

    private void configureAcceptedChallengeHolder(ChallengeViewHolder holder, Challenge challenge) {
        holder.leftButton.setVisibility(View.VISIBLE);
        holder.leftButton.setText(R.string.report_winner);
        holder.rightButton.setText(R.string.cancel);
        setHolderButtonListener(
                holder.leftButton,
                challenge,
                asHost ? ChallengeButtonAction.HOST_REPORT
                        : ChallengeButtonAction.OPPONENT_REPORT
        );
        setHolderButtonListener(
                holder.rightButton,
                challenge,
                ChallengeButtonAction.CANCEL
        );
    }

    private void configurePendingChallengeHolder(ChallengeViewHolder holder, Challenge challenge) {
        if (asHost && challenge.getOpponentId() != null) {
            holder.leftButton.setText(R.string.accept);
            holder.rightButton.setText(R.string.reject);
            setHolderButtonListener(
                    holder.leftButton,
                    challenge,
                    ChallengeButtonAction.ACCEPT
            );
        } else {
            holder.leftButton.setVisibility(View.INVISIBLE);
            holder.leftButton.setEnabled(false);
            holder.rightButton.setText(R.string.cancel);
        }
        setHolderButtonListener(
                holder.rightButton,
                challenge,
                ChallengeButtonAction.CANCEL
        );
    }

    private void configureCompletedChallengeHolder(ChallengeViewHolder holder, Challenge challenge) {
        // If the result is confirmed, hide and disable left button
        if (challenge.getResultStatus().equals(ResultStatus.CONFIRMED.name())) {
            holder.leftButton.setVisibility(View.INVISIBLE);
            holder.leftButton.setEnabled(false);
        // Otherwise, the result is disputed and the left button should be enabled to allow User's
        // to revise the winner they reported
        } else {
            holder.leftButton.setVisibility(View.VISIBLE);
            holder.leftButton.setEnabled(true);
            holder.leftButton.setText(R.string.change_winner);
            setHolderButtonListener(holder.leftButton,
                    challenge,
                    asHost ? ChallengeButtonAction.HOST_REPORT
                            : ChallengeButtonAction.OPPONENT_REPORT
            );
        }

        // Show right button for rating the other User
        holder.rightButton.setText(R.string.rate);

        // Only enable right button if the User has not rated previously
        holder.rightButton.setEnabled(
                (asHost && !challenge.getHostDidRate()) ||
                        (!asHost && !challenge.getOpponentDidRate())
        );
        holder.rightButton.getBackground().setAlpha(holder.rightButton.isEnabled() ? 255 : 128);
        setHolderButtonListener(holder.rightButton,
                challenge,
                ChallengeButtonAction.RATE
        );
    }

    private void setResultDisplay(ChallengeViewHolder holder, Challenge challenge) {
        ResultStatus resultStatus = ResultStatus.valueOf(challenge.getResultStatus());
        switch (resultStatus) {
            case NONE:
                toggleResultStatusVisibility(holder, true);
                break;
            case AWAITING_CONFIRMATION:
                toggleResultStatusVisibility(holder, false);
                // Set result status message such that it indicates if the challenge is waiting for
                // the opponent to report a winner or the current user to report a winner
                int statusMsgId;
                if (asHost) {
                    statusMsgId = challenge.getHostReportedWinner() != null ?
                            R.string.waiting_for_confirmation :
                            R.string.waiting_on_user;
                } else {
                    statusMsgId = challenge.getOpponentReportedWinner() != null ?
                            R.string.waiting_for_confirmation :
                            R.string.waiting_on_user;
                }
                holder.resultStatusText.setText(statusMsgId);
                break;
            case DISPUTED:
                toggleResultStatusVisibility(holder, false);
                holder.resultStatusText.setText(R.string.result_disputed);
                break;
            case CONFIRMED:
                toggleResultStatusVisibility(holder, false);
                holder.displayWinnerName(challenge.getHostIsWinner() ? challenge.getHostId()
                        : challenge.getOpponentId());

        }
        if (holder.statusTitleText.getVisibility() == View.VISIBLE) {
            holder.statusTitleText.setText(R.string.winner);
        }
    }


    private void toggleResultStatusVisibility(ChallengeViewHolder holder, boolean shouldHide) {
        holder.resultStatusText.setVisibility(shouldHide ? View.GONE : View.VISIBLE);
        holder.statusTitleText.setVisibility(shouldHide ? View.GONE : View.VISIBLE);
    }

    private void setHolderButtonListener(Button button,
                                         final Challenge challenge,
                                         final ChallengeButtonAction action) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengeAndAction.setValue(Pair.create(challenge, action));
            }
        });
    }


    @Override
    public int getItemCount() {
        return challenges == null ? 0 : challenges.size();
    }

    // Update the asHost property, which dictates whether the recycler view shows challenges where
    // the current user is the host (true) or challenges where the current user is the opponent
    // (false)
    void updateAsHost(boolean newAsHost) {
        this.asHost = newAsHost;
        notifyDataSetChanged();
    }

    void updateChallenges(List<Challenge> newChallenges) {
        this.challenges = newChallenges;
        notifyDataSetChanged();
    }

    LiveData<Pair<Challenge, ChallengeButtonAction>> getChallengeAndAction() {
        return challengeAndAction;
    }

    LiveData<String> getUserImageClickEventStream() {
        return userImageClickEvent;
    }

    final class ChallengeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView usernameText;
        private Chip sportChip;
        private TextView dateText;
        private TextView addressText;
        private TextView resultStatusText;
        private TextView statusTitleText;
        private Button leftButton;
        private Button rightButton;


        ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.userImageView);
            this.usernameText = itemView.findViewById(R.id.usernameText);
            this.sportChip = itemView.findViewById(R.id.sportChip);
            this.dateText = itemView.findViewById(R.id.dateText);
            this.addressText = itemView.findViewById(R.id.addressText);
            this.resultStatusText = itemView.findViewById(R.id.statusText);
            this.statusTitleText = itemView.findViewById(R.id.statusTitleText);
            this.leftButton = itemView.findViewById(R.id.cellLeftButton);
            this.rightButton = itemView.findViewById(R.id.cellRightButton);
        }

        void displayUserInfo(String userId) {
            if (userId == null) {
                usernameText.setText(R.string.awaiting_challenger);
                imageView.setImageResource(R.drawable.ic_person_black_24dp);
                return;
            }

            databaseReference
                    .child(User.userKey)
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String photoUrl = user.getPhotoUrl();
                        if (photoUrl != null) {
                            Picasso.get().load(photoUrl).into(imageView);
                        } else {
                            imageView.setImageResource(R.drawable.ic_person_black_24dp);
                        }
                        usernameText.setText(user.getUsername());

                        // Associate user's email address with imageView
                        imageView.setTag(user.getEmailAddress());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    imageView.setImageResource(R.drawable.ic_person_black_24dp);
                }
            });
        }

        void setSportChipText(Sport sport, Context context) {
            sportChip.setText(sport.toString());
            sportChip.setChipIcon(ContextCompat.getDrawable(context, sport.getIconId()));
        }

        void displayWinnerName(String winnerId) {
            databaseReference
                    .child(User.userKey)
                    .child(winnerId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User winner = dataSnapshot.getValue(User.class);
                            if (winner != null) {
                                resultStatusText.setText(winner.getUsername());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

}
