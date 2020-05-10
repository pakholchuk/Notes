package com.pakholchuk.notes.contracts;

import android.os.Bundle;

import com.pakholchuk.notes.arch.BaseContract;

public interface DetailsContract {

    interface View extends BaseContract.View {
        void showEditNote(Bundle bundle);

        void showImage(String imgPath);

        void close();
    }

    interface Presenter extends BaseContract.Presenter<DetailsContract.View> {
        void onViewCreated(Bundle bundle);

        void onEditClick();

        void onDeleteClick();

        void onImageClick();
    }
}
