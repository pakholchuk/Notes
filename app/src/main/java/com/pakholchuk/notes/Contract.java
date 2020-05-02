package com.pakholchuk.notes;

import android.os.Bundle;

import com.pakholchuk.notes.data.Note;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface Contract {
    interface PresenterContract {
        void viewReady();
        void newNotePressed();
        void clearList();
        void itemClicked(int position, long noteId);
        void add();
        void edit();
        void save();
        void delete();
        void imagePressed();
        void cachedImagePressed();
        void onActivityDestroyed();
    }

    interface ViewContract {
        void updateList(ArrayList<Note> notes);
        void showNote(Bundle bundle);
        void showEditNote(String tag, Bundle bundle);
        void closeNote();
        Bundle getDataFromUser();
        String getCachedImagePath();
        void showImage(String imgPath);
    }

    interface RepositoryContract {
        Observable<List<Note>> updateList();

        Observable<Note> loadNote(long id);

        void insert(Bundle b);

        void update(Note note, Bundle b);

        void delete(Note noteId);

        Completable clearAll();
    }
}
