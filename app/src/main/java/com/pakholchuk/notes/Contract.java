package com.pakholchuk.notes;

import android.view.View;

import com.pakholchuk.notes.repository.Note;

import java.util.ArrayList;

public interface Contract {
    interface PresenterContract {
        void add();
        void edit();
        void delete();
        void clearList();
        void detachView();
        void itemClicked(View view, int position);
        void imagePressed(View view);
    }

    interface ViewContract {
        void showList(ArrayList<Note> notes);
        void resetList();
        void showProgress();
        void hideProgress();
        void showChangeNoteFragment();
        void showNote();
        void closeNote();

    }

    interface RepositoryContract {
        Note getNote();
        void deleteNote();
        void addNote();
        void clearAll();
        ArrayList<Note> getAllNotes();
        void applyChangesToNote();

    }
}
