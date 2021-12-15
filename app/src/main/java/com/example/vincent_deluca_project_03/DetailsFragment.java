package com.example.vincent_deluca_project_03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vincent_deluca_project_03.databinding.FragmentDetailsBinding;

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
            String poster = getArguments().getString("poster");
            String title = getArguments().getString("title");
            String year = getArguments().getString("year");
            float rating = getArguments().getFloat("rating");
            String description = getArguments().getString("description");
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}