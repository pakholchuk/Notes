package com.pakholchuk.notes;

import android.os.Bundle;

import com.pakholchuk.notes.data.Note;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface Contract {
    interface PresenterContract {
        void add();

        void edit();

        void save();

        void delete();

        void clearList();

        void itemClicked(int position, long noteId);

        void imagePressed();
        void cachedImagePressed();

        void newNotePressed();

        void viewReady();

        void onActivityDestroyed();
    }

    interface ViewContract {
        void showList(ArrayList<Note> notes);

        void showProgress();

        void hideProgress();

        void showNote(Bundle bundle);

        void closeNote();

        Bundle getDataFromUser();

        void addItem(Object object);

        void showEditNote(String tag, Bundle bundle);

        void editItem(int position, Note note);

        void removeItem(int position);

        void clearAll();

        String getCachedImagePath();
        void showImage(String imgPath);
    }

    interface RepositoryContract {
        Observable<List<Note>> loadAllNotes();

        Observable<Note> loadNote(long id);

        Completable insert(Bundle b);

        Completable update(Note note, Bundle b);

        void delete(Note noteId);

        Completable clearAll();
    }
}
