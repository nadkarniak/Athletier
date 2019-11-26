package com.CS5520.athletier.ui.Challenges;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SegmentedSelectorViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedPosition = new MutableLiveData<>();

    LiveData<Integer> getSelectedPosition() {
        return selectedPosition;
    }

    void setSelectedPosition(int index) {
        selectedPosition.setValue(index);
    }
}
