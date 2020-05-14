package com.example.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.repositories.MovieRepository;
import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.SortOrder;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repository;
    private LiveData<List<Movie>> mPopularMovies;
    private LiveData<List<Movie>> mRatedMovies;
    private LiveData<List<Movie>> mFavMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance(application);
        mPopularMovies = repository.fetchMovies("1", SortOrder.POPULAR);
        mRatedMovies = repository.fetchMovies("1", SortOrder.TOP_RATED);
        mFavMovies = repository.fetchFavoriteMovies();
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return mPopularMovies;
    }

    public LiveData<List<Movie>> getRatedMovies() {
        return mRatedMovies;
    }

    public LiveData<List<Movie>> getFavMovies() {
        return mFavMovies;
    }



}
