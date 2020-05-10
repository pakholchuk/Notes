package com.pakholchuk.notes.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.pakholchuk.notes.R;
import com.pakholchuk.notes.arch.BaseFragment;
import com.pakholchuk.notes.contracts.EditContract;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.databinding.FragmentEditBinding;
import com.pakholchuk.notes.helpers.ImageHelper;
import com.pakholchuk.notes.presenter.EditPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EditFragment extends BaseFragment<EditContract.View, EditContract.Presenter>
        implements EditContract.View {
    public static final String TAG_ADD = "add";
    public static final String TAG_EDIT = "edit";
    private static final int LOAD_PIC_REQUEST_CODE = 22;

    private FragmentEditBinding binding;
    private ImageHelper imageHelper = new ImageHelper();
    private String defaultStringValue = "";
    private CompositeDisposable disposables = new CompositeDisposable();
    private Bundle arguments;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(binding.getRoot());
        setListeners();
        if (getArguments() != null) {
            arguments = getArguments();
            String tag = "" + arguments.getString("tag");
            switch (tag) {
                case TAG_ADD: {
                    onAddFragmentCreate();
                    break;
                }
                case TAG_EDIT: {
                    onEditFragmentCreate();
                    break;
                }
            }
        }
        presenter.onViewCreated(getArguments());
    }

    private void setListeners() {
        binding.btnSave.setOnClickListener(v -> presenter.onSaveClick(getUserDataBundle()));
        binding.btnSaveNew.setOnClickListener(v -> presenter.onSaveNewClick(getUserDataBundle()));
        binding.btnClose.setOnClickListener(v -> presenter.onCloseClick());
        binding.btnDelete.setOnClickListener(v -> presenter.onDeleteClick());
        binding.ivPickImage.setOnClickListener(v -> presenter.onPickImageClick());
        binding.ivPreviewInEditFragment.setOnClickListener(v -> presenter.onImageClick());
        binding.getRoot().setOnClickListener(v -> {
        });
    }

    private Bundle getUserDataBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(NoteConstants.NAME, String.valueOf(binding.etFragmentName.getText()));
        bundle.putString(NoteConstants.BODY, String.valueOf(binding.etFragmentBody.getText()));
        if (getArguments().getString(NoteConstants.IMAGE) != null) {
            bundle.putString(NoteConstants.IMAGE,
                    getArguments().getString(NoteConstants.IMAGE));
        } else bundle.putString(NoteConstants.IMAGE, "");
        return bundle;
    }

    private void onAddFragmentCreate() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.btnSave.setVisibility(View.GONE);
        binding.btnSaveNew.setVisibility(View.VISIBLE);
    }

    private void onEditFragmentCreate() {
        if (arguments != null) {
            binding.etFragmentName.setText(arguments.getString(NoteConstants.NAME, defaultStringValue));
            binding.etFragmentBody.setText(arguments.getString(NoteConstants.BODY, defaultStringValue));
            String savedImagePath = arguments.getString(NoteConstants.IMAGE, defaultStringValue);
            if (!"".equals(savedImagePath)) {
                setPreviewImage(savedImagePath);
            }
        }
        binding.btnSave.setVisibility(View.VISIBLE);
        binding.btnSave.setEnabled(true);
        binding.btnSaveNew.setVisibility(View.GONE);
        binding.btnSaveNew.setEnabled(false);

    }

    @Override
    public void setPreviewImage(String imagePath) {
        disposables.add(imageHelper.getPreviewBitmap(imagePath, getDeviceDensity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bmp -> binding.ivPreviewInEditFragment.setImageBitmap(bmp)));
    }

    private float getDeviceDensity() {
        return getContext().getResources().getDisplayMetrics().density;
    }

    @Override
    public void showImage(String imgPath) {
        Bundle bundle = new Bundle();
        bundle.putString(NoteConstants.IMAGE, imgPath);
        navController.navigate(R.id.action_edit_to_image, bundle);
    }

    @Override
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, LOAD_PIC_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LOAD_PIC_REQUEST_CODE: {
                    Uri uri = data.getData();
                    presenter.onImagePicked(uri.toString(), getImagesDirPath());
                    break;
                }
            }
        }
    }

    @Override
    public void close() {
        navController.popBackStack();
    }

    private String getImagesDirPath() {
        return (getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString();
    }

    @Override
    public void onDestroyView() {
        disposables.dispose();
        super.onDestroyView();
    }

    @Override
    protected EditContract.Presenter initPresenter() {
        return new EditPresenter();
    }
}
