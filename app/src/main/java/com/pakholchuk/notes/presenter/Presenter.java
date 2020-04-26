package com.pakholchuk.notes.presenter;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.repository.Repository;
import com.pakholchuk.notes.view.EditNoteFragment;

import java.util.ArrayList;
import java.util.List;


public class Presenter implements Contract.PresenterContract {
    private Contract.ViewContract view;
    private final Contract.RepositoryContract repository;
    private Bundle bundle;
    private Note note;
    private long noteId;
    private int positionInRecycler;

    public Presenter(Contract.ViewContract view) {
        this.view = view;
        this.repository = new Repository(this);
    }

    @Override
    public void viewReady() {
        repository.loadAllNotes();
    }

    @Override
    public void newNotePressed() {
        view.showEditFragment(EditNoteFragment.TAG_ADD, null);
    }

    @Override
    public void noteListReady(List<Note> noteList) {
        view.showList((ArrayList<Note>) noteList);
    }

    @Override
    public void add() {
        view.showProgress();
        Bundle b = view.getDataFromUser();
        repository.insert(b);
        view.closeNote();
    }

    @Override
    public void noteInserted(Note note) {
        this.note = note;
        view.addItem(note);
        view.hideProgress();
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
        view.closeNote();
    }

    @Override
    public void noteUpdated(Note note) {
        view.editItem(positionInRecycler, note);
        view.hideProgress();
    }

    @Override
    public void noteLoaded(Note note) {
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
        repository.loadNote(noteId);
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
