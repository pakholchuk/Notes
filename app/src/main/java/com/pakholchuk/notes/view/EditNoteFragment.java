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
import com.pakholchuk.notes.data.NoteFields;
import com.pakholchuk.notes.databinding.FragmentEditNoteBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EditNoteFragment extends Fragment {
    public static final String TAG_ADD = "add";
    public static final String TAG_EDIT = "edit";
    private static final int LOAD_PIC_REQUEST_CODE = 22;
    private OnFragmentEventListener onFragmentEventListener;
    private FragmentEditNoteBinding binding;
    private Context context;
    private String savedImagePath = "";
    private String defaultStringValue = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false);
        onFragmentEventListener = (OnFragmentEventListener) getActivity();
        context = getContext();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentEventListener.onFragmentButtonClick(v);
            }
        };
        binding.btnSave.setOnClickListener(onClickListener);
        binding.btnSaveNew.setOnClickListener(onClickListener);
        binding.btnClose.setOnClickListener(onClickListener);
        binding.btnDelete.setOnClickListener(onClickListener);
        binding.ivNewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    private void onAddFragmentCreate() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.btnSave.setVisibility(View.GONE);
        binding.btnSaveNew.setVisibility(View.VISIBLE);
        binding.etFragmentBody.setText("");
        binding.etFragmentName.setText("");
        binding.etFragmentBody.setHint("Enter note");
        binding.etFragmentName.setHint("Enter name");
    }

    private void onEditFragmentCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.etFragmentName.setText(bundle.getString(NoteFields.NAME, defaultStringValue));
            binding.etFragmentBody.setText(bundle.getString(NoteFields.BODY, defaultStringValue));
            savedImagePath = bundle.getString(NoteFields.IMAGE, defaultStringValue);
        }
        binding.btnSave.setVisibility(View.VISIBLE);
        binding.btnSaveNew.setVisibility(View.GONE);

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
        saveImage(uriFrom);
    }

    private void saveImage(Uri uriFrom) {
            Picasso.get().load(uriFrom).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future<String> future = executorService.submit(new Callable<String>() {
                        @Override
                        public String call() {
                            String path = (context
                                    .getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
                            String name = System.currentTimeMillis() + ".jpg";
                            File file = new File(path, name);
                            try {
                                OutputStream stream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                                stream.flush();
                                stream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            savedImagePath = file.getPath();
                            return savedImagePath;
                        }

                    });
                    while (!future.isDone()){
                        onImageSaved();
                    }
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {}
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {}
            });
    }

    private void onImageSaved() {
        binding.progress.setVisibility(View.GONE);
        binding.btnSave.setEnabled(true);
        binding.ivNewPicture.setImageResource(R.drawable.ic_image_black_24dp);
        binding.ivIsDone.setVisibility(View.VISIBLE);
        binding.ivNewPicture.setVisibility(View.VISIBLE);
    }

    public Bundle getData(){
        Bundle bundle = new Bundle();
        bundle.putString(NoteFields.NAME, binding.etFragmentName.getText().toString());
        bundle.putString(NoteFields.BODY, binding.etFragmentBody.getText().toString());
        bundle.putString(NoteFields.IMAGE, savedImagePath);
        return bundle;
    }

}
