package sai.com.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by krrish on 2/11/2016.
 */

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (findViewById(R.id.fragment_movieDetail_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_movieDetail_container, new MovieDetailFragment()).commit();
        }
    }

}
