package com.jphat.Breathe.util;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class ButtonPlayer {
	
	private final AudioManager audioManager;

	public ButtonPlayer( Activity activity ) {
		this.audioManager = ( AudioManager ) activity.getSystemService( Context.AUDIO_SERVICE );
	}
	
	public void playFile( AssetFileDescriptor afd ) {
		getAudioAccess();
		MediaPlayer mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource( afd.getFileDescriptor(), 
					afd.getStartOffset(), 
					afd.getLength() );
			afd.close();
			mPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
