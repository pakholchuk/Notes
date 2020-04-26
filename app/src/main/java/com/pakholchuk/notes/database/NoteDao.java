package com.pakholchuk.notes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pakholchuk.notes.data.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    Observable<List<Note>> getAll();

    @Query("SELECT * FROM note WHERE id = :id")
    Observable<Note> getById(long id);

    @Query("DELETE FROM note")
    Completable deleteAll();

    @Insert
    Completable insert(Note note);

    @Update
    Completable update(Note note);

    @Delete
    Completable delete(Note note);


}
