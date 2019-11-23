package com.CS5520.athletier.ui.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.CS5520.athletier.R;

public class TextDisplayFragment extends Fragment {

    private TextView titleText;
    private TextView detailsText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_display, container, false);
        titleText = view.findViewById(R.id.displayTextTitle);
        detailsText = view.findViewById(R.id.displayTextDetails);
        return view;
    }

    public void setTitleText(String title) {
        titleText.setText(title);
    }

    public void setDetailsText(String details) {
        detailsText.setText(details);
    }

}
