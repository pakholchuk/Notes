package com.pakholchuk.notes.arch;

import androidx.lifecycle.ViewModel;

public final class BaseViewModel<V extends BaseContract.View,
        P extends BaseContract.Presenter<V>> extends ViewModel {

    private P presenter;

    void setPresenter(P presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        presenter.onPresenterDestroy();
        presenter = null;
    }
}
