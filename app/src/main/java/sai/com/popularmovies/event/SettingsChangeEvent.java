package sai.com.popularmovies.event;

/**
 * Created by krrish on 5/11/2016.
 */

public class SettingsChangeEvent {

    private String movie_type;
    public SettingsChangeEvent( String movie_type) {
        movie_type=movie_type;
    }

    public String getMovie_type() {
        return movie_type;
    }
}
