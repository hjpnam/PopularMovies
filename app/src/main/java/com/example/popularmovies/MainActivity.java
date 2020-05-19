package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
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
    private SortOrder mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mSortOrder = (SortOrder) savedInstanceState.getSerializable("SORT_ORDER");
        } else {
            mSortOrder = SortOrder.POPULAR;
        }
        buildRecyclerView();
        setViewModel(mSortOrder);
    }

    private void setViewModel(SortOrder sortOrder) {
        mMovieViewModel = new ViewModelProvider(this)
                .get(MovieViewModel.class);
        mMovieViewModel.getMovies(sortOrder).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMovieAdapter.setMovies(movies);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("SORT_ORDER", mSortOrder);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_list, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.main_menu_popular:
                mSortOrder = SortOrder.POPULAR;
                setViewModel(SortOrder.POPULAR);
                return true;
            case R.id.main_menu_rate:
                mSortOrder = SortOrder.TOP_RATED;
                setViewModel(SortOrder.TOP_RATED);
                return true;
            case R.id.main_menu_favorite:
                mSortOrder = SortOrder.FAVORITE;
                setViewModel(SortOrder.FAVORITE);
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