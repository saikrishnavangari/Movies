package sai.com.popularmovies;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sai.com.popularmovies.Model.Movies;
import sai.com.popularmovies.Model.Reviews;
import sai.com.popularmovies.Model.Videos;
import sai.com.popularmovies.data.MoviesColumns;
import sai.com.popularmovies.data.MoviesProvider;
import sai.com.popularmovies.utils.RestApi;
import sai.com.popularmovies.utils.Utilities;

/**
 * Created by krrish on 2/11/2016.
 */
public class MovieDetailFragment extends Fragment {
    public static String MOVIEOBJECT="mMovieObject";
    private static String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    @BindView(R.id.poster_image)
    ImageView poster_image_IV;
    @BindView(R.id.title)
    TextView title_TV;
    @BindView(R.id.release_date)
    TextView release_date_TV;
    @BindView(R.id.vote_average)
    TextView vote_average_TV;
    @BindView(R.id.movie_overview)
    TextView movie_overview_TV;
    @BindView(R.id.movie_review)
    TextView movie_review;
    @BindView(R.id.trailer1)
    LinearLayout trailer1;
    @BindView(R.id.trailer2)
    LinearLayout trailer2;
    @BindView(R.id.header_text) TextView header_text;
    @BindView(R.id.favourite_button)
    Button favouriteButton;
    private Movies.results mMovieObject;
    private String mVideoKeys[];

    public static MovieDetailFragment newInstance(int index) {
        MovieDetailFragment f = new MovieDetailFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void loadVideos() {
        RestApi restApi = Utilities.getRetrofit();
        Call<Videos> videos = restApi.getVideos(mMovieObject.getId(), MainActivity.API_KEY);
        videos.enqueue(new Callback<Videos>() {
                           @Override
                           public void onResponse(Call<Videos> call, Response<Videos> response) {
                               Videos videoConent = response.body();
                               Log.d(LOG_TAG, "videos: " + response.body());
                               ArrayList<Videos.results> videos_list = videoConent.getResults();
                               int i = 0;
                               mVideoKeys = new String[videos_list.size()];
                               for (Videos.results videoObject : videos_list) {
                                   Log.d(LOG_TAG, videoObject.getType());
                                   if (videoObject.getType().equals("Trailer")) {
                                       mVideoKeys[i] = videoObject.getKey();
                                       Log.d(LOG_TAG, mVideoKeys[i]);
                                       i++;
                                   }
                               }
                               setClicklisteners();

                           }

                           @Override
                           public void onFailure(Call<Videos> call, Throwable t) {
                           }
                       }

        );
    }

    private void setClicklisteners() {
        if (mMovieObject != null) {
            trailer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchIntent(1);
                }
            });
            trailer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mVideoKeys.length > 1 && mVideoKeys[1] != null)
                        launchIntent(2);
                    else
                        Toast.makeText(getActivity(), "no second trailer for this movie", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadReview() {
        RestApi restApi = Utilities.getRetrofit();
        Call<Reviews> reviews = restApi.getReviews(mMovieObject.getId(), MainActivity.API_KEY);
        reviews.enqueue(new Callback<Reviews>() {
                            @Override
                            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                                Reviews reviews = response.body();
                                ArrayList<Reviews.results> review_list = reviews.getResults();
                                if (review_list.size() != 0) {
                                    String mContent = review_list.get(0).getContent();
                                    movie_review.setText("Movie Review" + "\n" + mContent);
                                }
                            }

                            @Override
                            public void onFailure(Call<Reviews> call, Throwable t) {
                            }
                        }

        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootview);

        Bundle arguments=getArguments();
        if (arguments != null) {
            mMovieObject = arguments.getParcelable(MOVIEOBJECT);
            loadReview();
            loadVideos();
            updateView();
        }
        return rootview;
    }


    private void launchIntent(int trailer_num) {

        switch (trailer_num) {
            case 1:
                Intent intent=new Intent
                        (Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mVideoKeys[trailer_num - 1]));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case 2:
                startActivity(new Intent
                        (Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mVideoKeys[trailer_num - 1])));
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       try {
           Cursor cursor = getActivity().getContentResolver().query(MoviesProvider.Movies.withId(mMovieObject.getId()), null,
                   null, null, null);
           if (cursor.getCount() > 0) {
               favouriteButton.setText(R.string.favourite_text_remove);
           }
       }
       catch (Exception e) {
           Log.d(LOG_TAG, getString(R.string.favourite_text_remove));
       }
        }

    private void updateView() {
        Picasso.with(getActivity()).load(MainActivity.IMAGE_BASE_URL + "w342/" +
                mMovieObject.getPoster_path()).into(poster_image_IV);
        title_TV.setText(mMovieObject.getOriginal_title());
        header_text.setText(mMovieObject.getOriginal_title());
        release_date_TV.setText(mMovieObject.getRelease_date());
        vote_average_TV.setText(String.valueOf(mMovieObject.getVote_average()));
        movie_overview_TV.setText(mMovieObject.getOverview());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray("mVideoKeys",mVideoKeys);
        outState.putString("movie_review_text",movie_review.getText().toString());
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.favourite_button)
    void favouriteButtonClicked(Button button){
        if (mMovieObject != null) {
            if (button.getText().equals(getString(R.string.favourite_text))) {
                button.setText(R.string.favourite_text_remove);
                ContentValues values = new ContentValues();
                values.put(MoviesColumns.Column_movieId, mMovieObject.getId());
                values.put(MoviesColumns.Column_TITLE, mMovieObject.getTitle());
                values.put(MoviesColumns.Column_voteCount, mMovieObject.getVote_count());
                values.put(MoviesColumns.Column_posterPath, mMovieObject.getPoster_path());
                values.put(MoviesColumns.Column_overview, mMovieObject.getOverview());
                values.put(MoviesColumns.Column_popularity, mMovieObject.getPopularity());
                values.put(MoviesColumns.Column_voteAverage, mMovieObject.getVote_average());
                values.put(MoviesColumns.Column_language, mMovieObject.getOriginal_language());
                values.put(MoviesColumns.Column_backdropPath, mMovieObject.getBackdrop_path());
                values.put(MoviesColumns.Column_releaseDate, mMovieObject.getRelease_date());
                getActivity().getContentResolver().insert(MoviesProvider.Movies.CONTENT_URI, values);
                Cursor cursor = getActivity().getContentResolver().query(MoviesProvider.Movies.CONTENT_URI, null,
                        null, null, null);
                while (cursor.moveToNext()) {
                    Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex(MoviesColumns.Column_TITLE)));
                }
                cursor.close();
            } else {
                button.setText(R.string.favourite_text);
                long rows = getActivity().getContentResolver().delete(MoviesProvider.Movies.withId(mMovieObject.getId()),
                        null, null);
                if (MainActivity.mTwoPane) {
                    resetviews();
                }
            }
        }
    }

    private void resetviews() {

        poster_image_IV.setImageResource(0);
        title_TV.setText("");
        header_text.setText("");
        release_date_TV.setText("");
        vote_average_TV.setText("");
        movie_overview_TV.setText("");
        movie_review.setText("");
    }


}
