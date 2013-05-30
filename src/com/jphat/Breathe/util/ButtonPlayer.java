package com.jphat.Breathe.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

public class ButtonPlayer {
	
	private static MediaPlayer mPlayer = null;	
	private final Context baseContext;
	private final AudioManager audioManager;
	private static final OnAudioFocusChangeListener afChangeListener = new AudioChangeListener();

	public ButtonPlayer( Activity activity ) {
		this.baseContext = activity.getBaseContext();
		this.audioManager = ( AudioManager ) activity.getSystemService( Context.AUDIO_SERVICE );
	}
	
	public void playFile( Uri uri ) {
		getAudioAccess();
		mPlayer = MediaPlayer.create(baseContext, uri );
		mPlayer.start();
		mPlayer.setOnCompletionListener(  new OnCompletionListener() {
			public void onCompletion( MediaPlayer player ) {
				player.release();
			}
		});
	}
	
	public void getAudioAccess() {
		int result = audioManager.requestAudioFocus( 
				new AudioChangeListener(), 
				AudioManager.STREAM_MUSIC, 
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK );
		if( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ) {
//			am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
		}
	}

}

class AudioChangeListener implements OnAudioFocusChangeListener {

	@Override
	public void onAudioFocusChange(int focusChange) {
		
	}
	
}
