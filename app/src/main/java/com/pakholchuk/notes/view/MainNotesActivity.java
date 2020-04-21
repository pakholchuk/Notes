package com.pakholchuk.notes.view;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.databinding.ActivityMainNotesBinding;
import com.pakholchuk.notes.presenter.Presenter;
import com.pakholchuk.notes.repository.Note;
import com.pakholchuk.notes.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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
public class MainNotesActivity extends AppCompatActivity implements Contract.ViewContract,
        NotesAdapter.OnItemClickListener, OnFragmentButtonClickListener,
        DeleteAllDialogFragment.DialogListener {

    private ActivityMainNotesBinding activityBinding;
    private Contract.PresenterContract presenter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Fragment fragment;
    private DialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMainNotesBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void init() {
        setSupportActionBar(activityBinding.toolbar);

        activityBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();

            }
        });
        presenter = new Presenter(this);
        initRecycler();
    }

    private void add() {
        fragment = new EditNoteFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, fragment, EditNoteFragment.TAG_ADD)
                .addToBackStack(EditNoteFragment.TAG_ADD)
                .commit();
    }

    private void initRecycler() {
        recyclerView = activityBinding.contentMain.recyclerMain;
        recyclerAdapter = new NotesAdapter(this);
        layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            inflatePreferenceFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inflatePreferenceFragment() {
        dialog = new DeleteAllDialogFragment();
        dialog.show(getSupportFragmentManager(), "DeleteAllDialogFragment");
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

    @Override
    public void onFragmentButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save : {
                presenter.add();
                break;
            }
            case R.id.btn_close : {
                onBackPressed();
                break;
            }
            case R.id.btn_delete : {
                presenter.delete();
                break;
            }
            case R.id.btn_edit : {
                presenter.edit();
                break;
            }
            case (R.id.iv_new_picture|R.id.iv_show_picture) : {
                presenter.imagePressed(view);
                break;
            }
            default:break;
        }
    }

    @Override
    public void onDialogPositiveClick() {
        presenter.clearList();
    }

    @Override
    public void onDialogNegativeClick() {
        dialog.dismiss();
    }
}
