package com.pakholchuk.notes.arch;

import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

public abstract class BasePresenter<V extends BaseContract.View>
        implements LifecycleObserver, BaseContract.Presenter<V> {

    private Bundle stateBundle;
    private V view;

    @Override
    final public void attachLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override
    final public void detachLifecycle(Lifecycle lifecycle) {
        lifecycle.removeObserver(this);
    }

    @Override
    final public void attachView(V view) {
        this.view = view;
    }

    @Override
    final public void detachView() {
        view = null;
    }

    @Override
    final public V getView() {
        return view;
    }

    @Override
    final public boolean isViewAttached() {
        return (view != null);
    }

    @Override
    final public Bundle getStateBundle() {
        return stateBundle == null ?
                stateBundle = new Bundle() : stateBundle;
    }

    @Override
    public void onPresenterCreated() {

    }

    @Override
    public void onPresenterDestroy() {
        if (stateBundle != null && !stateBundle.isEmpty()) {
            stateBundle.clear();
        }
    }
}
