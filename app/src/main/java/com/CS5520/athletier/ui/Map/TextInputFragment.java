package com.CS5520.athletier.ui.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;

public class TextInputFragment extends Fragment {

    private TextInputViewModel viewModel;
    private TextView titleTextView;
    private EditText inputEditText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_input, container, false);
        titleTextView = view.findViewById(R.id.inputTextFragTitle);
        inputEditText = view.findViewById(R.id.inputEditText);
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean hasInput = inputEditText.getText().length() > 0;
                viewModel.setHasInput(hasInput);
                if (hasInput) {
                    viewModel.setTextInput(inputEditText.getText().toString());
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TextInputViewModel.class);
    }


    public void setTitleTextView(String text) {
        titleTextView.setText(text);
    }

    public void setInputEditTextType(int inputType) {
        inputEditText.setInputType(InputType.TYPE_CLASS_TEXT | inputType);
    }

    public LiveData<String> getInputText() {
        return viewModel.getTextInput();
    }

    public LiveData<Boolean> getHasNonEmptyInput() { return viewModel.getHasInput(); }
}
