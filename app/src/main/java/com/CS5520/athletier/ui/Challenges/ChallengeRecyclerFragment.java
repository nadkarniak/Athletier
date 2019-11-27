package com.CS5520.athletier.ui.Challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Map.SpinnerInputFragment;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRecyclerFragment extends Fragment {

    private SpinnerInputFragment spinner;
    private ChallengeRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_recycler, container, false);
        recyclerView = view.findViewById(R.id.challengeRecycler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();
    }

    void updateAsHost(boolean asHost) {
        adapter.updateAsHost(asHost);
    }

    void updateChallenges(List<Challenge> challenges) {
        adapter.updateChallenges(challenges);
    }

    private void setupRecyclerView() {
        adapter = new ChallengeRecyclerAdapter(
                new ArrayList<Challenge>(),
                true,
                getContext()
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
