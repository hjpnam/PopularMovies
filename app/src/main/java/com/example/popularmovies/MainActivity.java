package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.SortOrder;
import com.example.popularmovies.viewmodels.MovieViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private MovieAdapter mMovieAdapter;
    private MovieViewModel mMovieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildRecyclerView();
        setViewModel();
        setObserver(SortOrder.POPULAR);
    }

    private void setViewModel() {
        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    private void setObserver(SortOrder sortOrder) {
        LiveData<List<Movie>> moviesLiveData;
        switch (sortOrder) {
            case TOP_RATED:
                moviesLiveData = mMovieViewModel.getRatedMovies();
                break;
            case FAVORITE:
                moviesLiveData = mMovieViewModel.getFavMovies();
                break;
            case POPULAR:
            default:
                moviesLiveData = mMovieViewModel.getPopularMovies();
        }

        moviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMovieAdapter.setMovies(movies);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_list, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        mMovieViewModel.getPopularMovies().removeObservers(this);

        switch (id) {
            case R.id.main_menu_popular:
                setObserver(SortOrder.POPULAR);
                return true;
            case R.id.main_menu_rate:
                setObserver(SortOrder.TOP_RATED);
                return true;
            case R.id.main_menu_favorite:
                setObserver(SortOrder.FAVORITE);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intentToStartMovieDetailActivity = new Intent(this, MovieDetailActivity.class);
        intentToStartMovieDetailActivity.putExtra("Selected movie", movie);

        startActivity(intentToStartMovieDetailActivity);
    }

    private void buildRecyclerView() {
        RecyclerView mMovieListRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        mMovieListRecyclerView.setHasFixedSize(true);
        mMovieListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mMovieAdapter = new MovieAdapter(this);

        mMovieListRecyclerView.setAdapter(mMovieAdapter);

    }

}
