package com.example.popularmovies.shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie {
    private String mTitle;
    private String mPosterPath;
    private String mOverview;
    private float mRating;
    private Date mReleaseDate;

    public Movie(String title, String posterPath, String overView, float rating, Date releaseDate) {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overView;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    public Movie(String title, String posterPath, String overView, float rating, String releaseDate) throws ParseException {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overView;
        mRating = rating;

        // Convert date string into Date object
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        mReleaseDate = format.parse(releaseDate);
    }

    public Movie(String title, String posterPath, String overView, String rating, String releaseDate) throws ParseException {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overView;
        mRating = Float.parseFloat(rating);

        // Convert date string into Date object
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        mReleaseDate = format.parse(releaseDate);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public float getRating() {
        return mRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }
}
