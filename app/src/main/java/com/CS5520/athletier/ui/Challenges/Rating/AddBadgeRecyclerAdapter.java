package com.CS5520.athletier.ui.Challenges.Rating;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsBadge;
import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.List;

public class AddBadgeRecyclerAdapter extends
        RecyclerView.Adapter<AddBadgeRecyclerAdapter.BadgeDetailsViewHolder> {

    // The Sport the User is rating in
    private Sport sport;
    // The SportsBadges of the selected Sport
    private List<SportsBadge> badges;
    // The names of the SportsBadges the User is awarding to the selected Opponent
    private List<String> awardedBadges;

    public AddBadgeRecyclerAdapter(Sport sport) {
        this.sport = sport;
        this.badges = sport.getBadgeOptions();
        this.awardedBadges = new ArrayList<>();
    }

    List<String> getAwardedBadges() {
        return this.awardedBadges;
    }

    @NonNull
    @Override
    public BadgeDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_badge_cell, parent, false);
        return new BadgeDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeDetailsViewHolder holder, int position) {
        SportsBadge badge = badges.get(position);
        holder.badgeIcon.setImageResource(badge.getResId());
        holder.badgeTitleText.setText(badge.getName());
        holder.badgeDetailsText.setText(badge.getDescription());
        holder.addBadgeButton.setChecked(false);
        // Tag each button with the name of the Badge it's holder is displaying info for
        holder.addBadgeButton.setTag(badge.getName());
        setupButtonListener(holder);
    }

    private void setupButtonListener(BadgeDetailsViewHolder holder) {
        holder.addBadgeButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                String badgeName = button.getTag().toString();
                if (isChecked) {
                    awardedBadges.add(badgeName);
                } else {
                    awardedBadges.remove(badgeName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return badges != null ? badges.size() : 0;
    }

    final class BadgeDetailsViewHolder extends RecyclerView.ViewHolder {
        private ImageView badgeIcon;
        private TextView badgeTitleText;
        private TextView badgeDetailsText;
        private CheckBox addBadgeButton;

        BadgeDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.badgeIcon = itemView.findViewById(R.id.addBadgeIcon);
            this.badgeTitleText = itemView.findViewById(R.id.addBadgeTitleText);
            this.badgeDetailsText = itemView.findViewById(R.id.addBadgeDescriptionText);
            this.addBadgeButton = itemView.findViewById(R.id.addBadgeCheckBox);
        }
    }
}
