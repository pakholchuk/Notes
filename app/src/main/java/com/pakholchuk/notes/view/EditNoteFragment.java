package com.pakholchuk.notes.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pakholchuk.notes.databinding.FragmentEditNoteBinding;

public class EditNoteFragment extends Fragment {
    public static final String TAG_ADD = "add";
    private OnFragmentButtonClickListener buttonClickListener;
    private FragmentEditNoteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false);
        buttonClickListener = (OnFragmentButtonClickListener) getActivity();
        String tag = getTag();
        if (TAG_ADD.equals(tag)) {
            onAddFragmentCreate();
        }
        return binding.getRoot();
    }

    private void onAddFragmentCreate() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.etFragmentBody.setText("");
        binding.etFragmentName.setText("");
        binding.etFragmentBody.setHint("Enter note");
        binding.etFragmentName.setHint("Enter name");
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener.onFragmentButtonClick(v);
            }
        };
        binding.btnSave.setOnClickListener(onClickListener);
        binding.btnClose.setOnClickListener(onClickListener);
        binding.btnDelete.setOnClickListener(onClickListener);
        binding.ivNewPicture.setOnClickListener(onClickListener);
    }

}
