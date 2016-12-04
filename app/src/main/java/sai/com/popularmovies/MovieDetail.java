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
    public static final String Movie_ID = "movie_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Intent intent = getIntent();
            movieObject = intent.getParcelableExtra("movieObject");
            arguments.putParcelable(MovieDetailFragment.MOVIEOBJECT, movieObject);
            String movie_type = intent.getStringExtra("movie_type");

            MovieDetailFragment fragment=new MovieDetailFragment();
            fragment.setArguments(arguments);
            // Add the fragment to the 'fragment_movieDetail_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_movieDetail_container, fragment).commit();
        }
    }


    public results getMovieObject() {
        return movieObject;
    }
}
