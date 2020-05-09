package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.PosterSize;
import com.example.popularmovies.shared.Review;
import com.example.popularmovies.shared.Trailer;
import com.example.popularmovies.utilities.NetworkUtils;
import com.example.popularmovies.viewmodels.ReviewViewModel;
import com.example.popularmovies.viewmodels.ReviewViewModelFactory;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private ReviewViewModel reviewViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Movie selectedMovie = getMovieFromIntent();

        populateDetailsViews(selectedMovie);

        buildRecyclerView();

        setViewModel(selectedMovie);
    }

    private void setViewModel(Movie selectedMovie) {
        reviewViewModel = new ViewModelProvider(this,
                new ReviewViewModelFactory(this.getApplication(), selectedMovie.getId()))
                .get(ReviewViewModel.class);
        reviewViewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                mReviewAdapter.setReviews(reviews);
            }
        });
        reviewViewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                mTrailerAdapter.setTrailers(trailers);
            }
        });
    }

    private Movie getMovieFromIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra("Selected movie");
    }

    private void populateDetailsViews(Movie selectedMovie) {
        String title = selectedMovie.getTitle();
        String posterPath = NetworkUtils.buildPosterUrl(selectedMovie.getPosterPath(), PosterSize.W342).toString();
        String overview = selectedMovie.getOverview();
        float rating = selectedMovie.getRating();
        long releaseDateLong = selectedMovie.getReleaseDate();

        // Date formatting
        Date releaseDate = new Date(releaseDateLong);
        String datePattern = "MM/dd/yyyy";
        DateFormat df = new SimpleDateFormat(datePattern);

        TextView titleTextView = (TextView) findViewById(R.id.tv_movie_detail_title);
        ImageView posterImageView = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        TextView releaseDateTextView = (TextView) findViewById(R.id.tv_movie_detail_release_date);
        TextView ratingTextView = (TextView) findViewById(R.id.tv_movie_detail_rating);
        TextView overviewTextView = (TextView) findViewById(R.id.tv_movie_detail_overview);

        titleTextView.setText(title);
        Picasso.get().load(posterPath).into(posterImageView);
        releaseDateTextView.setText(df.format(releaseDate));
        ratingTextView.setText(String.valueOf(rating) + " / 10");
        overviewTextView.setText(overview);
    }

    private void buildRecyclerView() {
        RecyclerView reviewRv = (RecyclerView) findViewById(R.id.rv_review_list);
        reviewRv.setHasFixedSize(true);
        reviewRv.setNestedScrollingEnabled(true);
        reviewRv.setLayoutManager(new LinearLayoutManager(this));

        mReviewAdapter = new ReviewAdapter();
        reviewRv.setAdapter(mReviewAdapter);

        RecyclerView trailerRv = (RecyclerView) findViewById(R.id.rv_trailer_list);
        trailerRv.setHasFixedSize(true);
        trailerRv.setNestedScrollingEnabled(true);
        trailerRv.setLayoutManager(new LinearLayoutManager(this));

        mTrailerAdapter = new TrailerAdapter();
        trailerRv.setAdapter(mTrailerAdapter);
    }
}
