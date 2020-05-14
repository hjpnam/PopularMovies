package com.example.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.repositories.ReviewRepository;
import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.Review;
import com.example.popularmovies.shared.Trailer;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {
    private LiveData<List<Review>> mReviews;
    private LiveData<List<Trailer>> mTrailers;
    private LiveData<Movie> mFavoriteMovie;
    private ReviewRepository repository;

    public ReviewViewModel(@NonNull Application application, int movieId) {
        super(application);
        repository = ReviewRepository.getInstance(application);
        mReviews = repository.fetchReviews(movieId);
        mTrailers = repository.fetchTrailers(movieId);
        mFavoriteMovie = repository.fetchFavoriteMovie(movieId);
    }

    public LiveData<List<Review>> getReviews() {
        return mReviews;
    }

    public LiveData<List<Trailer>> getTrailers() { return mTrailers; }

    public LiveData<Movie> getFavoriteMovie() { return mFavoriteMovie; }

    public void insertFavoriteMovie(Movie movie) {
        repository.insertFavoriteMovie(movie);
    }

    public void deleteFavoriteMovie(Movie movie) {
        repository.deleteFavoriteMovie(movie);
    }
}
