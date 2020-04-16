package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.popularmovies.shared.Movie;
import com.example.popularmovies.shared.SortOrder;
import com.example.popularmovies.utilities.JsonUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView mMovieListRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieAdapter mMovieAdapter;
    private SortOrder mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSortOrder = SortOrder.POPULAR;
        buildRecyclerView();
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
                fetchMovies();
                return true;

            case R.id.main_menu_rate:
                mSortOrder = SortOrder.TOP_RATED;
                fetchMovies();
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

    private void fetchMovies() {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<String> movieJsonFuture = service.submit(new FetchMovieTask());

        try {
            String fetchResult = movieJsonFuture.get(2, TimeUnit.SECONDS);
            List<Movie> movieList = JsonUtils.parseTmdbJson(fetchResult);
            mMovieAdapter.setMovies(movieList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buildRecyclerView() {
        mMovieListRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        mMovieListRecyclerView.setHasFixedSize(true);
        mMovieListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mMovieAdapter = new MovieAdapter(this);

        mMovieListRecyclerView.setAdapter(mMovieAdapter);

        fetchMovies();
    }

    class FetchMovieTask implements Callable<String> {

        @Override
        public String call() throws IOException {
            URL fetchMovieListUrl = NetworkUtils.buildApiUrl("1", mSortOrder);
            return NetworkUtils.getResponseFromHttpUrl(fetchMovieListUrl, 3000);
        }

    }
}
