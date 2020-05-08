package com.pakholchuk.notes.arch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment<V extends BaseContract.View, P extends BaseContract.Presenter<V>>
        extends Fragment implements BaseContract.View {
    private LifecycleRegistry lifecycleRegistry;
    protected P presenter;

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseViewModel<V,P> viewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        boolean isPresenterCreated = false;
        if (viewModel.getPresenter() == null) {
            viewModel.setPresenter(initPresenter());
            isPresenterCreated = true;
        }
        presenter = viewModel.getPresenter();
        presenter.attachLifecycle(getLifecycle());
        presenter.attachView((V) this);
        if (isPresenterCreated) {
            presenter.onPresenterCreated();
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry == null ?
                lifecycleRegistry = new LifecycleRegistry(this) :
                lifecycleRegistry;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachLifecycle(getLifecycle());
        presenter.detachView();
    }

    protected abstract P initPresenter();
}
