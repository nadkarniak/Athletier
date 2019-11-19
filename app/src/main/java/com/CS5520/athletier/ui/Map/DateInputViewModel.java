package com.CS5520.athletier.ui.Map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateInputViewModel extends ViewModel {
    private MutableLiveData<Date> selectedDate = new MutableLiveData<>();

    public DateInputViewModel() { }

    void setSelectedDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyy", Locale.US);
        try {
            Date date = format.parse(dateString);
            selectedDate.setValue(date);
        } catch (ParseException exception) {
            System.out.println("Failed to parse date: " + dateString);
        }
    }

}
