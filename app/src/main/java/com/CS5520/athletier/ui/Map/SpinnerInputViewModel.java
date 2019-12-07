package com.CS5520.athletier.ui.Map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpinnerInputViewModel extends AndroidViewModel {
    private MutableLiveData<String> selectedItem;

    public SpinnerInputViewModel(@NonNull Application application) {
        super(application);
        this.selectedItem = new MutableLiveData<>();
    }

    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String item) {
        selectedItem.setValue(item);
    }
}
