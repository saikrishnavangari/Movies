package sai.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import sai.com.popularmovies.Model.Movies.results;

/**
 * Created by krrish on 2/11/2016.
 */

public class MovieDetail extends AppCompatActivity {
    private results movieObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (findViewById(R.id.fragment_movieDetail_container) != null) {
            if (savedInstanceState != null) {
                movieObject=savedInstanceState.getParcelable("movieObject");
            }
            else
            {
                Intent intent=getIntent();
                movieObject= intent.getParcelableExtra("movieObject");
                String movie_type=intent.getStringExtra("movie_type") ;
                switch (movie_type) {
                    case "top_rated":
                        setTitle("Top Rated Movies");
                        break;
                    default:
                        setTitle("Popular Movies");
                }
            }
            // Add the fragment to the 'fragment_movieDetail_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_movieDetail_container, new MovieDetailFragment()).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movieObject",movieObject);
        super.onSaveInstanceState(outState);
    }

    public results getMovieObject() {

        return movieObject;
    }
}
