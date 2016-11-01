package sai.com.popularmovies.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sai.com.popularmovies.Model.Movies;

/**
 * Created by krrish on 1/11/2016.
 */

public interface RestApi {

@GET ("movie/popular")
    Call<Movies> getPopularMovies(@Query("api_key") String api_key,
                                  @Query("language") String language);

}
