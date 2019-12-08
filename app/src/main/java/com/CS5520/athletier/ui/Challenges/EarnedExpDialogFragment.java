package com.CS5520.athletier.ui.Challenges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.ExpEarnedInfo;

public class EarnedExpDialogFragment extends DialogFragment {
    public static final String EXP_KEY = "EXP";

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle arguments = getArguments();
        if (arguments != null) {
            ExpEarnedInfo info = arguments.getParcelable(EXP_KEY);
            if (info == null) { return builder.create(); }
            int expGained = info.getExpEarned();
            int userTier = info.getTier();
            String sportName = info.getSportName();

            String msg = "You earned " + expGained + "pts in " + sportName + "!\n" +
                    "Your are now tier " + userTier + "!";
            builder.setTitle(R.string.congratulations)
                    .setMessage(msg)
                    .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
        }
        return builder.create();
    }

}
