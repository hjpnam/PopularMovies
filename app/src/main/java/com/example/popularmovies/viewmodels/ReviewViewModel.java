package com.example.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.repositories.ReviewRepository;
import com.example.popularmovies.shared.Review;
import com.example.popularmovies.shared.Trailer;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {
    private LiveData<List<Review>> mReviews;
    private LiveData<List<Trailer>> mTrailers;

    public ReviewViewModel(@NonNull Application application, String movieId) {
        super(application);
        ReviewRepository repository = ReviewRepository.getInstance();
        mReviews = repository.fetchReviews(movieId);
        mTrailers = repository.fetchTrailers(movieId);
    }

    public LiveData<List<Review>> getReviews() {
        return mReviews;
    }

    public LiveData<List<Trailer>> getTrailers() { return mTrailers; }
}
