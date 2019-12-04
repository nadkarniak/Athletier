package com.CS5520.athletier.ui.Leaderboards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.SportsBadge;
import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BadgeRecyclerAdapter extends RecyclerView.Adapter<BadgeRecyclerAdapter.BadgeViewHolder> {

    private SportsAchievementSummary achievement;
    private List<SportsBadge> badges;

    public BadgeRecyclerAdapter(SportsAchievementSummary achievement) {
        this.achievement = achievement;
        this.badges = achievement.getSport() != null ? achievement.getSport().getBadgeOptions()
                : new ArrayList<SportsBadge>();

    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View badgeCell = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_cell,
                parent, false);
        return new BadgeViewHolder(badgeCell);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        if (position < badges.size()) {
            SportsBadge badge = badges.get(position);
            holder.badgeImageView.setImageResource(badge.getResId());
            holder.badgeTitleText.setText(badge.getName());

            // TODO: Display badge count...
        }
    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    final class BadgeViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView badgeImageView;
        private TextView badgeTitleText;


        BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.badgeImageView = itemView.findViewById(R.id.badgeCellIcon);
            this.badgeTitleText = itemView.findViewById(R.id.badgeNameTextView);
        }
    }

}
