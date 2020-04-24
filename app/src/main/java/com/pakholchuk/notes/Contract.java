package com.pakholchuk.notes;

import android.os.Bundle;

import com.pakholchuk.notes.data.Note;

import java.util.ArrayList;

public interface Contract {
    interface PresenterContract {
        void add();
        void edit();
        void delete();
        void clearList();
        void detachView();
        void itemClicked(int position, long noteId);
        void imagePressed();
        void newNotePressed();
        void save();
        void viewReady();
    }

    interface ViewContract {
        void showList(ArrayList<Note> notes);
        void showProgress();
        void hideProgress();
        void showNote(Bundle bundle);
        void closeNote();
        Bundle getDataFromUser();
        void addItem(Object object);
        void showEditFragment(String tag, Bundle bundle);
        void editItem(int position, Note note);
        void removeItem(int position);
        void clearAll();
        void showImageFragment(String imgPath);
    }

    interface RepositoryContract {
        Note getNote(long id);
        void clearAll();
        ArrayList<Note> getAllNotes();
        Note insert(Bundle b);
        void update(Note note, Bundle b);
        void delete(Note noteId);
    }
}
