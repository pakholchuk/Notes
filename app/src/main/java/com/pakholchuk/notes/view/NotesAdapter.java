package com.pakholchuk.notes.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.databinding.NoteItemBinding;

public class NotesAdapter extends ListAdapter<Note, NotesAdapter.NoteViewHolder> {
    private NoteItemBinding binding;
    private OnItemClickListener itemClickListener;

    public NotesAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback) {
        super(diffCallback);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = NoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.binding.tvItemName.setText(getItem(position).getName());
        String dateText = getItem(position).getLastEditDate();
        holder.binding.tvItemDate.setText(dateText);
        if (!getItem(position).getImgPath().equals("")) {
            holder.binding.ivAttachment.setVisibility(View.VISIBLE);
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            long noteId = getItem(getAdapterPosition()).getId();
            onItemClickListener.onItemClick(noteId);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long noteId);
    }

}
