package com.jphat.Breathe;

import java.util.ArrayList;
import java.util.List;

import com.jphat.Breathe.util.RawEnum;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private static OnAudioFocusChangeListener afChangeListener = null;
	private static MediaPlayer mPlayer = null;	
	List<Button> basicCourtesyButtons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set the volume control buttons to use the music stream which
		// my app is using
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

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
		
		List<Button> buttonList = ButtonMapper.createButtonListFromDirectory( getAssets(), getApplicationContext());
//		for(Button button: basicCourtesyButtons ) {
//			button.setVisibility(View.INVISIBLE);
//		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	public void toggleBasicCourtesy( View view ) {
		Button goodMorningButton = (Button)findViewById(R.id.button_good_morning);
		boolean buttonsVisible = goodMorningButton.getVisibility() == View.VISIBLE;

				for( Button bcButton: basicCourtesyButtons ) {
			if( buttonsVisible ) {
				bcButton.setVisibility( View.GONE );
			} else {
				bcButton.setVisibility( View.VISIBLE );
			}
		}
			
	}

	public void playFile( View view ) {
		getAudioAccess();
		String rawEnum = (String)view.getTag();
		mPlayer = MediaPlayer.create(getBaseContext(), RawEnum.valueOf(rawEnum).getValue() );
		mPlayer.start();
		mPlayer.setOnCompletionListener(  new OnCompletionListener() {
			public void onCompletion( MediaPlayer player ) {
				player.release();
			}
		});
	}
	
	public void getAudioAccess() {
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
