package com.jphat.Breathe;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jphat.Breathe.util.ButtonMapper;
import com.jphat.Breathe.util.ButtonPlayer;

public class MainActivity extends Activity {

	List<Button> basicCourtesyButtons;
	
	private static final String TAG = MainActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		long startTime = System.currentTimeMillis();
		super.onCreate(savedInstanceState);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout linearLayout = new LinearLayout( this );
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		ScrollView scrollView = new ScrollView( this );
		scrollView.setBackgroundColor(Color.DKGRAY);


		scrollView.addView( linearLayout );
		
		// Set the volume control buttons to use the music stream which
		// my app is using
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ButtonMapper bMapper = new ButtonMapper( linearLayout, new ButtonPlayer(this));
		try {
//			bMapper.createButtonsFromDirectory( getAssets(), getApplicationContext());
			bMapper.createButtons( getAssets(), getApplicationContext());
			List<Button> allButtons = bMapper.getAllButtons();
			for( Button button: allButtons ) {
				linearLayout.addView( button );
			}
			setContentView( scrollView );

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long totalTime = System.currentTimeMillis() - startTime;
		Log.e(TAG, "Total startTime for app = "+totalTime+"ms");
	}	
	
}
