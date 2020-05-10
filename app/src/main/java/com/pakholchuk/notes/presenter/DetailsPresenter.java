package com.pakholchuk.notes.presenter;

import android.os.Bundle;

import com.pakholchuk.notes.arch.BasePresenter;
import com.pakholchuk.notes.contracts.ContractRepository;
import com.pakholchuk.notes.contracts.DetailsContract;
import com.pakholchuk.notes.data.Note;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.helpers.ImageHelper;
import com.pakholchuk.notes.view.fragments.EditFragment;

public class DetailsPresenter extends BasePresenter<DetailsContract.View>
        implements DetailsContract.Presenter {

    private ContractRepository repository = new com.pakholchuk.notes.repository.Repository();
    private ImageHelper imageHelper = new ImageHelper();
    private Note note;
    private Bundle bundleContainsNote;

    @Override
    public void onViewCreated(Bundle bundle) {
        bundleContainsNote = bundle;
        note = new Note(bundle.getLong(NoteConstants.ID),
                bundle.getString(NoteConstants.NAME),
                bundle.getString(NoteConstants.BODY),
                bundle.getString(NoteConstants.IMAGE),
                bundle.getString(NoteConstants.CREATION),
                bundle.getString(NoteConstants.EDIT));
    }

    @Override
    public void onEditClick() {
        bundleContainsNote.putString("tag", EditFragment.TAG_EDIT);
        getView().showEditNote(bundleContainsNote);
    }

    @Override
    public void onDeleteClick() {
        repository.delete(note);
        imageHelper.deleteImage(note.getImgPath());
        getView().close();
    }

    @Override
    public void onImageClick() {
        getView().showImage(note.getImgPath());
    }

}
