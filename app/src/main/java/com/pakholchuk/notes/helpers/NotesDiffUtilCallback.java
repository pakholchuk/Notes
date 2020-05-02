package com.pakholchuk.notes.helpers;

import androidx.recyclerview.widget.DiffUtil;

import com.pakholchuk.notes.data.Note;

import java.util.List;

public class NotesDiffUtilCallback extends DiffUtil.Callback {
    private final List<Note> oldList;
    private final List<Note> newList;

    public NotesDiffUtilCallback(List<Note> oldList, List<Note> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);
        return (oldNote.getId() == newNote.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);
        return (oldNote.getName().equals(newNote.getName()))
                && (oldNote.getBody().equals(newNote.getBody()))
                && (oldNote.getImgPath().equals(newNote.getImgPath()))
                && (oldNote.getCreationDate().equals(newNote.getCreationDate()))
                && (oldNote.getLastEditDate().equals(newNote.getLastEditDate()));
    }
}
