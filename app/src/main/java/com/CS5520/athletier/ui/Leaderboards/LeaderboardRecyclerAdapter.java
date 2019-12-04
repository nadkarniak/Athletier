package com.CS5520.athletier.ui.Leaderboards;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.DataSnapShotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerAdapter.LeaderboardViewHolder> {

    private Sport selectedSport;
    private List<SportsAchievementSummary> achievements;
    private DatabaseReference databaseReference;
    private RecyclerView.RecycledViewPool viewPool;


    LeaderboardRecyclerAdapter(Sport sport) {
        this.selectedSport = sport;
        this.achievements = new ArrayList<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.viewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View leaderboardCell = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_cell, parent, false);
        return new LeaderboardViewHolder(leaderboardCell);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        SportsAchievementSummary achievement = achievements.get(position);
        holder.setUsernameAndImage(achievement.getOwnerId());

        Resources resources = holder.itemView.getContext().getResources();

        String rankTitle = resources.getString(R.string.rank)+ " " + (position + 1);
        String expTitle = "" + achievement.getExp() + " " + resources.getString(R.string.pts);
        String tierTitle = resources.getString(R.string.tier) + " " + achievement.getTier();

        holder.rankText.setText(rankTitle);
        holder.expText.setText(expTitle);
        holder.tierText.setText(tierTitle);

        holder.badgeRecyclerView.setRecycledViewPool(viewPool);
        holder.badgeRecyclerView.setAdapter(new BadgeRecyclerAdapter(achievement));
        holder.badgeRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        holder.itemView.getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );
    }

    @Override
    public int getItemCount() {
        return achievements != null ? achievements.size() : 0;
    }

    void setSelectedSport(Sport sport) {
        this.selectedSport = sport;
        querySportsAchievements();
    }

    private void querySportsAchievements() {
        databaseReference
                .child(SportsAchievementSummary.sportsAchievementKey)
                .child(selectedSport.name())
                // Sorts achievements in ascending order by exp
                .orderByChild(SportsAchievementSummary.expKey)
                .limitToFirst(50)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        achievements = DataSnapShotParser.parseToSummaryList(dataSnapshot);
                        // Need to reverse list so achievements are in descending order
                        // (most exp first)
                        Collections.reverse(achievements);
                        notifyDataSetChanged();
                        // TODO: Update rank in each achievement
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    final class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        private TextView rankText;
        private TextView expText;
        private ImageView imageView;
        private TextView usernameText;
        private TextView tierText;
        private RecyclerView badgeRecyclerView;
        private BadgeRecyclerAdapter badgeAdapter;

        LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rankText = itemView.findViewById(R.id.leaderboardRankText);
            this.expText = itemView.findViewById(R.id.leaderboardPointsText);
            this.imageView = itemView.findViewById(R.id.leaderboardImageView);
            this.usernameText = itemView.findViewById(R.id.leaderboardUsernameText);
            this.tierText = itemView.findViewById(R.id.leaderboardTierText);
            this.badgeRecyclerView = itemView.findViewById(R.id.leaderboardBadgeRecycler);
        }

        private void setUsernameAndImage(String userId) {
            databaseReference
                    .child(User.userKey)
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                usernameText.setText(user.getUsername());
                                Picasso.get().load(user.getPhotoUrl()).into(imageView);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }
    }

}
