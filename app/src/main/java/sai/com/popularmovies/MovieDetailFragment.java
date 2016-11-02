package sai.com.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by krrish on 2/11/2016.
 */
public class MovieDetailFragment extends Fragment {
    private static String LOG_TAG=MovieDetailFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.fragment_movie_detail,container);
        return rootview;
    }
}
