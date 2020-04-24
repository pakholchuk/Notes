package com.pakholchuk.notes.repository;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteFields;
import com.pakholchuk.notes.App;
import com.pakholchuk.notes.database.MainDatabase;
import com.pakholchuk.notes.database.NoteDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository implements Contract.RepositoryContract {

    private MainDatabase database;
    private NoteDao noteDao;
    private ArrayList<Note> notes = new ArrayList<>();
    private Note note;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Repository() {
        database = App.getInstance().getDatabase();
        noteDao = database.noteDao();
    }

    @Override
    public void clearAll() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public ArrayList<Note> getAllNotes() {
        notes.clear();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                notes = (ArrayList<Note>)noteDao.getAll();
            }
        });
        return notes;
    }

    @Override
    public Note getNote(final long id) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                note = noteDao.getById(id);
            }
        });
        return note;
    }

    @Override
    public Note insert(Bundle b) {
        String creation = "created: " + getSimpleDate();
        String edit = "last edit: " + getSimpleDate();
        long id = System.currentTimeMillis();
        note = new Note(id, b.getString(NoteFields.NAME),
                b.getString(NoteFields.BODY),
                b.getString(NoteFields.IMAGE),
                creation, edit);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        });
        return note;
    }

    @Override
    public void update(Note n, Bundle b) {
        n.setName(b.getString(NoteFields.NAME));
        n.setBody(b.getString(NoteFields.BODY));
        n.setImgPath(b.getString(NoteFields.IMAGE));
        n.setLastEditDate("last edit: " + getSimpleDate());
        note = n;
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });
    }

    @Override
    public void delete(final Note n) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(n);
            }
        });
    }

    private String getSimpleDate (){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        String result = sdf.format(new Date(System.currentTimeMillis()));
        return result;
    }
}
