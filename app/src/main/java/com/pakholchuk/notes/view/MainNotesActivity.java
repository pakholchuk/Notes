package com.pakholchuk.notes.view;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pakholchuk.notes.contracts.PresenterContract;
import com.pakholchuk.notes.contracts.ViewContract;
import com.pakholchuk.notes.databinding.ActivityMainNotesBinding;
import com.pakholchuk.notes.databinding.ContentMainNotesBinding;
import com.pakholchuk.notes.presenter.Presenter;
import com.pakholchuk.notes.repository.Note;
import com.pakholchuk.notes.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

/*
Приложение “Заметки”.
Требования:
*Просмотр списка добавленных заметок;
*Детальный просмотр заметки (с возможностью открыть редактирование заметки, либо удалить);
*Создание заметки (форма с вводом названия, текста заметки и выбором изображения для прикрепления);
*Настройки с возможностью удаления всех заметок;
*Адекватный вид в портретной и ландшафтной ориентации для телефонов и планшетов;
*Поддержка с Android API 15;
*Использовать git или mercurial.

Дополнительным плюсом будет реализация следующего функционала:
*Прикрепление нескольких изображений к заметке;
*Применение архитектуры MVP/MVVM;
*Unit-тесты (хотя бы несколько).

Данные сущности “заметка”:
*Название;
*Текст;
*Изображение (либо несколько);
*Дата создания;
*Дата изменения.
 */
public class MainNotesActivity extends AppCompatActivity implements ViewContract, NotesAdapter.OnItemClickListener {

    private ActivityMainNotesBinding activityBinding;
    private PresenterContract presenter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMainNotesBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        init();
    }

    private void init() {
        setSupportActionBar(activityBinding.toolbar);

        activityBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        presenter = new Presenter(this);
        initRecycler();
    }

    private void initRecycler() {
        recyclerView = activityBinding.contentMain.recyclerMain;
        recyclerAdapter = new NotesAdapter(this);
        layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            inflateSettingsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inflateSettingsDialog() {
    }

    @Override
    public void showList(ArrayList<Note> notes) {

    }

    @Override
    public void resetList() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showChangeNoteFragment() {

    }

    @Override
    public void showNote() {

    }

    @Override
    public void closeNote() {

    }

    @Override
    public void onItemClick(View view, int position) {
        presenter.itemClicked(view, position);
    }
}
