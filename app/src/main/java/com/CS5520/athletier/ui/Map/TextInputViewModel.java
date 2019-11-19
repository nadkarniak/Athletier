package com.CS5520.athletier.ui.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TextInputViewModel extends ViewModel {
    private MutableLiveData<String> textInput = new MutableLiveData<>();
    private MutableLiveData<Boolean> hasInput = new MutableLiveData<>(false);

    public TextInputViewModel() { }

    LiveData<String> getTextInput() {
        return textInput;
    }


    LiveData<Boolean> getHasInput() {
        return hasInput;
    }

    void setTextInput(String input) {
        textInput.setValue(input);
    }

    void setHasInput(boolean input) {
        hasInput.setValue(input);
    }
}
