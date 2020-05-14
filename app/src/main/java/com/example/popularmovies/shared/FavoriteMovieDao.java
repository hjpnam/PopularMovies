package com.example.popularmovies.shared;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {
    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE FROM favMovie_table")
    void deleteAllFavMovies();

    @Query("SELECT * FROM favMovie_table")
    LiveData<List<Movie>> getAllFavMovies();

    @Query("SELECT * FROM favMovie_table WHERE id = :queryId")
    Movie getFavMovie(int queryId);
}
