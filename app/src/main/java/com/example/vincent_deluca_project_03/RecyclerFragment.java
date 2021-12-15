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

import java.util.ArrayList;
import java.util.List;

public class RecyclerFragment extends Fragment implements ItemClickListener {

    @Override
    public void onItemClick(MovieModel movieModel) {
        NavArgument posterArg = new NavArgument.Builder().setDefaultValue(movieModel.url).build();
        NavArgument titleArg = new NavArgument.Builder().setDefaultValue(movieModel.title).build();
        NavArgument yearArg = new NavArgument.Builder().setDefaultValue(movieModel.year).build();
        NavArgument ratingArg = new NavArgument.Builder().setDefaultValue(movieModel.rating).build();
        NavArgument desciptionArg = new NavArgument.Builder().setDefaultValue(movieModel.description).build();
        NavController navController = NavHostFragment.findNavController(RecyclerFragment.this);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.nav_graph);
        navGraph.addArgument("poster", posterArg);
        navGraph.addArgument("title", titleArg);
        navGraph.addArgument("year", yearArg);
        navGraph.addArgument("rating", ratingArg);
        navGraph.addArgument("description", desciptionArg);
        navController.setGraph(navGraph);
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
        recyclerAdapter = new RecyclerAdapter( this, recyclerView);
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}