package com.pakholchuk.notes.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.databinding.NoteItemBinding;
import com.pakholchuk.notes.helpers.NotesDiffUtilCallback;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private NoteItemBinding binding;
    private ArrayList<Note> notes = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private Disposable disposable;

    public NotesAdapter(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    void updateNotesList(ArrayList<Note> newList) {
        NotesDiffUtilCallback notesDiffUtilCallback =
                new NotesDiffUtilCallback(notes, newList);
        notes = newList;
        disposable = Single.create((SingleOnSubscribe<DiffUtil.DiffResult>)
                emitter -> emitter.onSuccess(DiffUtil
                        .calculateDiff(notesDiffUtilCallback)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> result.dispatchUpdatesTo(NotesAdapter.this));
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = NoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.binding.tvItemName.setText(notes.get(position).getName());
        String dateText = notes.get(position).getLastEditDate();
        holder.binding.tvItemDate.setText(dateText);
        if (!notes.get(position).getImgPath().equals("")) {
            holder.binding.ivAttachment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener onItemClickListener;
        private NoteItemBinding binding;

        NoteViewHolder(@NonNull NoteItemBinding binding, OnItemClickListener listener) {
            super(binding.getRoot());
            View view = binding.getRoot();
            this.binding = binding;
            view.setOnClickListener(this);
            onItemClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            long noteId = notes.get(position).getId();
            onItemClickListener.onItemClick(position, noteId);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, long noteId);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        disposable.dispose();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
