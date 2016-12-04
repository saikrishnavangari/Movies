package sai.com.popularmovies.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sai.com.popularmovies.Model.Movies;
import sai.com.popularmovies.Model.Reviews;
import sai.com.popularmovies.Model.Videos;
import sai.com.popularmovies.MovieDetail;

/**
 * Created by krrish on 1/11/2016.
 */

public interface RestApi {

    @GET("movie/{movie_type}")
    Call<Movies> getPopularMovies(
            @Path("movie_type") String movie_type,
            @Query("api_key") String api_key,
            @Query("language") String language);

    @GET("movie/{movie_id}/reviews")
    Call<Reviews> getReviews(
            @Path(MovieDetail.Movie_ID) int movie_id,
            @Query("api_key") String api_key
    );
    @GET ("movie/{movie_id}/videos")
    Call<Videos> getVideos(
            @Path(MovieDetail.Movie_ID) int movie_id,
            @Query("api_key") String api_key
    );
}
