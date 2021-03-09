package com.example.sportracker.Dialogs.EnterContestName;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.sportracker.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EnterContestNameDialog extends DialogFragment {
    private final CompletableFuture<String> completableFuture;

    public EnterContestNameDialog(CompletableFuture<String> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @NotNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_enter_contest_name, null);
        builder.setTitle("Contest Name").setView(dialogView)
                // Add action buttons
                .setPositiveButton("CONTINUE", (dialog, id) -> {
                    String contestName = ((EditText) dialogView.findViewById(R.id.contestNameEditText)).getText().toString();
                    if (contestName == null || contestName.length() == 0) {
                        Toast.makeText(getContext(), "Insert a contest name", Toast.LENGTH_SHORT).show();
                        completableFuture.complete(null);
                    } else {
                        completableFuture.complete(contestName);
                    }
                })
                .setNegativeButton("CANCEL", (dialog, id) -> completableFuture.complete(null));
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(arg0 -> {
            Button[] buttons = {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE),
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            };
            for (Button button : buttons) {
                button.setTextColor(0xFFFFFFFF);
                button.setBackgroundColor(0x00FFFFFF);
            }
        });

        return dialog;
    }
}
