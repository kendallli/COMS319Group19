package com.example.mymediaplayer;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;


public class MediaPreferences extends PreferenceActivity {
	Preference.OnPreferenceChangeListener volumeChangeListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			// Check that the string is an integer
			if (newValue != null && newValue.toString().length() > 0) {
				int leftVolume = (int) Integer.valueOf((String)newValue.toString());
				
				int rightVolume = leftVolume;
				MyMediaPlayerActivity.setVolume(leftVolume, rightVolume);
				Log.i("i", "setVolume");
				return true;
			}
			return false;
		}
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		//getFragmentManager().beginTransaction().replace(android.R.id.content, new MediaPreferencesFragment()) .commit();
		
		this.addPreferencesFromResource(R.xml.media_preferences);
		ListPreference listPref = (ListPreference) findPreference("volume_setting");
		listPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// Check that the string is an integer
				if (newValue != null && newValue.toString().length() > 0) {
					int leftVolume = (int) Integer.valueOf((String)newValue.toString());
					
					int rightVolume = leftVolume;
					MyMediaPlayerActivity.setVolume(leftVolume, rightVolume);
					Log.i("i", "setVolume");
					return true;
				}
				return false;}});
		/**
		addPreferencesFromResource(R.xml.media_preferences);
		Preference ref = getPreferenceScreen().findPreference(
				"numberOfCircles");
		ref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// Check that the string is an integer
				if (newValue != null && newValue.toString().length() > 0) {
					int leftVolume = (int) Integer.valueOf((String)newValue.toString());
					
					int rightVolume = leftVolume;
					MyMediaPlayerActivity.setVolume(leftVolume, rightVolume);
					Log.i("i", "setVolume");
					return true;
				}
				return false;
			}
		});
		**/
		/**
		SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	//Preference ref = (Preference) this.getPreferences(R.xml.media_preferences);
		ref.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener(){
		

			@Override
			public void onSharedPreferenceChanged(SharedPreferences arg0,
					String newValue) {
					int leftVolume = (int) Integer.valueOf((String)newValue.toString());
				
					int rightVolume = leftVolume;
					MyMediaPlayerActivity.setVolume(leftVolume, rightVolume);
					Log.i("i", "setVolume");
				
			}
		});
		***/
        
	}
	

	/** This fragment shows the preferences for the media player */
	public static class MediaPreferencesFragment extends PreferenceFragment {
	      @Override
	      public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	            // Make sure default values are applied.
	            PreferenceManager.setDefaultValues(getActivity(),
	                         R.xml.media_preferences, false);
	            // Load the preferences from an XML resource
	            addPreferencesFromResource(R.xml.media_preferences);

	      }}}