package com.example.vincent_deluca_project_03;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vincent_deluca_project_03.databinding.FragmentAddBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private final ActivityResultLauncher<String[]> stringResult;
    private final ActivityResultLauncher<Intent> intentResult;
    private Uri imageUri = null;
    private final String DEFAULT_POSTER = "default_poster.jpg";

    public AddFragment() {
        stringResult = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    boolean areAllGranted = true;
                    for (boolean b : result.values()) {
                        areAllGranted = areAllGranted && b;
                    }
                    if (areAllGranted)
                        takePhoto();
                });
        intentResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                        Picasso.get().load(imageUri).into(binding.addPoster);
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        StorageReference pathReference = FirebaseStorage.getInstance().getReference(DEFAULT_POSTER);
        pathReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(binding.addPoster));
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        binding.addPoster.setOnClickListener(v -> this.stringResult.launch(permissions));

        binding.addRating.setMinValue(0);
        binding.addRating.setMaxValue(10);

        binding.submit.setOnClickListener(v -> submit());

        return view;
    }

    private void takePhoto() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        ContentResolver resolver = getActivity().getContentResolver();
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        this.intentResult.launch(intent);
    }

    private void submit() {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final String fileNameInStorage = UUID.randomUUID().toString();
            String path = fileNameInStorage + ".jpg";
            final StorageReference imageRef = storage.getReference(path);
            final StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            imageRef.putFile(imageUri, metadata).addOnSuccessListener(taskSnapshot ->
                    submitMovieModel(fileNameInStorage + ".jpg")
            ).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            submitMovieModel(DEFAULT_POSTER);
        }
    }

    private void submitMovieModel(String url) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference moviesRef = database.getReference("Movies");
        final DatabaseReference newMovieRef = moviesRef.push();
        String description = binding.addDescription.getText().toString();
        String director = binding.addDirector.getText().toString();
        String length = binding.addLength.getText().toString();
        float rating = (float) binding.addRating.getValue();
        String stars = binding.addStars.getText().toString();
        String title = binding.addTitle.getText().toString();
        String year = binding.addYear.getText().toString();
        newMovieRef.setValue(new MovieModel(
                description,
                director,
                length,
                rating,
                stars,
                title,
                url,
                year))
                .addOnSuccessListener(v ->
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show());
    }
}
