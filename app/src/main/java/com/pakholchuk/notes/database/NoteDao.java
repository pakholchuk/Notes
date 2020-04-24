package com.pakholchuk.notes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pakholchuk.notes.data.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("SELECT * FROM note WHERE id = :id")
    Note getById(long id);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);


}
