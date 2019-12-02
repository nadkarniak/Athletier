package com.CS5520.athletier.ui.Challenges;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Map.SpinnerInputViewModel;

import java.util.List;

public class ColoredSpinnerFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private SpinnerInputViewModel viewModel;
    private Spinner dropdownSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colored_spinner,
                container, false);
        dropdownSpinner = view.findViewById(R.id.coloredSpinner);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SpinnerInputViewModel.class);
    }

    void setSpinnerOptions(Context context, List<String> spinnerOptions) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                R.layout.colored_spinner_text, spinnerOptions);
        dropdownSpinner.setAdapter(spinnerAdapter);
        dropdownSpinner.setOnItemSelectedListener(this);
    }

    LiveData<String> getSelectedItem() {
        return viewModel.getSelectedItem();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (viewModel != null) {
            viewModel.setSelectedItem((String) adapterView.getItemAtPosition(i));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }



}
