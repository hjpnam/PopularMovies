package com.example.popularmovies.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popularmovies.shared.Review;
import com.example.popularmovies.shared.Trailer;
import com.example.popularmovies.utilities.JsonUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class  ReviewRepository {
    private static ReviewRepository reviewRepository;
    private static int numCores = Runtime.getRuntime().availableProcessors();
    private ExecutorService executor = Executors.newFixedThreadPool(numCores + 1);

    public static ReviewRepository getInstance() {
        if (reviewRepository == null) {
            reviewRepository = new ReviewRepository();
        }
        return reviewRepository;
    }

    private ReviewRepository() {}

    public LiveData<List<Review>> fetchReviews(final String movieId) {
        final MutableLiveData<List<Review>> data = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Future<String> reviewJsonFuture = executor.submit(new FetchReviewsTask(movieId));

                String fetchResult = null;
                try {
                    fetchResult = reviewJsonFuture.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                List<Review> reviewList = null;
                try {
                    reviewList = JsonUtils.parseReviewJson(fetchResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                data.postValue(reviewList);
            }
        });

        return data;
    }

    public LiveData<List<Trailer>> fetchTrailers(final String movieId) {
        final MutableLiveData<List<Trailer>> data = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Future<String> trailerJsonFuture = executor.submit(new FetchTrailersTask(movieId));

                String fetchResult = null;
                try {
                    fetchResult = trailerJsonFuture.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                List<Trailer> trailerList = null;
                try {
                    trailerList = JsonUtils.parseTrailerJson(fetchResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                data.postValue(trailerList);
            }
        });

        return data;
    }

    class FetchReviewsTask implements Callable<String> {
        String movieId;

        FetchReviewsTask(String movieId) {
            this.movieId = movieId;
        }

        @Override
        public String call() throws Exception {
            URL fetchReviewListUrl = NetworkUtils.buildDetailApiUrl("1", "reviews", movieId);
            return NetworkUtils.getResponseFromHttpUrl(fetchReviewListUrl, 3000);
        }
    }

    class FetchTrailersTask implements Callable<String> {
        String movieId;

        FetchTrailersTask(String movieId) {
            this.movieId = movieId;
        }

        @Override
        public String call() throws Exception {
            URL fetchTrailerListUrl = NetworkUtils.buildDetailApiUrl("1", "videos", movieId);
            return NetworkUtils.getResponseFromHttpUrl(fetchTrailerListUrl, 3000);
        }
    }
}
