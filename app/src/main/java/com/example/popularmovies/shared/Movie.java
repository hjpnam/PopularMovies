package com.example.popularmovies.shared;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Entity(tableName = "favMovie_table")
public class Movie implements Parcelable {
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "poster_path")
    private String mPosterPath;
    @ColumnInfo(name = "overview")
    private String mOverview;
    @ColumnInfo(name = "rating")
    private float mRating;
    @ColumnInfo(name = "release_date")
    private long mReleaseDate;
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    private int mId;

    public Movie(String title, String posterPath, String overview, float rating, long releaseDate, int id) {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
        mId = id;
    }

    @Ignore
    public Movie(String title, String posterPath, String overView, float rating, String releaseDate, int id) throws ParseException {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overView;
        mRating = rating;
        mId = id;

        // Convert date string into Date object
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        mReleaseDate = format.parse(releaseDate).getTime();
    }

    @Ignore
    public Movie(String title, String posterPath, String overView, String rating, String releaseDate, int id) throws ParseException {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overView;
        mRating = Float.parseFloat(rating);
        mId = id;

        // Convert date string into Date object
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        mReleaseDate = format.parse(releaseDate).getTime();
    }

    @Ignore
    protected Movie(Parcel in) {
        mTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mRating = in.readFloat();
        mReleaseDate = in.readLong();
        mId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public long getReleaseDate() {
        return mReleaseDate;
    }

    public int getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeFloat(mRating);
        dest.writeLong(mReleaseDate);
        dest.writeInt(mId);
    }
}
