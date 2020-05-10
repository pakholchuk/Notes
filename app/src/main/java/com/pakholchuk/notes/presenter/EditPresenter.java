package com.pakholchuk.notes.presenter;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.pakholchuk.notes.arch.BasePresenter;
import com.pakholchuk.notes.contracts.ContractRepository;
import com.pakholchuk.notes.contracts.EditContract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.helpers.ImageHelper;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EditPresenter extends BasePresenter<EditContract.View>
        implements EditContract.Presenter {

    private Note note;
    private ContractRepository repository = new com.pakholchuk.notes.repository.Repository();
    private String tempImagePath;
    private ImageHelper imageHelper = new ImageHelper();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onViewCreated(@Nullable Bundle bundle) {
        if (bundle != null && note == null) {
            note = new Note(bundle.getLong(NoteConstants.ID),
                    bundle.getString(NoteConstants.NAME),
                    bundle.getString(NoteConstants.BODY),
                    bundle.getString(NoteConstants.IMAGE),
                    bundle.getString(NoteConstants.CREATION),
                    bundle.getString(NoteConstants.EDIT));
        }
        if (tempImagePath != null) {
            getView().setPreviewImage(tempImagePath);
        }
    }

    @Override
    public void onSaveNewClick(Bundle bundle) {
        getView().close();
        if (tempImagePath != null) {
            bundle.putString(NoteConstants.IMAGE, tempImagePath);
        } else {
            bundle.putString(NoteConstants.IMAGE, "");
        }
        repository.insert(bundle);
    }

    @Override
    public void onSaveClick(Bundle bundle) {
        getView().close();
        if (tempImagePath != null) {
            bundle.putString(NoteConstants.IMAGE, tempImagePath);
            if (!note.getImgPath().equals("")) {
                imageHelper.deleteImage(note.getImgPath());
            }
        }
        repository.update(note, bundle);
    }

    @Override
    public void onPickImageClick() {
        getView().pickImage();
    }

    @Override
    public void onImagePicked(String pickedImagePath, String appImagesDirPath) {
        disposables.add(imageHelper.saveImageToApp(pickedImagePath, appImagesDirPath)
                .subscribeOn(Schedulers.io())
                .subscribe(tempImagePath -> {
                    this.tempImagePath = tempImagePath;
                    getView().setPreviewImage(tempImagePath);
                }));
    }

    @Override
    public void onImageClick() {
        if (tempImagePath != null) {
            getView().showImage(tempImagePath);
        } else {
            getView().showImage(note.getImgPath());
        }
    }

    @Override
    public void onDeleteClick() {
        getView().close();
        repository.delete(note);
        if (!note.getImgPath().equals("")) {
            imageHelper.deleteImage(note.getImgPath());
        }
    }

    @Override
    public void onCloseClick() {
        getView().close();
        if (tempImagePath != null) {
            imageHelper.deleteImage(tempImagePath);
        }
    }

    @Override
    public void onPresenterDestroy() {
        Log.d("mytag", "onPresenterDestroy: ");
        super.onPresenterDestroy();
        disposables.dispose();
    }
}
