package com.pakholchuk.notes.contracts;

import android.os.Bundle;

import com.pakholchuk.notes.arch.BaseContract;
import com.pakholchuk.notes.data.Note;
import java.util.ArrayList;

public interface MainContract {
    interface View extends BaseContract.View {
        void updateList(ArrayList<Note> notes);
        void showNote(Bundle bundle);
        void showEditNote(Bundle bundle);
    }
    interface Presenter extends BaseContract.Presenter<MainContract.View>{
        void onViewCreated();
        void onNewNoteClicked();
        void onClearListClicked(String imagesDir);
        void onItemClicked(long noteId);
    }
}
