package com.example.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ReviewViewModelFactory implements ViewModelProvider.Factory {
    private Application mApp;
    private String mMovieId;

    public ReviewViewModelFactory(Application app, String movieId) {
        mApp = app;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReviewViewModel(mApp, mMovieId);
    }
}
