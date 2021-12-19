package com.example.vincent_deluca_project_03;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference moviesRef = database.getReference("Movies");
    ChildEventListener moviesRefListener;
    private List<MovieKey> movieList;
    private List<MovieKey> movieListFiltered;
    private final ItemClickListener itemClickListener;

    private class MovieKey {
        private String key;
        private MovieModel movieModel;

        protected MovieKey(String key, MovieModel movieModel) {
            this.key = key;
            this.movieModel = movieModel;
        }
    }

    public RecyclerAdapter(ItemClickListener itemClickListener, RecyclerView recyclerView) {
        this.itemClickListener = itemClickListener;
        movieList = movieListFiltered = new ArrayList<>();
        moviesRefListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MovieKey movieKey = new MovieKey(snapshot.getKey(), new MovieModel(
                        snapshot.child("description").getValue().toString(),
                        snapshot.child("director").getValue().toString(),
                        snapshot.child("length").getValue().toString(),
                        ((Number) snapshot.child("rating").getValue()).floatValue(),
                        snapshot.child("stars").getValue().toString(),
                        snapshot.child("title").getValue().toString(),
                        snapshot.child("url").getValue().toString(),
                        snapshot.child("year").getValue().toString()
                ));
                movieList.add(movieKey);
                notifyItemInserted(movieList.size() - 1);
                recyclerView.scrollToPosition(movieList.size() - 1);
                movieListFiltered = movieList;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < movieList.size(); i++) {
                    if (movieList.get(i).key.equals(snapshot.getKey())) {
                        movieList.remove(i);
                        notifyItemRemoved(i);
                        movieListFiltered = movieList;
                        return;
                    }
                }
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
                    List<MovieKey> filteredList = new ArrayList<>();
                    for (MovieKey movie : movieList) {
                        String title = movie.movieModel.title.toLowerCase();
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
                movieListFiltered = (List<MovieKey>) filterResults.values;
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
        MovieKey movieKey = movieListFiltered.get(position);
        MovieModel movieModel = movieKey.movieModel;
        holder.card_title.setText(movieModel.title);
        holder.card_rating.setRating(movieModel.rating);
        holder.card_year.setText(movieModel.year);
        StorageReference pathReference = FirebaseStorage.getInstance().getReference(movieModel.url);
        pathReference.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri).into(holder.card_poster));
        holder.card_poster.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClick(movieModel);
        });
        holder.extras.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.extras_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete) {
                    moviesRef.child(movieKey.key).setValue(null).addOnSuccessListener(unused ->
                            Toast.makeText(v.getContext(), "Delete", Toast.LENGTH_SHORT).show());
                    return true;
                } else if (item.getItemId() == R.id.duplicate) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference moviesRef = database.getReference("Movies");
                    final DatabaseReference newMovieRef = moviesRef.push();
                    newMovieRef.setValue(movieModel).addOnSuccessListener(unused ->
                            Toast.makeText(v.getContext(), "Duplicate", Toast.LENGTH_SHORT).show());
                    return true;
                } else {
                    return false;
                }
            });
            popup.show();
        });
    }

    public void removeListener() {
        if (moviesRef != null && moviesRefListener != null)
            moviesRef.removeEventListener(moviesRefListener);
    }

    @Override
    public int getItemCount() {
        return movieListFiltered.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView card_title;
        public TextView card_year;
        public RatingBar card_rating;
        public ImageView card_poster;
        public ImageView extras;

        public ViewHolder(@NonNull View view) {
            super(view);
            card_title = view.findViewById(R.id.card_title);
            card_year = view.findViewById(R.id.card_year);
            card_rating = view.findViewById(R.id.card_rating);
            card_poster = view.findViewById(R.id.card_poster);
            extras = view.findViewById(R.id.extras);
        }
    }
}
