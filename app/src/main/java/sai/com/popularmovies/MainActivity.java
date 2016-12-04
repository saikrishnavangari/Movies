package sai.com.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG,"inside oncreate");
       /* if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
            }
            // Add the fragment to the 'fragment_container' FrameLayout
            else {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new MainActivityFragment()).commit();
                Log.d(LOG_TAG, "executing");
            }
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
