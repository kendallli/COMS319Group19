package com.example.mymediaplayer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * CPRE 388 - Labs
 * 
 * Copyright 2013
 */

public class MyMediaPlayerActivity extends Activity {

	/**
	 * Other view elements
	 */
	private TextView songTitleLabel;
	private int result;
	private final boolean Shuffle_Disable = false;
	private final boolean Shuffle_Enable = true;
	private final boolean SoloLoop_Enable = true;
	private final boolean SoloLoop_Disable = false;
	boolean shuffle;
	boolean soloLoop;
	boolean playinghasStarted=false;
	/**
	 *  media player:
	 *  http://developer.android.com/reference/android/media/MediaPlayer.html 
	 */
	private static MediaPlayer mp;

	/**
	 * Index of the current song being played
	 */
	private int currentSongIndex = 0;

	
	Resources res;

	SharedPreferences prefs;
	
	/**
	 * List of Sounds that can be played in the form of SongObjects
	 */
	private static ArrayList<SongObject> songsList = new ArrayList<SongObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player_main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this); 
		res = getResources();
		songTitleLabel = (TextView) findViewById(R.id.songTitle);

        
		// Initialize the media player
		mp = new MediaPlayer();

		// Getting all songs in a list
		populateSongsList();

		// By default play first song if there is one in the list
		if(!playinghasStarted)
			playSong(0,Shuffle_Enable,SoloLoop_Enable);
		//mp.pause();
		Button backbutton=((Button)findViewById(R.id.backbutton));
		backbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				playSong(currentSongIndex-1,Shuffle_Enable,SoloLoop_Enable);
				
			}
		});
		
		final Button playpausebutton=((Button)findViewById(R.id.playpausebutton));
		playpausebutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(mp.isPlaying()){
					mp.pause();
					playpausebutton.setBackgroundResource(R.drawable.btn_play);
				}
				else {
					if(playinghasStarted){
						mp.start();
						playpausebutton.setBackgroundResource(R.drawable.btn_pause);
					}else playSong(currentSongIndex,Shuffle_Enable,SoloLoop_Enable);
				}
				
			}
		});
		
		Button forwardbutton=((Button)findViewById(R.id.forwardbutton));
		forwardbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				playSong(currentSongIndex+1,Shuffle_Enable,SoloLoop_Enable);
				
			}
		});
		
		mp.setOnCompletionListener(new OnCompletionListener(){
			public void onCompletion (MediaPlayer mp){
				
				playSong(currentSongIndex+1,Shuffle_Enable,SoloLoop_Enable);
			}
		});
		
		
		// Setup preferences and resources
		
	}
	
	public static void setVolume(int leftVolume, int rightVolume){
		Log.i("i", "set volume "+leftVolume);
		mp.setVolume((float)leftVolume/100, (float)rightVolume/100);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_player_menu, menu);
		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_choose_song:
			// Open SongList to display a list of audio files to play
			//TODO
			Intent intent = new Intent(this, SongList.class);
			startActivityForResult(intent,result);


			return true;
		case R.id.menu_preferences:
			// Display Settings page
			//TODO
			Intent intentForSettingsPage = new Intent(this, MediaPreferences.class);
			startActivity(intentForSettingsPage);


			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	/**
	 * Helper function to play a song at a specific index of songsList
	 * @param songIndex - index of song to be played
	 */
	@SuppressLint("NewApi")
	public void  playSong(int songIndex,boolean shuffleEnable, boolean soloLoopEnable){
		//System.out.print("shuffle = "+shuffle);
		
		playinghasStarted=true;
		if(shuffleEnable){
		shuffle = prefs.getBoolean(res.getString(R.string.mp_shuffle_pref), false);;
		//boolean loopPlay = prefs.getBoolean(res.getString(R.string.loopPlay), false);;
		}else shuffle = false;
		if(soloLoopEnable){
			soloLoop = prefs.getBoolean(res.getString(R.string.loopPlay), false);;
			}else soloLoop = false;
		if(shuffle){
			Random r = new Random();
			int temp=r.nextInt(songsList.size()-1);
			while(temp==songIndex)
				temp=r.nextInt(songsList.size()-1);

			songIndex = temp;
		}
		if(soloLoop){
			songIndex=currentSongIndex; 
		}
		Log.i("i", "playing+"+songIndex);
		//mp.setVolume(leftVolume, rightVolume);
		// Play song if index is within the songsList
		if (songIndex < songsList.size() && songIndex >= 0) {
			try {
				mp.stop();
				mp.reset();
				mp.setDataSource(songsList.get(songIndex).getFilePath());
				
				mp.prepare();
				
					
				mp.start();
				// Displaying Song title
				String songTitle = songsList.get(songIndex).getTitle();
				songTitleLabel.setText(songTitle);

				
				// Changing Button Image to pause image
				((Button)findViewById(R.id.playpausebutton)).setBackgroundResource(R.drawable.btn_pause);

				// Update song index
				currentSongIndex = songIndex;

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} else if (songsList.size() > 0) {
			playSong(0,Shuffle_Enable,SoloLoop_Enable);
		}
	}


	/** 
	 * Get list of info for all sounds to be played
	 */
	public void populateSongsList(){
		//TODO add all songs from audio content URI to this.songsList
		
		// Get a Cursor object from the content URI 
		String mSelectionClause = MediaStore.Audio.Media.IS_MUSIC + " = 1";
		String[] mProjection = {MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA};
		String[] mSelectionArgs = null;
		String mSortOrder = null;
		// Queries the External storage audio files and returns results
		Cursor mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // The content URI of Audio files
		mProjection,
		mSelectionClause,
		mSelectionArgs,
		mSortOrder );
		// The list of columns to return for each row
		// Selection criteria
		// Arguments of the Selection criteria
		// The sort order for the returned rows

		// Use the cursor to loop through the results and add them to 
		//		the songsList as SongObjects
		mCursor.moveToFirst();
		SongObject songObject;
		while(mCursor.moveToNext()){
			songObject = new SongObject("","");
			songObject.setTitle(mCursor.getString(0));

			songObject.setFilePath(mCursor.getString(1));
			songsList.add(songObject);
		}
	
	}

	/**
	 * Get song list for display in ListView
	 * @return list of Songs 
	 */
	public static ArrayList<SongObject> getSongsList(){
		return songsList;
	}
	
	@Override
	protected void onActivityResult(int requestCode,int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      // See if the requestCode matches the SongList requestCode you used
	      // Then play the song that is returned by the SongList activity
	      if(requestCode == result)
	    	  playSong(data.getIntExtra("songIndex", 0),Shuffle_Disable,SoloLoop_Disable);
	}

}
