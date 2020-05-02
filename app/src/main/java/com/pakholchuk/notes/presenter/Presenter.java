package com.pakholchuk.notes.presenter;

import android.content.Context;
import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.helpers.ImageHelper;
import com.pakholchuk.notes.repository.Repository;
import com.pakholchuk.notes.view.EditNoteFragment;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class Presenter implements Contract.PresenterContract {
    private Contract.ViewContract view;
    private final Contract.RepositoryContract repository;
    private Bundle bundle;
    private Note note;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ImageHelper imageHelper;

    public Presenter(Contract.ViewContract view) {
        this.view = view;
        this.repository = new Repository();
        imageHelper = new ImageHelper((Context)view);
    }

    @Override
    public void viewReady() {
        disposables.add(repository.updateList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> view.updateList((ArrayList<Note>) notes)));
    }

    @Override
    public void newNotePressed() {
        view.showEditNote(EditNoteFragment.TAG_ADD, null);
    }

    @Override
    public void add() {
        Bundle b = view.getDataFromUser();
        view.closeNote();
        repository.insert(b);
    }


    @Override
    public void edit() {
        view.closeNote();
        view.showEditNote(EditNoteFragment.TAG_EDIT, bundle);
    }

    @Override
    public void save() {
        Bundle b = view.getDataFromUser();
        view.closeNote();
        repository.update(note, b);
    }

    @Override
    public void delete() {
        repository.delete(note);
        view.closeNote();
    }

    @Override
    public void clearList() {
        disposables.add(repository.clearAll()
                .doOnComplete(imageHelper::deleteAllImages)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    @Override
    public void itemClicked(int position, long noteId) {
        disposables.add(repository.loadNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::noteLoaded));
    }

    private void noteLoaded(Note note) {
        this.note = note;
        bundle = new Bundle();
        bundle.putString(NoteConstants.NAME, note.getName());
        bundle.putString(NoteConstants.BODY, note.getBody());
        bundle.putString(NoteConstants.CREATION, note.getCreationDate());
        bundle.putString(NoteConstants.EDIT, note.getLastEditDate());
        bundle.putString(NoteConstants.IMAGE, note.getImgPath());
        view.showNote(bundle);
    }

    @Override
    public void imagePressed() {
        view.showImage(note.getImgPath());
    }

    @Override
    public void cachedImagePressed() {
        view.showImage(view.getCachedImagePath());
    }

    @Override
    public void onActivityDestroyed() {
        imageHelper = null;
        view = null;
        disposables.dispose();
    }
}
