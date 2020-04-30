package com.pakholchuk.notes;

import android.app.Application;

import androidx.room.Room;

import com.pakholchuk.notes.repository.database.MainDatabase;

public class App extends Application {
    private static App instance;
    private MainDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, MainDatabase.class,
                "database").build();
    }

    public static App getInstance() {
        return instance;
    }

    public MainDatabase getDatabase() {
        return database;
    }
}
