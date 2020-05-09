package com.example.popularmovies.utilities;

import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.Review;
import com.example.popularmovies.shared.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String QUERY_RESULTS = "results";

    public static List<Movie> parseTmdbJson(String jsonString) throws JSONException, ParseException {
        // Individual movie info is an element of "results" array.
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_TITLE = "title";
        final String TMDB_VOTE_AVG = "vote_average";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_ID = "id";

        // List of Movie objects to be returned;
        List<Movie> movieList = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(jsonString);

        if (jsonString == null || jsonString.equals("")) {
            return null;
        }

        JSONArray resultsArray = moviesJson.getJSONArray(QUERY_RESULTS);
        for (int i = 0; i < resultsArray.length(); i++) {
            // Get JSON object representing a movie
            JSONObject movieJSON = resultsArray.getJSONObject(i);

            String title = movieJSON.getString(TMDB_TITLE);
            String posterPath = movieJSON.getString(TMDB_POSTER_PATH);
            String overview = movieJSON.getString(TMDB_OVERVIEW);
            float rating = (float) movieJSON.getDouble(TMDB_VOTE_AVG);
            String releaseDate = movieJSON.getString(TMDB_RELEASE_DATE);
            String id = movieJSON.getString(TMDB_ID);

            Movie movie = new Movie(title, posterPath, overview, rating, releaseDate, id);

            movieList.add(movie);
        }

        return movieList;
    }

    public static List<Review> parseReviewJson(String jsonString) throws JSONException {
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        List<Review> reviewList = new ArrayList<>();

        JSONObject reviewsJson = new JSONObject(jsonString);

        if (jsonString == null || jsonString.equals("")) {
            return null;
        }

        JSONArray reviewsArray = reviewsJson.getJSONArray(QUERY_RESULTS);
        for (int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewJSON = reviewsArray.getJSONObject(i);

            String author = reviewJSON.getString(REVIEW_AUTHOR);
            String content = reviewJSON.getString(REVIEW_CONTENT);

            Review review = new Review(content, author);

            reviewList.add(review);
        }

        return reviewList;
    }

    public static List<Trailer> parseTrailerJson(String jsonString) throws JSONException {
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
        final String TRAILER_SITE = "site";
        final String VIDEO_TYPE = "type";

        List<Trailer> trailerList = new ArrayList<>();

        JSONObject videosJson = new JSONObject(jsonString);

        if (jsonString == null || jsonString.equals("")) {
            return null;
        }

        JSONArray videosArray = videosJson.getJSONArray(QUERY_RESULTS);
        for (int i = 0; i < videosArray.length(); i++) {
            JSONObject videoJSON = videosArray.getJSONObject(i);

            if (videoJSON.getString(VIDEO_TYPE).toString().equals("Trailer")) {
                String key = videoJSON.getString(TRAILER_KEY).toString();
                String name = videoJSON.getString(TRAILER_NAME).toString();
                String site = videoJSON.getString(TRAILER_SITE).toString();

                Trailer trailer = new Trailer(key, name, site);

                trailerList.add(trailer);
            }
        }

        return trailerList;
    }
}
