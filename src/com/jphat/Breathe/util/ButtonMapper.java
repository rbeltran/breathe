package com.jphat.Breathe.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonMapper {

	private final static String BUTTON_DIR = "button_map";
	private Map<Button, List<Button>> buttonMap = null;
	private Map<Button,String> buttonToAsset = null;
	private final ButtonPlayer buttonPlayer;
	private final LinearLayout layout;
	
	public ButtonMapper( LinearLayout layout, ButtonPlayer bPlayer ) {
		this.buttonPlayer = bPlayer;
		buttonMap = new LinkedHashMap<Button,List<Button>>();
		buttonToAsset = new HashMap<Button,String>();
		this.layout = layout;
	}

	public LinearLayout createButtonsFromDirectory( AssetManager assetManager, Context context ) throws IOException {

		List<Button> buttonList = null;
		Button topButton = null;
		Button subButton = null;
		int count = 1;
		
		String[] topLevelNames = assetManager.list( BUTTON_DIR );
		String[] subNames = null;
		String path = null;
		
		for( String topName : topLevelNames ) { 
			topButton = new Button( context );
			topButton.setText(topName);
			topButton.setVisibility(View.VISIBLE);
			topButton.setId(count++);
			topButton.setOnClickListener(new TopButtonListener());
		    topButton.setLayoutParams(new LayoutParams(
		            ViewGroup.LayoutParams.WRAP_CONTENT,
		                ViewGroup.LayoutParams.WRAP_CONTENT));
		    layout.addView(topButton);

			path = BUTTON_DIR+"/"+topName;
			subNames = assetManager.list( path );
			buttonList = new ArrayList<Button>();
			
			for( String subName : subNames ) {
				subButton = new Button( context );
				subButton.setText( subName );
				subButton.setId( count++ );
				subButton.setVisibility( View.GONE );
				subButton.setOnClickListener(new SubButtonListener());
			    subButton.setLayoutParams(new LayoutParams(
			            ViewGroup.LayoutParams.WRAP_CONTENT,
			                ViewGroup.LayoutParams.WRAP_CONTENT));
			    layout.addView(subButton);

			    buttonToAsset.put( subButton, path+"/"+subName );
				buttonList.add( subButton );
			}
			buttonMap.put( topButton, buttonList );
		}
	
		return layout;
	}
	
	
	public void toggleTopLevelButton( Button topLevel ) {
		boolean buttonsVisible = topLevel.getVisibility() != View.VISIBLE;
		List<Button> subButtons = buttonMap.get( topLevel );
		for( Button sub : subButtons ) {
			if( buttonsVisible ) {
				sub.setVisibility( View.GONE );
			} else {
				sub.setVisibility( View.VISIBLE );
			}
		}
			
	}
	
	class TopButtonListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
				toggleTopLevelButton( (Button) view);
		}
	}
	class SubButtonListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			Uri.Builder uriBuilder = new Uri.Builder();
			Button clicked = (Button) view;
			String uriString = buttonToAsset.get( clicked );
//			uriBuilder.encodedPath(uriString);
			buttonPlayer.playFile( new File( uriString ));
		}
	}
}
