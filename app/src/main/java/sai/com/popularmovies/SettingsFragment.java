package sai.com.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import sai.com.popularmovies.event.SettingsChangeEvent;

/**
 * Created by krrish on 2/11/2016.
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        Preference movieType=findPreference(getString(R.string.pref_movie_type_key));
        bindPreferenceSummaryToValue(movieType);
        Log.d("setting" ,"yes");
    }
    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        String preferenceString = preferences.getString(preference.getKey(), "");
        onPreferenceChange(preference, preferenceString);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String value=o.toString();
        preference.setSummary(value);
        EventBus.getDefault().post(new SettingsChangeEvent(value));
        return true;
    }


}
