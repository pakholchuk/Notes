package com.pakholchuk.notes.contracts;

import com.pakholchuk.notes.repository.Note;

import java.util.ArrayList;

public interface ViewContract {
    void showList(ArrayList<Note> notes);
    void resetList();
    void showProgress();
    void hideProgress();
    void showChangeNoteFragment();
    void showNote();
    void closeNote();

}
