package com.pakholchuk.notes.presenter;

import android.os.Bundle;
import android.util.Log;

import com.pakholchuk.notes.arch.BasePresenter;
import com.pakholchuk.notes.contracts.ContractRepository;
import com.pakholchuk.notes.contracts.MainContract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.helpers.ImageHelper;
import com.pakholchuk.notes.view.fragments.EditFragment;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    ContractRepository repository = new com.pakholchuk.notes.repository.Repository();
    CompositeDisposable disposables = new CompositeDisposable();
    ImageHelper imageHelper = new ImageHelper();

    @Override
    public void onViewCreated() {
        disposables.add(repository.getAll()
                .doOnNext(list -> Log.d("FATAL_TAG", "onNext" + list.size()))
//                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> getView().updateList((ArrayList<Note>) notes)));
    }

    @Override
    public void onNewNoteClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("tag", EditFragment.TAG_ADD);
        getView().showEditNote(bundle);
    }

    @Override
    public void onClearListClicked(String imagesDirPath) {
        disposables.add(repository.clearAll()
                .doOnComplete(() -> imageHelper.deleteAllImages(imagesDirPath))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    @Override
    public void onItemClicked(long noteId) {
        disposables.add(repository.loadNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::noteLoaded));
    }

    private void noteLoaded(Note note) {
        Bundle bundle = new Bundle();
        bundle.putLong(NoteConstants.ID, note.getId());
        bundle.putString(NoteConstants.NAME, note.getName());
        bundle.putString(NoteConstants.BODY, note.getBody());
        bundle.putString(NoteConstants.IMAGE, note.getImgPath());
        bundle.putString(NoteConstants.CREATION, note.getCreationDate());
        bundle.putString(NoteConstants.EDIT, note.getLastEditDate());
        getView().showNote(bundle);
    }
}
