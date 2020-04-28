package com.pakholchuk.notes;

import android.os.Bundle;

import com.pakholchuk.notes.data.Note;

import java.util.ArrayList;
import java.util.List;

public interface Contract {
    interface PresenterContract {
        void add();
        void edit();
        void delete();
        void clearList();
        void onActivityDestroyed();
        void itemClicked(int position, long noteId);
        void imagePressed();
        void newNotePressed();
        void save();
        void viewReady();

        void noteListReady(List<Note> noteList);

        void noteLoaded(Note note);

        void noteInserted(Note note);

        void noteUpdated(Note note);
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
        void loadNote(long id);
        void clearAll();
        void loadAllNotes();
        void insert(Bundle b);
        void update(Note note, Bundle b);
        void delete(Note noteId);
        void disposeAll();
    }
}
