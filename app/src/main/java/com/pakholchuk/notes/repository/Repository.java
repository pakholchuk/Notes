package com.pakholchuk.notes.repository;

import android.os.Bundle;

import com.pakholchuk.notes.App;
import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.helpers.ImageHelper;
import com.pakholchuk.notes.repository.database.NoteDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;


public class Repository implements Contract.RepositoryContract {

    private NoteDao noteDao;
    private Note note;

    public Repository() {
        noteDao = App.getInstance().getDatabase().noteDao();
    }

    @Override
    public Completable clearAll() {
        return noteDao.deleteAll();
    }



    @Override
    public Observable<List<Note>> loadAllNotes() {
        return noteDao.getAll()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Note> loadNote(long id) {
        return noteDao.getById(id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insert(Bundle b) {
        String creation = "created: " + getSimpleDate();
        String edit = "last edit: " + getSimpleDate();
        long id = System.currentTimeMillis();
        note = new Note(id, b.getString(NoteConstants.NAME),
                b.getString(NoteConstants.BODY),
                b.getString(NoteConstants.IMAGE),
                creation, edit);
        return noteDao.insert(note)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable update(Note n, Bundle b) {
        n.setName(b.getString(NoteConstants.NAME));
        n.setBody(b.getString(NoteConstants.BODY));
        n.setImgPath(b.getString(NoteConstants.IMAGE));
        n.setLastEditDate("last edit: " + getSimpleDate());
        note = n;
        return noteDao.update(n)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void delete(Note n) {
        ImageHelper.deleteImage(n.getImgPath());
        noteDao.delete(n)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private String getSimpleDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

}
