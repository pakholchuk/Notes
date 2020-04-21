package com.pakholchuk.notes.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pakholchuk.notes.R;

public class DeleteAllDialogFragment extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }
    private DialogListener dialogListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogListener = (DialogListener) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_all_sure)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogListener.onDialogPositiveClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogListener.onDialogNegativeClick();
                    }
                });
        return builder.create();
    }
}
