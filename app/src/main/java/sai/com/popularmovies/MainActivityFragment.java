package sai.com.popularmovies;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sai.com.popularmovies.Adapters.GridItemImageAdapter;
import sai.com.popularmovies.Model.Movies;
import sai.com.popularmovies.data.MoviesColumns;
import sai.com.popularmovies.data.MoviesProvider;
import sai.com.popularmovies.utils.RestApi;
import sai.com.popularmovies.utils.Utilities;

import static sai.com.popularmovies.R.id.fragment_movieDetail_container;
import static sai.com.popularmovies.R.id.gridview;

/**
 * Created by krrish on 1/11/2016.
 */

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int LOADER_ID = 1;
    private static String MOVIE_TYPE_KEY = "movie_type";
    private static String MOVIE_LIST_KEY = "movieListArrayKey";
    private View rootview;
    private String movie_type;
    private GridView mgridview;
    private ArrayList<Movies.results> moviesList = new ArrayList<Movies.results>();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(LOG_TAG, "inside on create ");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingsIntent);
            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "inside oncreateView");
        rootview = inflater.inflate(R.layout.fragemnt_main, container, false);
        mgridview = (GridView) rootview.findViewById(gridview);
        final Bundle args = new Bundle();
        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movies.results movieObject = moviesList.get(i);
                if (!MainActivity.mTwoPane) {
                    Log.d(LOG_TAG, "movieObject :" + movieObject.toString());
                    Intent MovieDetailIntent = new Intent(getActivity(), MovieDetail.class);
                    MovieDetailIntent.putExtra("movieObject", movieObject);
                    startActivity(MovieDetailIntent);
                } else {
                    args.putParcelable(MovieDetailFragment.MOVIEOBJECT, movieObject);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(args);
                    getActivity().getFragmentManager().beginTransaction().replace(fragment_movieDetail_container, fragment).commit();
                }
            }
        });
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            moviesList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            movie_type = savedInstanceState.getString(MOVIE_TYPE_KEY);
            if (movie_type.equals("favourites")) {
                //  getLoaderManager().initLoader(LOADER_ID,null,this);
            } else {
                update_ui();
            }

        }

    }

    //build retrofit and load user preferred data to update the ui
    private void loadData() {
        movie_type = Utilities.getPreferredMovieType(getActivity());
        if (!movie_type.equals("favourites")) {
            RestApi restApi = Utilities.getRetrofit();
            Call<Movies> call = restApi.getPopularMovies(movie_type, MainActivity.API_KEY, "en-US");
            call.enqueue(new Callback<Movies>() {
                             @Override
                             public void onResponse(Call<Movies> call, Response<Movies> response) {
                                 Log.d(LOG_TAG, "success");
                                 Movies movies = response.body();
                                 Log.d(LOG_TAG, "movies: " + response.body());
                                 moviesList = movies.getResults();
                                 update_ui();
                             }

                             @Override
                             public void onFailure(Call<Movies> call, Throwable t) {

                             }
                         }

            );
        } else {
            getLoaderManager().initLoader(LOADER_ID, null, this);
            /*Cursor cursor=getActivity().getContentResolver().query(MoviesProvider.Movies.CONTENT_URI,
                    null,null,null,null);*/
        }
    }

    private void getMovieList(Cursor cursor) {
        Log.d(LOG_TAG, String.valueOf(cursor.getCount()));
        moviesList.clear();
        while (cursor.moveToNext()) {
            Movies.results movieObject = new Movies.results();
            movieObject.setId(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_movieId)));
            movieObject.setOriginal_title(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_TITLE)));
            movieObject.setVote_count(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_voteCount)));
            movieObject.setVote_average(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_voteAverage)));
            movieObject.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_backdropPath)));
            movieObject.setOriginal_language(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_language)));
            movieObject.setPopularity(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_popularity)));
            movieObject.setOverview(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_overview)));
            movieObject.setRelease_date(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_releaseDate)));
            movieObject.setPoster_path(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_posterPath)));
            moviesList.add(movieObject);
        }
        Log.d(LOG_TAG, "moviesList" + String.valueOf(moviesList.size()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, moviesList);
        outState.putString(MOVIE_TYPE_KEY, movie_type);
    }


    //    build and load the data into layout

    void update_ui() {
        GridItemImageAdapter gridAdapter = new GridItemImageAdapter(getActivity(), 0, moviesList);
        Log.d(LOG_TAG, "moviesList" + String.valueOf(moviesList.size()));
        mgridview.setAdapter(gridAdapter);


    }

    /*public String getmovie_type() {

        SharedPreferences sharedPrefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movie_type = sharedPrefernces.getString(
                getString(R.string.pref_movie_type_key), getString(R.string.pref_movie_type_default));
        Log.d(LOG_TAG, "movietype :" + movie_type);
        return movie_type;
    }*/
    void setScreenTitle() {
        switch (Utilities.getPreferredMovieType(getActivity())) {

            case "top_rated":
                getActivity().setTitle("Top Rated Movies");
                break;
            case "favourites":
                getActivity().setTitle("My Favourites");
                break;
            default:
                getActivity().setTitle("Popular Movies");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onstart");

        if (movie_type != Utilities.getPreferredMovieType(getActivity())) {
            Log.d(LOG_TAG, "loading in onstart");
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setScreenTitle();
        Log.d(LOG_TAG, "inside onResume");

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "inside onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "inside onstop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.d(LOG_TAG, "inside onDestroy");
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MoviesProvider.Movies.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
       /* if(movie_type.equals("favourites")) {
            if ((cursor.getCount() >= 0)) {
                mcursorAdapter = new GridItemImageCursorAdapter(getActivity(), null, 0);
                mgridview.setAdapter(mcursorAdapter);
                mcursorAdapter.swapCursor(cursor);
                getMovieList(cursor);
                Log.d(LOG_TAG, "cursorloadfinished");
            }
            if (cursor.getCount() == 0)
                Toast.makeText(getActivity(), "Ther are no movies in the list, please select some", Toast.LENGTH_SHORT).show();
        }*/
        if (movie_type.equals("favourites")) {
            getMovieList(cursor);
            update_ui();
        }
        if (cursor.getCount() == 0)
            Toast.makeText(getActivity(), "Ther are no movies in the list, please select some", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mgridview.setAdapter(null);
    }
}
