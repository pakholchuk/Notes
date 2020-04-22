package com.pakholchuk.notes.presenter;


import android.os.Bundle;
import android.view.View;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.repository.Repository;
import com.pakholchuk.notes.view.EditNoteFragment;


public class Presenter implements Contract.PresenterContract {
    private Contract.ViewContract view;
    private final Contract.RepositoryContract repository;
    private Bundle bundle;
    private Note note;
    private int noteId;


    public Presenter(Contract.ViewContract view) {
        this.view = view;
        this.repository = Repository.getInstance();
    }

    @Override
    public void add() {
        view.showProgress();
        Bundle b = view.getDataFromUser();
        repository.newNote(b);
        view.addItem(note);
        view.hideProgress();
        view.closeNote();
    }

    @Override
    public void newNotePressed() {
        view.showEditFragment(EditNoteFragment.TAG_ADD, null);
    }

    @Override
    public void save() {
        view.showProgress();
        Bundle b = view.getDataFromUser();
        repository.editNote(noteId, b);
        note = repository.getNote(noteId);
        view.editItem(noteId, note);
        view.hideProgress();
        view.closeNote();
    }

    @Override
    public void edit() {
        view.closeNote();
        view.showEditFragment(EditNoteFragment.TAG_EDIT, bundle);
    }

    @Override
    public void delete() {
        view.showProgress();
        repository.deleteNote(noteId);
        view.removeItem(noteId);
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
    public void detachView() {
        view = null;
    }

    @Override
    public void itemClicked(int id) {
        noteId = id;
        note = repository.getNote(id);
        bundle = new Bundle();
        bundle.putString(Note.NAME, note.getName());
        bundle.putString(Note.BODY, note.getBody());
        bundle.putString(Note.CREATION, note.getCreationDate());
        bundle.putString(Note.EDIT, note.getLastEditDate());
        bundle.putString(Note.IMAGE, note.getImgPath());
        view.showNote(bundle);
    }

    @Override
    public void imagePressed() {
        view.showImageFragment(note.getImgPath());
    }
}
