package com.CS5520.athletier.ui.Challenges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.CS5520.athletier.R;


public class SelectWinnerDialogFragment extends DialogFragment {

    public interface SelectedWinnerDialogListener {
        public void onDialogPositiveClick(String selectedWinner);
    }

    SelectedWinnerDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SelectedWinnerDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Context does not imlement SelectedWinnerDialogListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] options = { "I won", "I lost" };
        final String[] selectedWinner = new String[]{options[0]};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set dialog title
        builder.setTitle(R.string.select_winner_title)
                .setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedWinner[0] = options[i];
                    }
                })
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogPositiveClick(selectedWinner[0]);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();
    }


}
