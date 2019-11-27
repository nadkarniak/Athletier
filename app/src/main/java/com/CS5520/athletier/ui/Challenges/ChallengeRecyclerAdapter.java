package com.CS5520.athletier.ui.Challenges;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.R;
import com.google.android.material.chip.Chip;

import java.lang.ref.WeakReference;
import java.util.List;

public class ChallengeRecyclerAdapter extends RecyclerView.Adapter<ChallengeRecyclerAdapter.ChallengeViewHolder> {
    private List<Challenge> challenges;
    private boolean asHost;
    private WeakReference<Context> contextRef;

    ChallengeRecyclerAdapter(List<Challenge> challenges, boolean asHost, Context context) {
        this.challenges = challenges;
        this.asHost = asHost;
        this.contextRef = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View challengeCell = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.challenge_cell, parent, false);
        return new ChallengeViewHolder(challengeCell);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Context context = contextRef.get();
        Challenge challenge = challenges.get(position);
        holder.setUsernameText(asHost ? challenge.getOpponentName(): challenge.getHostName());
        if (context != null) {
            Sport sport = Sport.fromString(challenge.getSport());
            if (sport != null) {
                holder.setSportChipText(sport, context);
            }
        }
        holder.setDateText(challenge.getFormattedDate());
        holder.setAddressText(challenge.getFormattedAddress());
        holder.setImageView("abc");
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


    final class ChallengeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView usernameText;
        private Chip sportChip;
        private TextView dateText;
        private TextView addressText;


        ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.userImageView);
            this.usernameText = itemView.findViewById(R.id.usernameText);
            this.sportChip = itemView.findViewById(R.id.sportChip);
            this.dateText = itemView.findViewById(R.id.dateText);
            this.addressText = itemView.findViewById(R.id.addressText);
        }

        void setImageView(String userPhotoUrl) {
            imageView.setImageResource(R.drawable.ic_person_black_24dp);
        }

        void setUsernameText(String username) {
            this.usernameText.setText(username != null ? username : "Awaiting Opponent");
        }

        void setSportChipText(Sport sport, Context context) {
            sportChip.setText(sport.toString());
            sportChip.setChipIcon(ContextCompat.getDrawable(context, sport.getIconId()));
        }

        void setDateText(String dateString) {
            dateText.setText(dateString);
        }

        void setAddressText(String address) {
            addressText.setText(address);
        }

    }

}
