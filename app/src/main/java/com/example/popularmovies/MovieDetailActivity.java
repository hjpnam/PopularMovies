package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.PosterSize;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie selectedMovie = intent.getParcelableExtra("Selected movie");

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
        ratingTextView.setText(String.valueOf(rating));
        overviewTextView.setText(overview);
    }
}
