package sai.com.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import sai.com.popularmovies.Model.Movies;
import sai.com.popularmovies.Model.Movies.results;

/**
 * Created by krrish on 2/11/2016.
 */
public class MovieDetailFragment extends Fragment {
    private static String LOG_TAG=MovieDetailFragment.class.getSimpleName();
    private Movies.results movieObject;
    @BindView(R.id.poster_image) ImageView poster_image_IV;
    @BindView(R.id.title) TextView title_TV;
    @BindView(R.id.release_date) TextView release_date_TV;
    @BindView(R.id.language) TextView languge_TV;
    @BindView(R.id.movie_overview) TextView movie_overview_TV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        results movieObject=((MovieDetail)getActivity()).getMovieObject();
        View rootview=inflater.inflate(R.layout.fragment_movie_detail,container,false);
        ButterKnife.bind(this, rootview);
        Picasso.with(getActivity()).load(MainActivity.IMAGE_BASE_URL+"w342/"+
                movieObject.getPoster_path()).into(poster_image_IV);
        title_TV.setText(movieObject.getOriginal_title());
        release_date_TV.setText(movieObject.getRelease_date());
        languge_TV.setText(movieObject.getOriginal_language());
        movie_overview_TV.setText(movieObject.getOverview());
        return rootview;

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
