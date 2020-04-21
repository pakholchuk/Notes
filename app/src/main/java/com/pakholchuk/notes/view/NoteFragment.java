package com.pakholchuk.notes.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.pakholchuk.notes.databinding.FragmentNoteBinding;

public class NoteFragment extends Fragment {

    private OnFragmentButtonClickListener buttonClickListener;
    private FragmentNoteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        buttonClickListener = (OnFragmentButtonClickListener) getActivity();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener.onFragmentButtonClick(v);
            }
        };
        binding.btnClose.setOnClickListener(onClickListener);
        binding.btnDelete.setOnClickListener(onClickListener);
        binding.btnEdit.setOnClickListener(onClickListener);
        binding.ivShowPicture.setOnClickListener(onClickListener);

    }
}
