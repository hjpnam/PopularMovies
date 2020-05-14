package com.example.popularmovies.shared;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1)
public abstract class FavoriteMovieDatabase extends RoomDatabase {
    private static FavoriteMovieDatabase instance;

    public abstract FavoriteMovieDao favoriteMovieDao();

    public static synchronized FavoriteMovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FavoriteMovieDatabase.class, "favMovie_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
