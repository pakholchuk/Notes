package com.pakholchuk.notes.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.databinding.FragmentNoteBinding;

public class NoteFragment extends Fragment {
    public static final String TAG_SHOW = "show";
    private OnFragmentEventListener buttonClickListener;
    private FragmentNoteBinding binding;
    private final String defaultValue = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        buttonClickListener = (OnFragmentEventListener) getActivity();
        View.OnClickListener onClickListener = v -> buttonClickListener.onFragmentButtonClick(v);
        binding.btnClose.setOnClickListener(onClickListener);
        binding.btnDelete.setOnClickListener(onClickListener);
        binding.btnEdit.setOnClickListener(onClickListener);
        binding.ivShowPicture.setOnClickListener(onClickListener);
        binding.getRoot().setOnClickListener(v -> {});
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.tvFragmentName.setText(bundle.getString(NoteConstants.NAME, defaultValue));
            binding.tvFragmentBody.setText(bundle.getString(NoteConstants.BODY, defaultValue));
            binding.tvFragmentCreationDate.setText(bundle.getString(NoteConstants.CREATION, defaultValue));
            binding.tvFragmentEditdate.setText(bundle.getString(NoteConstants.EDIT, defaultValue));
            if (bundle.getString(NoteConstants.IMAGE).equals("")) {
                binding.ivShowPicture.setVisibility(View.GONE);
            }
        }

    }
}
