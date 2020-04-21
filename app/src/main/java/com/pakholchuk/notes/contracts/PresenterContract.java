package com.pakholchuk.notes.contracts;

import android.view.View;

public interface PresenterContract {
    void add();
    void edit();
    void delete();
    void clearList();
    void attachView();
    void detachView();

    void itemClicked(View view, int position);
}
