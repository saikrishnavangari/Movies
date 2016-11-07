package sai.com.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sai.com.popularmovies.Adapters.GridItemImageAdapter;
import sai.com.popularmovies.Connectivity.NetworkConnection;
import sai.com.popularmovies.Model.Movies;
import sai.com.popularmovies.event.SettingsChangeEvent;
import sai.com.popularmovies.utils.RestApi;

/**
 * Created by krrish on 1/11/2016.
 */

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private Retrofit retrofit;
    private RestApi restApi;
    private View rootview;
    private String movie_type;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApi = retrofit.create(RestApi.class);
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
                startActivity(settingsIntent);
            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "inside oncreateview");
        rootview = inflater.inflate(R.layout.fragemnt_main, container, false);
        if (NetworkConnection.isOnline(getActivity()))
            loadData();
        else
            Toast.makeText(getActivity(), "no Internet Connection", Toast.LENGTH_SHORT).show();
        return rootview;
    }

    //build retrofit and load user preferred data to update the ui
    private void loadData() {
        SharedPreferences sharedPrefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setMovie_type(sharedPrefernces.getString(
                getString(R.string.pref_movie_type_key),
                getString(R.string.pref_movie_type_default)));
        Log.d(LOG_TAG, "movie/TYpe" +movie_type);
        switch (getmovie_type()) {
            case "top_rated":
                getActivity().setTitle("Top Rated Movies");
                Log.d(LOG_TAG, "movie/TYpe" +getmovie_type());
                break;
            default:
                getActivity().setTitle("Popular Movies");
        }


        Call<Movies> call = restApi.getPopularMovies(movie_type, MainActivity.API_KEY, "en-US");
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(LOG_TAG, "success");
                update_ui(response);
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
    }

    //    build and load the data into layout
    void update_ui(Response<Movies> response) {
        Movies movies = response.body();
        final List<Movies.results> moviesList = movies.getResults();
        GridView gridview = (GridView) rootview.findViewById(R.id.gridview);
        GridItemImageAdapter gridAdapter = new GridItemImageAdapter(getActivity(), 0, moviesList);
        gridview.setAdapter(gridAdapter);
        //set onitemClickListener on gridview

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movies.results movieObject = moviesList.get(i);
                Log.d(LOG_TAG, "movieObject :" + movieObject.toString());
                Intent MovieDetailIntent = new Intent(getActivity(), MovieDetail.class);
                MovieDetailIntent.putExtra("movieObject", movieObject);
                MovieDetailIntent.putExtra("movie_type", getmovie_type());
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


    public String getmovie_type() {
        if (movie_type != null)
            movie_type = "popular";
            return movie_type;
    }

    public void setMovie_type(String movie_type) {

        this.movie_type = movie_type;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingsChangeEvent event)
    {
        setMovie_type(event.getMovie_type());
        Log.d(LOG_TAG,movie_type);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "inside onstart");
        EventBus.getDefault().register(this);


    }

    @Override
    public void onResume() {
        super.onResume();

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
        EventBus.getDefault().unregister(this);
        Log.d(LOG_TAG, "inside onstop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "inside onDestroy");

    }


}
