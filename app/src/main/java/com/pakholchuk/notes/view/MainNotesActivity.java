package com.pakholchuk.notes.view;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pakholchuk.notes.R;
import com.pakholchuk.notes.arch.BaseActivity;
import com.pakholchuk.notes.contracts.MainContract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.databinding.ActivityMainNotesBinding;
import com.pakholchuk.notes.presenter.MainPresenter;
import com.pakholchuk.notes.view.fragments.DeleteAllDialogFragment;

import java.io.File;
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
public class MainNotesActivity extends BaseActivity<MainContract.View, MainContract.Presenter>
        implements MainContract.View,
        NotesAdapter.OnItemClickListener,
        DeleteAllDialogFragment.DialogListener {

    private ActivityMainNotesBinding activityBinding;
    private RecyclerView recyclerView;
    private NotesAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DialogFragment dialog;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMainNotesBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        init();
        //presenter.onViewCreated();
    }

    @Override
    protected MainContract.Presenter initPresenter() {
        return new MainPresenter();
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
//        activityBinding.fab.setOnClickListener(v -> presenter.onNewNoteClicked());
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        initRecycler();
    }

//    private void initRecycler() {
//        recyclerView = activityBinding.contentMain.recyclerMain;
//        recyclerAdapter = new NotesAdapter(this);
//        layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, true);
//        recyclerView.setAdapter(recyclerAdapter);
//        recyclerView.setLayoutManager(layoutManager);
//    }

    @Override
    public void updateList(ArrayList<Note> notes) {
        recyclerAdapter.submitList(notes);
    }

    @Override
    public void showNote(Bundle bundle) {
        navController.navigate(R.id.detailsFragment, bundle);
    }

    @Override
    public void showEditNote(Bundle bundle) {
        navController.navigate(R.id.editNoteFragment, bundle);
    }

    @Override
    public void onItemClick(long noteId) {
        presenter.onItemClicked(noteId);
    }

    private void showOptionsDialog() {
        dialog = new DeleteAllDialogFragment();
        dialog.show(getSupportFragmentManager(), "DeleteAllDialogFragment");
    }

    @Override
    public void onDialogPositiveClick() {
        presenter.onClearListClicked(getImagesDirPath());
    }

    private String getImagesDirPath() {
        return (this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString();
    }

    @Override
    public void onDialogNegativeClick() {
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
//        recyclerView.setAdapter(null);
        super.onDestroy();
    }
}
