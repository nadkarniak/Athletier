package com.CS5520.athletier.ui.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpinnerInputViewModel extends ViewModel {
    private MutableLiveData<String> selectedItem = new MutableLiveData<>();

    public SpinnerInputViewModel() { }

    LiveData<String> getSelectedItem() {
        return selectedItem;
    }

    void setSelectedItem(String item) {
        selectedItem.setValue(item);
    }
}
