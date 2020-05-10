package com.pakholchuk.notes.view.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.pakholchuk.notes.R;
import com.pakholchuk.notes.arch.BaseFragment;
import com.pakholchuk.notes.contracts.DetailsContract;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.databinding.FragmentDetailsBinding;
import com.pakholchuk.notes.helpers.ImageHelper;
import com.pakholchuk.notes.presenter.DetailsPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsFragment extends BaseFragment<DetailsContract.View, DetailsContract.Presenter>
        implements DetailsContract.View {
    private FragmentDetailsBinding binding;
    private ImageHelper imageHelper = new ImageHelper();
    private String imagePath;
    private CompositeDisposable disposables = new CompositeDisposable();
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        fillFields();
        presenter.onViewCreated(getArguments());
        navController = Navigation.findNavController(binding.getRoot());
    }

    @Override
    protected DetailsContract.Presenter initPresenter() {
        return new DetailsPresenter();
    }

    private void setListeners() {
        binding.btnClose.setOnClickListener(v -> navController.popBackStack());
        binding.btnDelete.setOnClickListener(v -> presenter.onDeleteClick());
        binding.btnEdit.setOnClickListener(v -> presenter.onEditClick());
        binding.ivPreview.setOnClickListener(v -> presenter.onImageClick());
        binding.getRoot().setOnClickListener(v -> {
        });
    }

    private void fillFields() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.tvFragmentName.setText(bundle.getString(NoteConstants.NAME));
            binding.tvFragmentBody.setText(bundle.getString(NoteConstants.BODY));
            binding.tvFragmentCreationDate.setText(bundle.getString(NoteConstants.CREATION));
            binding.tvFragmentEditdate.setText(bundle.getString(NoteConstants.EDIT));
            imagePath = bundle.getString(NoteConstants.IMAGE);
            if (!"".equals(imagePath)) {
                setPreviewImage();
            }
        }
    }

    private void setPreviewImage() {
        disposables.add(imageHelper.getPreviewBitmap(imagePath, getDeviceDensity())
                .doOnSuccess((bmp) -> Log.d("FATAL ", "onSuccess: "))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bmp -> binding.ivPreview.setImageBitmap(bmp)));
    }

    private float getDeviceDensity() {
        return getContext().getResources().getDisplayMetrics().density;
    }

    @Override
    public void showEditNote(Bundle bundle) {
        navController.navigate(R.id.action_details_to_edit, bundle);
    }

    @Override
    public void showImage(String imgPath) {
        Bundle bundle = new Bundle();
        bundle.putString(NoteConstants.IMAGE, imgPath);
        navController.navigate(R.id.action_details_to_image, bundle);
    }

    @Override
    public void close() {
        navController.popBackStack();
    }

    @Override
    public void onDestroy() {
        Log.d("FATAL", "Details onDestroy: ");
        disposables.dispose();
        super.onDestroy();
    }
}
