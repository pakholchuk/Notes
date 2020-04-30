package com.pakholchuk.notes.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.databinding.NoteItemBinding;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private NoteItemBinding binding;
    private ArrayList<Note> notes = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public NotesAdapter(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    void updateNotesList(ArrayList<Note> list) {
        notes.clear();
        notes = list;
        notifyDataSetChanged();
    }

    void addNewNote(Note note) {
        notes.add(note);
        notifyItemInserted(notes.size());
    }

    void clearAll() {
        notes.clear();
        notifyDataSetChanged();
    }

    void editNote(int position, Note note) {
        notes.set(position, note);
        notifyItemChanged(position);
    }

    void deleteNote(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
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

}
