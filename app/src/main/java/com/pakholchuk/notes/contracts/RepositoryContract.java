package com.pakholchuk.notes.contracts;

import com.pakholchuk.notes.repository.Note;

import java.util.ArrayList;

public interface RepositoryContract {
    Note getNote();
    void deleteNote();
    void addNote();
    void clearAll();
    ArrayList<Note> getAllNotes();
    void applyChangesToNote();

}
