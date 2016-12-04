package sai.com.popularmovies;

import android.app.Fragment;
import android.content.Intent;
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

/**
 * Created by krrish on 1/11/2016.
 */

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static String MOVIE_TYPE_KEY="movie_type";
    private static String MOVIE_LIST_KEY="movieListArrayKey";
    private View rootview;
    private String movie_type;
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
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            moviesList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            movie_type=savedInstanceState.getString(MOVIE_TYPE_KEY);
            update_ui();
        }

    }

    //build retrofit and load user preferred data to update the ui
    private void loadData() {
        movie_type = Utilities.getPreferredMovieType(getActivity());
        if(!movie_type.equals("favourites")) {
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
        }
        else{

            Cursor cursor=getActivity().getContentResolver().query(MoviesProvider.Movies.CONTENT_URI,
                    null,null,null,null);
            getMovieList(cursor);
            update_ui();
        }
    }

    private void getMovieList(Cursor cursor) {
        Log.d(LOG_TAG,String.valueOf(cursor.getCount()));
        while(cursor.moveToNext()){
            Log.d(LOG_TAG,cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_overview)));
            Movies.results movieObject= new Movies.results();
            movieObject.setId(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_movieId)));
            movieObject.setTitle(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_TITLE)));
            movieObject.setVote_count(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_voteCount)));
            movieObject.setVote_average(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_voteAverage)));
            movieObject.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_backdropPath)));
            movieObject.setOriginal_language(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_language)));
            movieObject.setPopularity(cursor.getInt(cursor.getColumnIndex(MoviesColumns.Column_popularity)));
           // movieObject.setOverview(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_overview)));
            movieObject.setRelease_date(cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_releaseDate)));

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, moviesList);
        outState.putString(MOVIE_TYPE_KEY, movie_type);
    }



    //    build and load the data into layout

    void update_ui() {

        GridView gridview = (GridView) rootview.findViewById(R.id.gridview);
        GridItemImageAdapter gridAdapter = new GridItemImageAdapter(getActivity(), 0, moviesList);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movies.results movieObject = moviesList.get(i);
                Log.d(LOG_TAG, "movieObject :" + movieObject.toString());
                Intent MovieDetailIntent = new Intent(getActivity(), MovieDetail.class);
                MovieDetailIntent.putExtra("movieObject", movieObject);
                startActivity(MovieDetailIntent);
            }
        });
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


}
