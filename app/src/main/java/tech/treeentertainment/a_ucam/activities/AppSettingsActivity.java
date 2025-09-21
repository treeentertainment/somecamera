package tech.treeentertainment.a_ucam.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import tech.treeentertainment.a_ucam.R;

public class AppSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        getPreferenceManager().setSharedPreferencesName("app_settings");
        PreferenceManager.setDefaultValues(this, R.xml.app_settings_preferences, false);
        addPreferencesFromResource(R.xml.app_settings_preferences);
    }
}
