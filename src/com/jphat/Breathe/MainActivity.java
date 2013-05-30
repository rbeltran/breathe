package com.jphat.Breathe;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jphat.Breathe.util.ButtonMapper;
import com.jphat.Breathe.util.ButtonPlayer;

public class MainActivity extends Activity {

	List<Button> basicCourtesyButtons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout linearLayout = new LinearLayout( this );
		ScrollView scrollView = new ScrollView( this );
		scrollView.addView( linearLayout );
		
		// Set the volume control buttons to use the music stream which
		// my app is using
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ButtonMapper bMapper = new ButtonMapper( linearLayout, new ButtonPlayer(this));
		try {
			linearLayout = bMapper.createButtonsFromDirectory( getAssets(), getApplicationContext());
			setContentView( scrollView );

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
}
