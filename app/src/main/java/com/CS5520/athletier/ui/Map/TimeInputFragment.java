package com.CS5520.athletier.ui.Map;

import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;

import java.util.Calendar;
import java.util.Date;

public class TimeInputFragment extends Fragment {
    private TimeInputViewModel viewModel;
    private TextView titleTextView;
    private EditText timeInputText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_input, container, false);
        titleTextView = view.findViewById(R.id.inputTextFragTitle);
        titleTextView.setText(R.string.time_title);
        timeInputText = view.findViewById(R.id.inputEditText);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TimeInputViewModel.class);
        setupTimeInputText();
    }

    public LiveData<Date> getSelectedTime() {
        return viewModel.getSelectedTime();
    }

    private void setupTimeInputText() {
        // Disable keyboard popup on edit text
        timeInputText.setFocusable(false);
        timeInputText.setShowSoftInputOnFocus(false);
        timeInputText.setInputType(0);

        // Default edit text to display current time
        setTimeInputText(Calendar.getInstance().getTime());
        viewModel.setSelectedTime(Calendar.getInstance().getTime());

        timeInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);
                displayTimePicker(hour, min);
            }
        });
    }

    private void displayTimePicker(int currentHour, int currentMin) {
        Context context = getContext();
        if (context == null) { return; }

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                Calendar newCalender = Calendar.getInstance();
                newCalender.set(Calendar.HOUR_OF_DAY, hour);
                newCalender.set(Calendar.MINUTE, min);
                Date selectedDate = newCalender.getTime();
                setTimeInputText(selectedDate);

                System.out.println(selectedDate);
                viewModel.setSelectedTime(selectedDate);
            }
        }, currentHour, currentMin, false);

        timePickerDialog.show();
    }

    private void setTimeInputText(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        timeInputText.setText(dateFormat.format(date));
    }
    
}
