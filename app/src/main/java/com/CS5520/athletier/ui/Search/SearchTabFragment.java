package com.CS5520.athletier.ui.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Search.FindUser.FindUserActivity;

public class SearchTabFragment extends Fragment {

    private SearchTabViewModel mViewModel;
    private Button search;
    private EditText email;

    public static SearchTabFragment newInstance() {
        return new SearchTabFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_tab, container, false);
        setupSearchUserButton(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchTabViewModel.class);
        // TODO: Use the ViewModel

    }

    private void setupSearchUserButton(View view) {
        email = view.findViewById(R.id.email);
        search = view.findViewById(R.id.enter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserProfile();
            }
        });
    }

    private void showUserProfile() {
        Activity currentActivity = getActivity();
        if (currentActivity != null) {
            if (email.getText() != null) {
                Intent intent = new Intent(currentActivity, FindUserActivity.class);
                intent.putExtra("email", email.getText().toString());
                startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.slide_up, R.anim.no_slide);
            }
        }
    }
}
