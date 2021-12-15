package com.example.vincent_deluca_project_03;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference moviesRef = database.getReference("Movies");
    ChildEventListener moviesRefListener;
    private List<MovieModel> movieList;
    private List<MovieModel> movieListFiltered;
    private final ItemClickListener itemClickListener;

    public RecyclerAdapter(ItemClickListener itemClickListener, RecyclerView recyclerView) {
        this.itemClickListener = itemClickListener;
        movieList = movieListFiltered = new ArrayList<>();
        moviesRefListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                movieList.add(new MovieModel(
                        snapshot.child("description").getValue().toString(),
                        snapshot.child("director").getValue().toString(),
                        snapshot.child("length").getValue().toString(),
                        ((Double) snapshot.child("rating").getValue()).floatValue(),
                        snapshot.child("stars").getValue().toString(),
                        snapshot.child("title").getValue().toString(),
                        snapshot.child("url").getValue().toString(),
                        snapshot.child("year").getValue().toString()
                ));
                notifyItemInserted(movieList.size() - 1);
                recyclerView.scrollToPosition(movieList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        moviesRef.addChildEventListener(moviesRefListener);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    movieListFiltered = movieList;
                } else {
                    List<MovieModel> filteredList = new ArrayList<>();
                    for (MovieModel movie : movieList) {
                        String title = movie.title.toLowerCase();
                        if (title.contains(charString.toLowerCase()))
                            filteredList.add(movie);
                    }
                    movieListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = movieListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                movieListFiltered = (List<MovieModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MovieModel movieModel = movieList.get(position);
        holder.card_title.setText(movieModel.title);
        holder.card_rating.setRating(movieModel.rating);
        holder.card_year.setText(movieModel.year);
        StorageReference pathReference = FirebaseStorage.getInstance().getReference(movieModel.url);
        pathReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(holder.card_poster));
        holder.card_poster.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClick(movieModel);
        });
    }

    public void removeListener() {
        if (moviesRef != null && moviesRefListener != null)
            moviesRef.removeEventListener(moviesRefListener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView card_title;
        public TextView card_year;
        public RatingBar card_rating;
        public ImageView card_poster;

        public ViewHolder(@NonNull View view) {
            super(view);
            card_title = (TextView) view.findViewById(R.id.card_title);
            card_year = (TextView) view.findViewById(R.id.card_year);
            card_rating = (RatingBar) view.findViewById(R.id.card_rating);
            card_poster = (ImageView) view.findViewById(R.id.card_poster);
        }
    }
}
