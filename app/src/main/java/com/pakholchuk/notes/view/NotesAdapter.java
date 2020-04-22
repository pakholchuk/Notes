package com.pakholchuk.notes.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pakholchuk.notes.databinding.NoteItemBinding;
import com.pakholchuk.notes.data.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private NoteItemBinding binding;
    private ArrayList<Note> notes = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public NotesAdapter(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    void updateNotesList(ArrayList<Note> list){
        notes.clear();
        notes = list;
        notifyDataSetChanged();
    }
    void addNewNote(Note note){
        notes.add(note);
        notifyItemInserted(notes.size());
    }
    void clearAll() {
        notes.clear();
        notifyDataSetChanged();
    }
    void editNote(Note note, int position) {
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
        String dateText = "Last changed: " + notes.get(position).getLastEditDate();
        holder.binding.tvItemDate.setText(dateText);
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
            this.binding=binding;
            view.setOnClickListener(this);
            onItemClickListener = listener;
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
