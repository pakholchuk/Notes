package com.pakholchuk.notes.presenter;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteFields;
import com.pakholchuk.notes.repository.Repository;
import com.pakholchuk.notes.view.EditNoteFragment;


public class Presenter implements Contract.PresenterContract {
    private Contract.ViewContract view;
    private final Contract.RepositoryContract repository;
    private Bundle bundle;
    private Note note;
    private long noteId;
    private int positionInRecycler;

    public Presenter(Contract.ViewContract view) {
        this.view = view;
        this.repository = new Repository();
    }

    @Override
    public void viewReady() {
        view.showList(repository.getAllNotes());
    }

    @Override
    public void newNotePressed() {
        view.showEditFragment(EditNoteFragment.TAG_ADD, null);
    }

    @Override
    public void add() {
        view.showProgress();
        Bundle b = view.getDataFromUser();
        Note newNote = repository.insert(b);
        view.addItem(newNote);
        view.hideProgress();
        view.closeNote();
    }

    @Override
    public void edit() {
        view.closeNote();
        view.showEditFragment(EditNoteFragment.TAG_EDIT, bundle);
    }

    @Override
    public void save() {
        view.showProgress();
        Bundle b = view.getDataFromUser();
        repository.update(note, b);
        note = repository.getNote(noteId);
        view.editItem(positionInRecycler, note);
        view.hideProgress();
        view.closeNote();
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
        this.noteId = noteId;
        positionInRecycler = position;
        note = repository.getNote(noteId);
        bundle = new Bundle();
        bundle.putString(NoteFields.NAME, note.getName());
        bundle.putString(NoteFields.BODY, note.getBody());
        bundle.putString(NoteFields.CREATION, note.getCreationDate());
        bundle.putString(NoteFields.EDIT, note.getLastEditDate());
        bundle.putString(NoteFields.IMAGE, note.getImgPath());
        view.showNote(bundle);
    }

    @Override
    public void imagePressed() {
        view.showImageFragment(note.getImgPath());
    }

    @Override
    public void detachView() {
        view = null;
    }
}
