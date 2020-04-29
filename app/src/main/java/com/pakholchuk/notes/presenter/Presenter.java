package com.pakholchuk.notes.presenter;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.repository.Repository;
import com.pakholchuk.notes.view.EditNoteFragment;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class Presenter implements Contract.PresenterContract {
    private Contract.ViewContract view;
    private final Contract.RepositoryContract repository;
    private Bundle bundle;
    private Note note;
    private int positionInRecycler;
    private CompositeDisposable disposables = new CompositeDisposable();

    public Presenter(Contract.ViewContract view) {
        this.view = view;
        this.repository = new Repository();
    }

    @Override
    public void viewReady() {
        disposables.add(repository.loadAllNotes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> view.showList((ArrayList<Note>) notes)));
    }

    @Override
    public void newNotePressed() {
        view.showEditFragment(EditNoteFragment.TAG_ADD, null);
    }

    @Override
    public void add() {
        view.showProgress();
        Bundle b = view.getDataFromUser();
        view.closeNote();
        disposables.add(repository.insert(b)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> noteInserted(note)));
    }

    private void noteInserted(Note note) {
        this.note = note;
        view.addItem(note);
        view.hideProgress();
    }

    @Override
    public void edit() {
        view.showEditFragment(EditNoteFragment.TAG_EDIT, bundle);
    }

    @Override
    public void save() {
        view.showProgress();
        Bundle b = view.getDataFromUser();
        view.closeNote();
        disposables.add(repository.update(note, b)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> noteUpdated(note)));
    }

    private void noteUpdated(Note note) {
        view.editItem(positionInRecycler, note);
        view.hideProgress();
    }

    @Override
    public void delete() {
        view.showProgress();
        repository.delete(note);
        view.removeItem(positionInRecycler);
        view.hideProgress();
        view.closeNote();
    }

    @Override
    public void clearList() {
        view.showProgress();
        repository.clearAll();
        view.clearAll();
        view.hideProgress();
    }

    @Override
    public void itemClicked(int position, long noteId) {
        positionInRecycler = position;
        disposables.add(repository.loadNote(noteId)
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
        view.showImageFragment(note.getImgPath());
    }

    @Override
    public void onActivityDestroyed() {
        view = null;
        disposables.dispose();
    }
}
