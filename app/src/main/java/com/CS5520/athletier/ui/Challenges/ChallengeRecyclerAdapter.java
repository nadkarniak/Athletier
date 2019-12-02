package com.CS5520.athletier.ui.Challenges;

import android.app.Activity;
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

        // Configure buttons based on acceptance status of the challenge
        switch (AcceptanceStatus.valueOf(challenge.getAcceptanceStatus())) {
            case ACCEPTED:
                holder.leftButton.setVisibility(View.VISIBLE);
                holder.leftButton.setText(R.string.finish);
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
                hideStatusText(holder);
                break;
            case PENDING:
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
                hideStatusText(holder);
                break;
            case COMPLETE:
                holder.rightButton.setText(asHost ? R.string.report_winner : R.string.confirm_winner);
                holder.leftButton.setVisibility(View.GONE);
                holder.leftButton.setEnabled(false);
                holder.statusTitleText.setVisibility(View.VISIBLE);
                holder.statusText.setVisibility(View.VISIBLE);
                holder.statusTitleText.setText(R.string.winner);
        }
    }

    private void hideStatusText(ChallengeViewHolder holder) {
        holder.statusText.setVisibility(View.GONE);
        holder.statusTitleText.setVisibility(View.GONE);
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
        private TextView statusText;
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
            this.statusText = itemView.findViewById(R.id.statusText);
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
                    .child("users")
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
    }

}
