package com.example.vincent_deluca_project_03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vincent_deluca_project_03.databinding.FragmentDetailsBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        if(args != null){
            MovieModel movieModel = (MovieModel) args.getSerializable("movie");
            StorageReference pathReference = FirebaseStorage.getInstance().getReference(movieModel.url);
            pathReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(binding.moviePoster));
            binding.movieTitle.setText(movieModel.title);
            binding.movieYear.setText(movieModel.year);
            binding.movieLength.setText(String.format("Length: %s", movieModel.length));
            binding.movieStars.setText(String.format("Starring: %s", movieModel.stars));
            binding.movieDirector.setText(String.format("Directed By: %s", movieModel.director));
            binding.movieRating.setRating(movieModel.rating);
            binding.movieDescription.setText(movieModel.description);
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}