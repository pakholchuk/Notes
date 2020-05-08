package com.pakholchuk.notes.contracts;

import android.os.Bundle;

import com.pakholchuk.notes.data.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface ContractRepository {
    Flowable<List<Note>> getAll();

    Observable<Note> loadNote(long id);

    void insert(Bundle b);

    void update(Note note, Bundle b);

    void delete(Note note);

    Completable clearAll();
}
