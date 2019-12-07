package com.CS5520.athletier.ui.Challenges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.R;


public class SelectWinnerDialogFragment extends DialogFragment {

    public interface SelectedWinnerDialogListener {
        void onDialogPositiveClick(boolean reportingAsHost,
                                   boolean hostSelectedAsWinner);
    }

    private SelectedWinnerDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SelectedWinnerDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent does not implement SelectedWinnerDialogListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle arguments = getArguments();
        if (arguments == null) {
            return builder.create();
        }

        final boolean reportingAsHost = arguments.getBoolean(Challenge.hostIdKey);



        // Radio button titles to show in the dialogue
        final String[] options = {getString(R.string.i_won), getString(R.string.i_lost)};

        // Array of one String for the selected winner. This needs to be an array so
        // it can be modified in the OnClick method of the .setSingleChoiceItems below
        final String[] selectedWinner = { options[0] };

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
                        if (reportingAsHost) {
                            listener.onDialogPositiveClick(
                                    true,
                                    selectedWinner[0].equals(options[0])
                            );
                        } else {
                            listener.onDialogPositiveClick(
                                    false,
                                    selectedWinner[0].equals(options[1])
                            );
                        }
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
