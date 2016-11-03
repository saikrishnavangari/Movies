package sai.com.popularmovies;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by krrish on 2/11/2016.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
