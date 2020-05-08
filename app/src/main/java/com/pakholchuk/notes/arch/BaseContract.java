package com.pakholchuk.notes.arch;

import android.os.Bundle;

import androidx.lifecycle.Lifecycle;

public interface BaseContract {
    interface View {

    }

    interface Presenter<V extends BaseContract.View> {
        void attachLifecycle(Lifecycle lifecycle);
        void detachLifecycle(Lifecycle lifecycle);
        void attachView(V view);
        void detachView();
        V getView();
        Bundle getStateBundle();
        boolean isViewAttached();
        void onPresenterCreated();
        void onPresenterDestroy();
    }
}
