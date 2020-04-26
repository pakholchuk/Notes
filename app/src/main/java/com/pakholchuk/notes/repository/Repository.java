package com.pakholchuk.notes.repository;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.App;
import com.pakholchuk.notes.database.MainDatabase;
import com.pakholchuk.notes.database.NoteDao;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class Repository implements Contract.RepositoryContract {

    private Contract.PresenterContract presenter;
    private MainDatabase database;
    private NoteDao noteDao;
    private Note note;

    public Repository(Contract.PresenterContract presenter) {
        database = App.getInstance().getDatabase();
        noteDao = database.noteDao();
        this.presenter = presenter;
    }

    @Override
    public void clearAll() {
        noteDao.deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void loadAllNotes() {
        noteDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presenter::noteListReady);
    }

    @Override
    public void loadNote(long id) {
        noteDao.getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(note -> presenter.noteLoaded(note));
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> presenter.noteInserted(note));
    }

    @Override
    public void update(Note n, Bundle b) {
        n.setName(b.getString(NoteConstants.NAME));
        n.setBody(b.getString(NoteConstants.BODY));
        n.setImgPath(b.getString(NoteConstants.IMAGE));
        n.setLastEditDate("last edit: " + getSimpleDate());
        note = n;
        noteDao.update(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> presenter.noteUpdated(note));
    }

    @Override
    public void delete(Note n) {
        noteDao.delete(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private String getSimpleDate (){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
