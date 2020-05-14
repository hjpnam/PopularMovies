package com.example.popularmovies.utilities;


import android.net.Uri;
import android.util.Log;


import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.shared.PosterSize;
import com.example.popularmovies.shared.SortOrder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    // TMDB base URL
    private static final String TMDB_API_URL = "api.themoviedb.org";

    // language the API will return
    private static final String LANGUAGE = "en-US";

    static final String API_KEY_PARAM = "api_key";
    static final String LANGUAGE_PARAM = "language";
    static final String PAGE_PARAM = "page";

    static final String AUTH_VERSION_PATH = "3";
    static final String SUB_MOVIE_PATH = "movie";

    // TMDB image fetch url
    private static final String TMDB_POSTER_FETCH_URL = "http://image.tmdb.org/t/p/";


    /**
     * Builds URL for api requests to the TMDB server.
     *
     * @param pageQuery The page to be queried for.
     * @param sortOrder The sort order for the queried movies
     * @return The URL for querying the TMDB server
     */
    public static URL buildApiUrl(String pageQuery, SortOrder sortOrder) {
        String pathExtension = null;
        switch (sortOrder) {
            case TOP_RATED:
                pathExtension = "top_rated";
                break;
            case POPULAR:
                pathExtension = "popular";
                break;
            default:
                pathExtension = null;
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(TMDB_API_URL)
                .appendPath(AUTH_VERSION_PATH)
                .appendPath(SUB_MOVIE_PATH)
                .appendPath(pathExtension)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, pageQuery);
        Uri builtUri = builder.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built API URL " + url);

        return url;
    }

    public static URL buildDetailApiUrl(String pageQuery, String subPath, int movieId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(TMDB_API_URL)
                .appendPath(AUTH_VERSION_PATH)
                .appendPath(SUB_MOVIE_PATH)
                .appendPath(String.valueOf(movieId))
                .appendPath(subPath)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, pageQuery);
        Uri builtUri = builder.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built detail API URL " + url);

        return url;
    }

    public static URL buildPosterUrl(String posterId, PosterSize posterSize) {
        String posterSizePath = null;
        switch (posterSize) {
            case W92:
                posterSizePath = "w92";
                break;
            case W154:
                posterSizePath = "w154";
                break;
            case W342:
                posterSizePath = "w342";
                break;
            case W500:
                posterSizePath = "w500";
                break;
            case W780:
                posterSizePath = "w780";
                break;
            case W185:
            default:
                posterSizePath = "w185";
        }

        Uri uri = Uri.parse(TMDB_POSTER_FETCH_URL).buildUpon()
                .appendPath(posterSizePath)
                .appendEncodedPath(posterId)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Poster URL " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    do {
                        line = reader.readLine();
                        sb.append(line);
                        sb.append("\n");
                    } while (line != null);

                    reader.close();
                    return sb.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
