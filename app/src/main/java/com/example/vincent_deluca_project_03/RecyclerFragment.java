package com.example.vincent_deluca_project_03;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vincent_deluca_project_03.databinding.FragmentRecyclerBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFragment extends Fragment implements ItemClickListener {

    @Override
    public void onItemClick(MovieModel movieModel) {
        NavController navController = NavHostFragment.findNavController(RecyclerFragment.this);
        Bundle arguments = new Bundle();
        arguments.putString("poster", movieModel.url);
        arguments.putString("title", movieModel.title);
        arguments.putString("year", movieModel.year);
        arguments.putFloat("rating", movieModel.rating);
        arguments.putString("description", movieModel.description);
        navController.navigate(R.id.DetailsFragment, arguments);
    }

    public interface OnItemSelectedListener {
        public void onListItemSelected(View sharedView, int imageResourceID, String title, String year);
    }

    OnItemSelectedListener clickListener;
    private FragmentRecyclerBinding binding;
    private RecyclerAdapter recyclerAdapter;
    private List<MovieModel> movieList = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRecyclerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        RecyclerView recyclerView = view.findViewById(R.id.mainRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(this, recyclerView);
        recyclerView.setAdapter(recyclerAdapter);

        binding.fab.setOnClickListener(v ->
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
        );
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}