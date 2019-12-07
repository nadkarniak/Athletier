package com.CS5520.athletier.ui.Challenges;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsBadge;
import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.List;

public class AddBadgeRecyclerAdapter extends
        RecyclerView.Adapter<AddBadgeRecyclerAdapter.BadgeDetailsViewHolder> {

    private Sport sport;
    private List<SportsBadge> badges;
    private List<String> awardedBadges;

    public AddBadgeRecyclerAdapter(Sport sport) {
        this.sport = sport;
        this.badges = sport.getBadgeOptions();
        this.awardedBadges = new ArrayList<>();
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
