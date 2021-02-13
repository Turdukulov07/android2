package com.geektech.noteapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST_FOR_ICON = 2;
    private ImageView imageViewSetPhoto, imageViewIcon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        imageViewSetPhoto = view.findViewById(R.id.image_wallpaper);
        imageViewIcon = view.findViewById(R.id.image_view_icon);

        imageViewIcon.setOnClickListener(v -> pickImageIcon());

        imageViewSetPhoto.setOnClickListener(v -> pickImage());
        return view;

    }

    private void pickImageIcon() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_FOR_ICON);
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            Uri imageUri = data.getData();
            imageViewSetPhoto.setImageURI(imageUri);
        }
        if (requestCode == PICK_IMAGE_REQUEST_FOR_ICON && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            Uri uri = data.getData();
            imageViewIcon.setImageURI(uri);
        }
    }
}