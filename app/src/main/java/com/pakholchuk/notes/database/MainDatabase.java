package com.pakholchuk.notes.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pakholchuk.notes.data.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
