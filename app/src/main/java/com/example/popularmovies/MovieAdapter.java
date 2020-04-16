package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.PosterSize;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieDetailAdapterViewHolder> {
    private List<Movie> mMovies;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
        mMovies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_list_item, parent, shouldAttachToParentImmediately);
        return new MovieDetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieDetailAdapterViewHolder holder, int position) {
        String posterUrl = NetworkUtils.buildPosterUrl(mMovies.get(position).getPosterPath(), PosterSize.W500).toString();
        Picasso.get().load(posterUrl).into(holder.mPosterView);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    public void setMovies(List<Movie> movies) {
        mMovies = new ArrayList<>(movies);
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public class MovieDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPosterView;

        public MovieDetailAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mPosterView = (ImageView) itemView.findViewById(R.id.iv_poster);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = mMovies.get(adapterPosition);
            mClickHandler.onClick(selectedMovie);
        }
    }
}
