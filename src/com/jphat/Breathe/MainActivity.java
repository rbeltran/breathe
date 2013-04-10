package com.jphat.Breathe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	OnAudioFocusChangeListener afChangeListener = null;
	MediaPlayer mPlayer;
	
	List<Button> basicCourtesyButtons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set the volume control buttons to use the music stream which
		// my app is using
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mPlayer = new MediaPlayer();

		basicCourtesyButtons = new ArrayList<Button>();
		basicCourtesyButtons.add((Button)findViewById(R.id.button_good_morning));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_good_afternoon));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_good_evening));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_im_sorry));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_excuse_me));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_thank_you));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_thank_patience));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_please));
		basicCourtesyButtons.add((Button)findViewById(R.id.button_good_day));
		
		for(Button button: basicCourtesyButtons ) {
			button.setVisibility(View.INVISIBLE);
		}
		
//		afChangeListener = new OnAudioFocusChangeListener() {
//		    public void onAudioFocusChange(int focusChange) {
//		        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ) {
//		            // Pause playback
//		        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
//		            // Resume playback 
//		        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
//		            am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
//		            am.abandonAudioFocus(afChangeListener);
//		            // Stop playback
//		        }
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onStop() {
		mPlayer.release();
		mPlayer = null;
	}
	@Override
	public void onPause() {
		mPlayer.release();
		mPlayer = null;
	}
	
	public void toggleBasicCourtesy( View view ) {
		Button goodMorningButton = (Button)findViewById(R.id.button_good_morning);
		boolean buttonsVisible = goodMorningButton.getVisibility() == View.VISIBLE;

				for( Button bcButton: basicCourtesyButtons ) {
			if( buttonsVisible ) {
				bcButton.setVisibility( View.INVISIBLE );
			} else {
				bcButton.setVisibility( View.VISIBLE );
			}
		}
			
	}

	public void playGoodMorning( View view ) {
		mPlayer.reset();
		playSound();

		try {
			AssetFileDescriptor fd = getAssets().openFd("GoodMorning.mp3");
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setDataSource(fd.getFileDescriptor());
			fd.close();
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playSound() {
		AudioManager am = (AudioManager) getSystemService( Context.AUDIO_SERVICE );
		int result = am.requestAudioFocus( 
				afChangeListener, 
				AudioManager.STREAM_MUSIC, 
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK );
		if( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ) {
//			am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
		}
	}
}
