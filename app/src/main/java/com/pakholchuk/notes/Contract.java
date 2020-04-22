package com.pakholchuk.notes;

import android.os.Bundle;
import android.view.View;

import com.pakholchuk.notes.data.Note;

import java.util.ArrayList;

public interface Contract {
    interface PresenterContract {
        void add();
        void edit();
        void delete();
        void clearList();
        void detachView();
        void itemClicked( int position);
        void imagePressed();
        void newNotePressed();
        void save();
    }

    interface ViewContract {
        void showList(ArrayList<Note> notes);
        void resetList();
        void showProgress();
        void hideProgress();
        void showChangeNoteFragment();
        void showNote(Bundle bundle);
        void closeNote();

        Bundle getDataFromUser();

        void addItem(Object object);

        void showEditFragment(String tag, Bundle bundle);

        void editItem(int noteId, Note note);

        void removeItem(int noteId);

        void clearAll();

        void showImageFragment(String imgPath);
    }

    interface RepositoryContract {
        Note getNote(int id);
        void deleteNote(int noteId);
        void addNote(Note note);
        void clearAll();
        ArrayList<Note> getAllNotes();
        void applyChangesToNote();
        void editNote(int noteId, Bundle b);

        void newNote(Bundle b);
    }
}
