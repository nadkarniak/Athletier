package com.CS5520.athletier.ui.Challenges;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SegmentedSelectorViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> selectedPosition;

    public SegmentedSelectorViewModel(@NonNull Application application) {
        super(application);
        selectedPosition = new MutableLiveData<>();
    }

    LiveData<Integer> getSelectedPosition() {
        return selectedPosition;
    }

    void setSelectedPosition(int index) {
        selectedPosition.setValue(index);
    }
}
