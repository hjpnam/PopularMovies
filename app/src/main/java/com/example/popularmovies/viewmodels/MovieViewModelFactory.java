package com.example.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmovies.shared.SortOrder;

public class MovieViewModelFactory implements ViewModelProvider.Factory {
    private Application mApp;
    private String mPageQuery;
    private SortOrder mSortOrder;

    public MovieViewModelFactory(Application app, String pageQuery, SortOrder sortOrder) {
        mApp = app;
        mPageQuery = pageQuery;
        mSortOrder = sortOrder;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(mApp, mPageQuery, mSortOrder);
    }
}
