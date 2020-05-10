package com.pakholchuk.notes.contracts;

import android.os.Bundle;

import com.pakholchuk.notes.arch.BaseContract;

public interface EditContract {
    interface View extends BaseContract.View {
        void setPreviewImage(String path);

        void showImage(String imgPath);

        void pickImage();

        void close();

    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onViewCreated(Bundle bundle);

        void onSaveNewClick(Bundle bundle);

        void onSaveClick(Bundle bundle);

        void onPickImageClick();

        void onImagePicked(String pickedImagePath, String appImagesDirPath);

        void onImageClick();

        void onDeleteClick();

        void onCloseClick();
    }
}
