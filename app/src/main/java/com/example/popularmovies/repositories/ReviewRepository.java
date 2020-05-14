package com.example.popularmovies.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popularmovies.shared.FavoriteMovieDao;
import com.example.popularmovies.shared.FavoriteMovieDatabase;
import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.Review;
import com.example.popularmovies.shared.Trailer;
import com.example.popularmovies.utilities.JsonUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class  ReviewRepository {
    private static final String TAG = "REVIEW_REPOSITORY";
    private static ReviewRepository reviewRepository;
    private static FavoriteMovieDao favoriteMovieDao;
    private static int numCores = Runtime.getRuntime().availableProcessors();
    private ExecutorService executor = Executors.newFixedThreadPool(numCores + 1);

    public static ReviewRepository getInstance(Context context) {
        if (reviewRepository == null) {
            reviewRepository = new ReviewRepository();
            FavoriteMovieDatabase db = FavoriteMovieDatabase.getInstance(context);
            favoriteMovieDao = db.favoriteMovieDao();
        }
        return reviewRepository;
    }

    private ReviewRepository() {}

    public LiveData<List<Review>> fetchReviews(final int movieId) {
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
                Log.d(TAG, "reviews fetched");
                data.postValue(reviewList);
            }
        });

        return data;
    }

    public LiveData<List<Trailer>> fetchTrailers(final int movieId) {
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

    public LiveData<Movie> fetchFavoriteMovie(final int movieId) {
        final MutableLiveData<Movie> data = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Future<Movie> future = executor.submit(new FetchFavMovieTask(favoriteMovieDao, movieId));

                try {
                    data.postValue(future.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return data;
    }

    public void insertFavoriteMovie(Movie movie) {
        executor.execute(new InsertFavMovieTask(favoriteMovieDao, movie));
    }

    public void deleteFavoriteMovie(Movie movie) {
        executor.execute(new DeleteFavMovieTask(favoriteMovieDao, movie));
    }

    static class FetchReviewsTask implements Callable<String> {
        int movieId;

        FetchReviewsTask(int movieId) {
            this.movieId = movieId;
        }

        @Override
        public String call() throws Exception {
            URL fetchReviewListUrl = NetworkUtils.buildDetailApiUrl("1", "reviews", movieId);
            return NetworkUtils.getResponseFromHttpUrl(fetchReviewListUrl);
        }
    }

    static class FetchTrailersTask implements Callable<String> {
        int movieId;

        FetchTrailersTask(int movieId) {
            this.movieId = movieId;
        }

        @Override
        public String call() throws Exception {
            URL fetchTrailerListUrl = NetworkUtils.buildDetailApiUrl("1", "videos", movieId);
            return NetworkUtils.getResponseFromHttpUrl(fetchTrailerListUrl);
        }
    }

    static class FetchFavMovieTask implements Callable<Movie> {
        FavoriteMovieDao dao;
        int movieId;

        FetchFavMovieTask(FavoriteMovieDao dao, int movieId) {
            this.dao = dao;
            this.movieId = movieId;
        }

        @Override
        public Movie call() {
            return dao.getFavMovie(movieId);
        }
    }

    private static class InsertFavMovieTask implements Runnable {
        FavoriteMovieDao dao;
        Movie movie;

        InsertFavMovieTask(FavoriteMovieDao dao, Movie movie) {
            this.dao = dao;
            this.movie = movie;
        }

        @Override
        public void run() {
            dao.insert(movie);
        }
    }

    private static class DeleteFavMovieTask implements Runnable {
        FavoriteMovieDao dao;
        Movie movie;

        DeleteFavMovieTask(FavoriteMovieDao dao, Movie movie) {
            this.dao = dao;
            this.movie = movie;
        }

        @Override
        public void run() {
            dao.delete(movie);
        }
    }
}
