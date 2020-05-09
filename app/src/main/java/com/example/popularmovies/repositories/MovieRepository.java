package com.example.popularmovies.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.SortOrder;
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

public class MovieRepository {
    private static MovieRepository movieRepository;
    private static int numCores = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor = Executors.newFixedThreadPool(numCores + 1);

    public static MovieRepository getInstance() {
        if (movieRepository == null) {
            movieRepository = new MovieRepository();
        }
        return movieRepository;
    }

    private MovieRepository() {

    }

    public LiveData<List<Movie>> fetchMovies(final String pageQuery, final SortOrder sortOrder) {
        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Future<String> movieJsonFuture = executor.submit(new FetchMoviesTask(pageQuery, sortOrder));

                String fetchResult = null;
                try {
                    fetchResult = movieJsonFuture.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                List<Movie> movieList = null;
                try {
                    movieList = JsonUtils.parseTmdbJson(fetchResult);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                data.postValue(movieList);
            }
        });

        return data;
    }

    class FetchMoviesTask implements Callable<String> {
        String pageQuery;
        SortOrder sortOrder;

        FetchMoviesTask(String pageQuery, SortOrder sortOrder) {
            this.pageQuery = pageQuery;
            this.sortOrder = sortOrder;
        }

        @Override
        public String call() throws Exception {
            URL fetchMovieListUrl = NetworkUtils.buildApiUrl(pageQuery, sortOrder);
            return NetworkUtils.getResponseFromHttpUrl(fetchMovieListUrl, 3000);
        }
    }
}
