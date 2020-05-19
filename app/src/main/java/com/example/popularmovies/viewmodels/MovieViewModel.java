package com.example.popularmovies.viewmodels;

import android.app.Application;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.repositories.MovieRepository;
import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.SortOrder;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    MovieRepository repository;
    private LiveData<List<Movie>> mFavoriteMovies;
    private LiveData<List<Movie>> mRatedMovies;
    private LiveData<List<Movie>> mPopularMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance(application);
        mFavoriteMovies = repository.fetchFavoriteMovies();
        mPopularMovies = repository.fetchMovies("1", SortOrder.POPULAR);
        mRatedMovies = repository.fetchMovies("1", SortOrder.TOP_RATED);
    }

    public LiveData<List<Movie>> getMovies(SortOrder sortOrder) {
        switch (sortOrder) {
            case TOP_RATED:
                return mRatedMovies;
            case FAVORITE:
                return mFavoriteMovies;
            case POPULAR:
            default:
                return mPopularMovies;
        }
    }
}
