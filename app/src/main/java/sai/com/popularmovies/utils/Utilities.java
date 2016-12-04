package sai.com.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sai.com.popularmovies.MainActivity;
import sai.com.popularmovies.R;

/**
 * Created by krrish on 2/12/2016.
 */

public class Utilities {

    public static String getPreferredMovieType(Context context){
        SharedPreferences sharedPrefernces = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefernces.getString(
                context.getString(R.string.pref_movie_type_key),
                context.getString(R.string.pref_movie_type_default));
    }

    public static RestApi getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RestApi.class);
    }
}
