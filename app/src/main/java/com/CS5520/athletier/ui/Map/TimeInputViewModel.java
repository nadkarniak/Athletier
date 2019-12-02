package com.CS5520.athletier.ui.Map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;

public class TimeInputViewModel extends AndroidViewModel {
    private MutableLiveData<Date> selectedTime;


    public TimeInputViewModel(@NonNull Application application) {
        super(application);
        this.selectedTime = new MutableLiveData<>();
    }

    LiveData<Date> getSelectedTime() {
        return selectedTime;
    }

    void setSelectedTime(Date time) {
        selectedTime.setValue(time);
    }
}
