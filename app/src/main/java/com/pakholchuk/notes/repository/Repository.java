package com.pakholchuk.notes.repository;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;

import java.util.ArrayList;

public class Repository implements Contract.RepositoryContract {
    private static Repository instance;
    private Repository() {
    }

    public static Contract.RepositoryContract getInstance() {
        if(instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    @Override
    public Note getNote(int id) {
        return null;
    }

    @Override
    public void deleteNote(int noteId) {

    }

    @Override
    public void addNote(Note note) {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public ArrayList<Note> getAllNotes() {
        return null;
    }

    @Override
    public void applyChangesToNote() {

    }

    @Override
    public void editNote(int noteId, Bundle b) {

    }

    @Override
    public void newNote(Bundle b) {

    }
}
