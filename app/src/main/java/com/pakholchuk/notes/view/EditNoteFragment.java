package com.pakholchuk.notes.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pakholchuk.notes.R;
import com.pakholchuk.notes.data.NoteConstants;
import com.pakholchuk.notes.databinding.FragmentEditNoteBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EditNoteFragment extends Fragment {
    public static final String TAG_ADD = "add";
    public static final String TAG_EDIT = "edit";
    private static final int LOAD_PIC_REQUEST_CODE = 22;
    private OnFragmentEventListener onFragmentEventListener;
    private FragmentEditNoteBinding binding;
    private Context context;
    private String savedImagePath = "";
    private String defaultStringValue = "";
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false);
        onFragmentEventListener = (OnFragmentEventListener) getActivity();
        context = getContext();
        setListeners();
        String tag = "" + getTag();
        switch (tag) {
            case TAG_ADD : {
                onAddFragmentCreate();
                break;
            }
            case TAG_EDIT : {
                onEditFragmentCreate();
                break;
            }
        }
        binding.getRoot().setOnClickListener(v -> {});
        return binding.getRoot();
    }

    private void setListeners() {
        View.OnClickListener onClickListener = v -> onFragmentEventListener.onFragmentButtonClick(v);
        binding.btnSave.setOnClickListener(onClickListener);
        binding.btnSaveNew.setOnClickListener(onClickListener);
        binding.btnClose.setOnClickListener(onClickListener);
        binding.btnDelete.setOnClickListener(onClickListener);
        binding.ivNewPicture.setOnClickListener(v -> pickImage());
    }

     private void onAddFragmentCreate() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.btnSave.setVisibility(View.GONE);
        binding.btnSave.setEnabled(false);
        binding.btnSaveNew.setEnabled(true);
        binding.btnSaveNew.setVisibility(View.VISIBLE);
        binding.etFragmentBody.setText("");
        binding.etFragmentName.setText("");
        binding.etFragmentBody.setHint("Enter note");
        binding.etFragmentName.setHint("Enter name");
    }

    private void onEditFragmentCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.etFragmentName.setText(bundle.getString(NoteConstants.NAME, defaultStringValue));
            binding.etFragmentBody.setText(bundle.getString(NoteConstants.BODY, defaultStringValue));
            savedImagePath = bundle.getString(NoteConstants.IMAGE, defaultStringValue);
        }
        binding.btnSave.setVisibility(View.VISIBLE);
        binding.btnSave.setEnabled(true);
        binding.btnSaveNew.setVisibility(View.GONE);
        binding.btnSaveNew.setEnabled(false);

    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent, LOAD_PIC_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LOAD_PIC_REQUEST_CODE : {
                    Uri uri = data.getData();
                    onImagePicked(uri);
                    break;
                }
            }
        }
    }

    private void onImagePicked(Uri uriFrom) {
        binding.btnSave.setEnabled(false);
        binding.ivNewPicture.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);

        disposables.add(saveImage(uriFrom)
                .flatMap((Function<Bitmap, Single<String>>) bitmap -> Single
                        .create(emitter -> emitter.onSuccess(saveImageInApp(bitmap))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onImageSaved));
    }

    private Single<Bitmap> saveImage(Uri uriFrom) {
        return Single.create(emitter -> {
          emitter.onSuccess(Picasso.get()
                  .load(uriFrom)
                  .get());
        });
    }

    private String saveImageInApp(Bitmap bitmap) throws IOException {
        String path = (context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        String name = System.currentTimeMillis() + ".jpg";
        File file = new File(path, name);
        OutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        stream.flush();
        stream.close();
        return file.getPath();
    }

    private void onImageSaved(String s) {
        savedImagePath = s;
        binding.progress.setVisibility(View.GONE);
        binding.btnSave.setEnabled(true);
        binding.ivNewPicture.setImageResource(R.drawable.ic_image_black_24dp);
        binding.ivIsDone.setVisibility(View.VISIBLE);
        binding.ivNewPicture.setVisibility(View.VISIBLE);
    }

    public Bundle getData(){
        Bundle bundle = new Bundle();
        bundle.putString(NoteConstants.NAME, binding.etFragmentName.getText().toString());
        bundle.putString(NoteConstants.BODY, binding.etFragmentBody.getText().toString());
        bundle.putString(NoteConstants.IMAGE, savedImagePath);
        return bundle;
    }

    @Override
    public void onDestroyView() {
        disposables.dispose();
        super.onDestroyView();
    }
}
