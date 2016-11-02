package sai.com.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sai.com.popularmovies.Adapters.GridItemImageAdapter;
import sai.com.popularmovies.Connectivity.NetworkConnection;
import sai.com.popularmovies.Model.Movies;
import sai.com.popularmovies.utils.RestApi;

/**
 * Created by krrish on 1/11/2016.
 */

public class MainActivityFragment extends Fragment {
    private View rootview;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragemnt_main, container, false);
        if(NetworkConnection.isOnline(getActivity()))
        loadData();
        else
            Toast.makeText(getActivity(),"no Internet Connection",Toast.LENGTH_SHORT).show();
        return rootview;
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi restApi = retrofit.create(RestApi.class);

        Call<Movies> call = restApi.getPopularMovies(MainActivity.API_KEY, "en-US");
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
    void update_ui(Response<Movies> response){
        Movies movies=response.body();
        final List<Movies.results> moviesList=movies.getResults();
        GridView gridview= (GridView) rootview.findViewById(R.id.gridview);
        GridItemImageAdapter gridAdapter=new GridItemImageAdapter(getActivity(),0,moviesList);
        gridview.setAdapter(gridAdapter);
        for (Movies.results movie:moviesList){
            Log.d(LOG_TAG, movie.toString());
        }


        //set onitemClickListener on gridview

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   Movies.results movieObject= moviesList.get(i);
            }
        });
    }
}
