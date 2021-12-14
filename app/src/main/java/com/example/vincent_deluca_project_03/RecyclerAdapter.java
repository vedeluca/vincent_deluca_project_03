package com.example.vincent_deluca_project_03;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {
//TODO: FINISH THIS
    private RecyclerFragment.OnItemSelectedListener clickListener = null;

    @Override
    public Filter getFilter() {
        return null;
    }
    public void setOnItemClickListener(RecyclerFragment.OnItemSelectedListener listener) {
        clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movie_title;
        public TextView movie_date;
        public RatingBar movie_rating;
        public ImageView movie_poster;

        public ViewHolder(@NonNull View view) {
            super(view);
            movie_title = (TextView) view.findViewById(R.id.movie_title);
            movie_date = (TextView) view.findViewById(R.id.movie_date);
            movie_rating = (RatingBar) view.findViewById(R.id.movie_rating);
            movie_poster = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }
}
