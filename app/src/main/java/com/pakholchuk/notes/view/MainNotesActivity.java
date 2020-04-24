package com.pakholchuk.notes.view;

import android.os.Bundle;

import com.pakholchuk.notes.Contract;
import com.pakholchuk.notes.data.NoteFields;
import com.pakholchuk.notes.databinding.ActivityMainNotesBinding;
import com.pakholchuk.notes.presenter.Presenter;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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
        NotesAdapter.OnItemClickListener, OnFragmentEventListener,
        DeleteAllDialogFragment.DialogListener {

    private ActivityMainNotesBinding activityBinding;
    private Contract.PresenterContract presenter;
    private RecyclerView recyclerView;
    private NotesAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditNoteFragment editNoteFragment;
    private DialogFragment dialog;
    private NoteFragment noteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMainNotesBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        init();
        presenter.viewReady();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            showOptionsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        setSupportActionBar(activityBinding.toolbar);
        presenter = new Presenter(this);
        activityBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.newNotePressed();
            }
        });
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
    public void showList(ArrayList<Note> notes) {
        recyclerAdapter.updateNotesList(notes);
    }

    @Override
    public void clearAll() {
        recyclerAdapter.clearAll();
    }

    @Override
    public void addItem(Object object) {
        recyclerAdapter.addNewNote((Note)object);
    }

    @Override
    public void showNote(Bundle bundle) {
        noteFragment = new NoteFragment();
        noteFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(NoteFragment.TAG_SHOW)
                .add(noteFragment, NoteFragment.TAG_SHOW)
                .commit();
    }

    @Override
    public void editItem(int position, Note note) {
        recyclerAdapter.editNote(position, note);
    }

    @Override
    public void showEditFragment(String tag, Bundle bundle) {
        editNoteFragment = new EditNoteFragment();
        editNoteFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, editNoteFragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public Bundle getDataFromUser(){
        return editNoteFragment.getData();
    }

    @Override
    public void showImageFragment(String imgPath) {
        ImageFragment imageFragment = new ImageFragment();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(NoteFields.IMAGE)
                .add(imageFragment, NoteFields.IMAGE)
                .commit();
    }

    @Override
    public void closeNote() {
        if(noteFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(noteFragment).commit();
        }
        if(editNoteFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(editNoteFragment).commit();
        }
    }

    @Override
    public void removeItem(int position) {
        recyclerAdapter.deleteNote(position);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onItemClick(int position, long noteId) {
        presenter.itemClicked(position, noteId);
    }

    @Override
    public void onFragmentButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_new: {
                presenter.add();
                break;
            }
            case R.id.btn_save: {
                presenter.save();
            }
            case R.id.btn_close : {
                closeNote();
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
            case (R.id.iv_show_picture) : {
                presenter.imagePressed();
                break;
            }
            default:break;
        }
    }

    private void showOptionsDialog() {
        dialog = new DeleteAllDialogFragment();
        dialog.show(getSupportFragmentManager(), "DeleteAllDialogFragment");
    }

    @Override
    public void onDialogPositiveClick() {
        presenter.clearList();
    }

    @Override
    public void onDialogNegativeClick() {
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
