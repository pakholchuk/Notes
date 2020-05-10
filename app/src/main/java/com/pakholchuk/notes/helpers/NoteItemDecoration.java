package com.pakholchuk.notes.helpers;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoteItemDecoration extends RecyclerView.ItemDecoration {

    private int offset;

    public NoteItemDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        if (layoutParams.getSpanIndex() % 2 == 0) {
            outRect.top = offset;
            outRect.left = offset;
            outRect.right = offset / 2;
        } else {
            outRect.top = offset;
            outRect.left = offset / 2;
            outRect.right = offset;
        }
    }
}
