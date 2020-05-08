package com.pakholchuk.notes.repository;

import android.os.Bundle;
import android.util.Log;

import com.pakholchuk.notes.App;
import com.pakholchuk.notes.contracts.ContractRepository;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.repository.database.NoteDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;


public class Repository implements ContractRepository {

    private NoteDao noteDao;
    private Note note;

    public Repository() {
        noteDao = App.getInstance().getDatabase().noteDao();
    }



    @Override
    public Flowable<List<Note>> getAll() {
        return noteDao.getAll();
    }

    @Override
    public Observable<Note> loadNote(long id) {
        return noteDao.getById(id);
    }

    @Override
    public void insert(Bundle b) {
        String creation = "created: " + getSimpleDate();
        String edit = "last edit: " + getSimpleDate();
        long id = System.currentTimeMillis();
        note = new Note(id, b.getString(NoteConstants.NAME),
                b.getString(NoteConstants.BODY),
                b.getString(NoteConstants.IMAGE),
                creation, edit);
        noteDao.insert(note)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("FATAL_TAG", "insert: " + note.getId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void update(Note n, Bundle b) {
        n.setName(b.getString(NoteConstants.NAME));
        n.setBody(b.getString(NoteConstants.BODY));
        n.setImgPath(b.getString(NoteConstants.IMAGE));
        n.setLastEditDate("last edit: " + getSimpleDate());
        note = n;
        noteDao.update(n)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void delete(Note n) {

        noteDao.delete(n)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public Completable clearAll() {
        return noteDao.deleteAll();
    }

    private String getSimpleDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

}
