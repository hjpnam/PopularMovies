package com.example.popularmovies.utilities;

import com.example.popularmovies.shared.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List<Movie> parseTmdbJson(String jsonString) throws JSONException, ParseException {
        // Individual movie info is an element of "results" array.
        final String TMDB_RESULT = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_TITLE = "title";
        final String TMDB_VOTE_AVG = "vote_average";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_RELEASE_DATE = "release_date";

        // List of Movie objects to be returned;
        List<Movie> movieList = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(jsonString);

        if (jsonString == null || jsonString == "") {
            return null;
        }

        JSONArray resultsArray = moviesJson.getJSONArray(TMDB_RESULT);
        for (int i = 0; i < resultsArray.length(); i++) {
            // Get JSON object representing a movie
            JSONObject movieJSON = resultsArray.getJSONObject(i);

            String title = movieJSON.getString(TMDB_TITLE);
            String posterPath = movieJSON.getString(TMDB_POSTER_PATH);
            String overview = movieJSON.getString(TMDB_OVERVIEW);
            float rating = (float) movieJSON.getDouble(TMDB_VOTE_AVG);
            String releaseDate = movieJSON.getString(TMDB_RELEASE_DATE);

            Movie movie = new Movie(title, posterPath, overview, rating, releaseDate);

            movieList.add(movie);
        }

        return movieList;
    }
}
