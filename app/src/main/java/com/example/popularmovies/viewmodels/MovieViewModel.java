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
    MovieRepository repository;
    private LiveData<List<Movie>> mMovies;

    public MovieViewModel(@NonNull Application application, String pageQuery, SortOrder sortOrder) {
        super(application);
        repository = MovieRepository.getInstance();
        mMovies = repository.fetchMovies(pageQuery, sortOrder);
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }
}
