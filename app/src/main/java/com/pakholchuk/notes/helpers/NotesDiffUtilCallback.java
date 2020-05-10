package com.pakholchuk.notes.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.pakholchuk.notes.data.Note;

public class NotesDiffUtilCallback extends DiffUtil.ItemCallback<Note> {

    @Override
    public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Note oldNote, @NonNull Note newNote) {
        return (oldNote.getName().equals(newNote.getName()))
                && (oldNote.getBody().equals(newNote.getBody()))
                && (oldNote.getImgPath().equals(newNote.getImgPath()))
                && (oldNote.getCreationDate().equals(newNote.getCreationDate()))
                && (oldNote.getLastEditDate().equals(newNote.getLastEditDate()));
    }


}
