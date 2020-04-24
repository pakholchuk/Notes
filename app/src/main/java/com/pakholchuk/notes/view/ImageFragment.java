package com.pakholchuk.notes.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pakholchuk.notes.data.NoteFields;
import com.pakholchuk.notes.databinding.FragmentImageBinding;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {
    FragmentImageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Picasso.get().load(getArguments().getString(NoteFields.IMAGE))
                    .into(binding.image);
        }

    }
}
