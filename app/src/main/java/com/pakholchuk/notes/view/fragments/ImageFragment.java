package com.pakholchuk.notes.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.databinding.FragmentImageBinding;
import com.pakholchuk.notes.view.OnFragmentEventListener;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {
    private FragmentImageBinding binding;
    private OnFragmentEventListener eventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater, container, false);
        binding.getRoot().setOnClickListener(v -> {
        });
        eventListener = (OnFragmentEventListener) getActivity();
        binding.ivCloseImage.setOnClickListener(eventListener::onFragmentViewClick);
        loadImage();
        return binding.getRoot();
    }

    private void loadImage() {
        if (getArguments() != null) {
            String path = "file://" + (getArguments().getString(NoteConstants.IMAGE));
            Picasso.get().load(path)
                    .into(binding.image);
        }
    }

}
