package com.CS5520.athletier.ui.Map;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;

import java.util.Calendar;
import java.util.Date;

public class DateInputFragment extends Fragment {
    private DateInputViewModel viewModel;
    private TextView titleTextView;
    private EditText dateInputText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_input, container, false);
        titleTextView = view.findViewById(R.id.inputTextFragTitle);
        dateInputText = view.findViewById(R.id.inputEditText);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DateInputViewModel.class);
        setupTextViews();
    }

    public LiveData<Date> getSelectedDate() {
        return viewModel.getSelectedDate();
    }

    private void setupTextViews() {
        titleTextView.setText(R.string.date_title);

        Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        // Set default date to today's date - Month must be adjusted because Android assumes month
        // 0 is January and month 11 is December
        dateInputText.setText(dateToString(currentYear, currentMonth % 12 + 1, currentDay));
        viewModel.setSelectedDate(dateInputText.getText().toString());

        // Prevent date input edit text from being selected and from showing keyboard
        dateInputText.setFocusable(false);
        dateInputText.setShowSoftInputOnFocus(false);
        dateInputText.setInputType(0);

        // Show date picker dialog on click
        dateInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePickerDialog(currentYear, currentMonth, currentDay);
            }
        });
    }

    private void createDatePickerDialog(int currentYear, int currentMonth, int currentDay) {
        Context context = getContext();
        if (context == null) { return; }

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Android Calendar months start at 0 (January) so correction is needed to display
                // correct month number
                String dateString = dateToString(year, month % 12 + 1, day);
                dateInputText.setText(dateString);
                viewModel.setSelectedDate(dateString);
            }
        }, currentYear, currentMonth, currentDay);

        // Set minimum allowable date to current date minus 1 second - This cannot be set to the
        // exact current time
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private String dateToString(int year, int month, int day) {
        String dayString = day < 10 ? "0" + day : "" + day;
        String monthString = month < 10 ? "0" + month : "" + month;
        return monthString + "-" + dayString + "-" + year;
    }
}
