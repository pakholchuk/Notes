package com.pakholchuk.notes.view;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.databinding.FragmentNoteBinding;
import com.pakholchuk.notes.helpers.ImageHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NoteFragment extends Fragment {
    public static final String TAG_SHOW = "show";
    private OnFragmentEventListener buttonClickListener;
    private FragmentNoteBinding binding;
    private ImageHelper imageHelper;
    private final String defaultValue = "";
    private String imagePath;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        imageHelper = new ImageHelper(getContext());
        setListeners();
        fillFields();
        return binding.getRoot();
    }

    private void setListeners() {
        buttonClickListener = (OnFragmentEventListener) getActivity();
        View.OnClickListener onClickListener = v -> buttonClickListener.onFragmentViewClick(v);
        binding.btnClose.setOnClickListener(onClickListener);
        binding.btnDelete.setOnClickListener(onClickListener);
        binding.btnEdit.setOnClickListener(onClickListener);
        binding.ivPreview.setOnClickListener(onClickListener);
        binding.getRoot().setOnClickListener(v->{});
    }

    private void fillFields() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.tvFragmentName.setText(bundle.getString(NoteConstants.NAME, defaultValue));
            binding.tvFragmentBody.setText(bundle.getString(NoteConstants.BODY, defaultValue));
            binding.tvFragmentCreationDate.setText(bundle.getString(NoteConstants.CREATION, defaultValue));
            binding.tvFragmentEditdate.setText(bundle.getString(NoteConstants.EDIT, defaultValue));
            imagePath = bundle.getString(NoteConstants.IMAGE);
            if (!"".equals(imagePath)) {
                Uri imageUri = Uri.parse("file://" + imagePath);
                disposables.add(imageHelper.loadImage(imageUri)
                        .map(imageHelper::createPreviewBitmap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bmp -> binding.ivPreview.setImageBitmap(bmp)));
            }
        }
    }

    @Override
    public void onDestroyView() {
        disposables.dispose();
        super.onDestroyView();
    }
}
