package com.pakholchuk.notes.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.databinding.FragmentImageBinding;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {
    FragmentImageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater, container, false);
        binding.getRoot().setOnClickListener(v -> {});
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String path = "file://" + (getArguments().getString(NoteConstants.IMAGE));
        if (getArguments() != null) {
            Picasso.get().load(path)
                    .into(binding.image);
        }
    }
}
