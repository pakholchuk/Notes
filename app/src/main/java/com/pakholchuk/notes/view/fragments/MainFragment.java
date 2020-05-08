package com.pakholchuk.notes.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pakholchuk.notes.R;
import com.pakholchuk.notes.arch.BaseContract;
import com.pakholchuk.notes.arch.BaseFragment;
import com.pakholchuk.notes.contracts.MainContract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.databinding.FragmentMainBinding;
import com.pakholchuk.notes.helpers.NotesDiffUtilCallback;
import com.pakholchuk.notes.presenter.MainPresenter;
import com.pakholchuk.notes.view.NotesAdapter;

import java.util.ArrayList;

public class MainFragment extends BaseFragment<MainContract.View, MainContract.Presenter>
        implements MainContract.View, NotesAdapter.OnItemClickListener {
    private FragmentMainBinding binding;
    private RecyclerView recyclerView;
    private NotesAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(v -> presenter.onNewNoteClicked());
        initRecycler();
        presenter.onViewCreated();
        navController = Navigation.findNavController(binding.getRoot());
    }


    private void initRecycler() {
        recyclerView = binding.recyclerMain;
        recyclerAdapter = new NotesAdapter(new NotesDiffUtilCallback());
        recyclerAdapter.setItemClickListener(this);
        layoutManager = new GridLayoutManager(getContext(), 2,
                RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    public void updateList(ArrayList<Note> notes) {
        Log.d("FATAL_TAG", "updateList: " + notes.size());
        recyclerAdapter.submitList(notes);
    }

    @Override
    public void showNote(Bundle bundle) {
        navController.navigate(R.id.detailsFragment, bundle);
    }

    @Override
    public void showEditNote(Bundle bundle) {
        navController.navigate(R.id.action_mainFragment_to_editNoteFragment, bundle);
    }

    @Override
    public void onItemClick(long noteId) {
        presenter.onItemClicked(noteId);
    }

    @Override
    protected MainContract.Presenter initPresenter() {
        return new MainPresenter();
    }
    @Override
    public void onDestroy() {
        recyclerView.setAdapter(null);
        super.onDestroy();
    }
}
