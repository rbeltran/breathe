package com.jphat.Breathe.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.Button;

public class ButtonMapper {

	private final static String BUTTON_DIR = "button_map";
	
	public Map<Button,List<Button>> createButtonsFromDirectory( AssetManager assetManager, Context context ) throws IOException {
		Map<Button,List<Button>> buttonMap = new LinkedHashMap<Button,List<Button>>();
		List<Button> buttonList = null;
		Button topButton = null;
		Button subButton = null;
		int count = 1;
		
		String[] topLevelNames = assetManager.list( BUTTON_DIR );
		String[] subNames = null;
		
		for( String topName : topLevelNames ) { 
			topButton = new Button( context );
			topButton.setText(topName);
			topButton.setVisibility(View.VISIBLE);
			topButton.setId(count++);
			
			subNames = assetManager.list( BUTTON_DIR+"/"+topName );
			buttonList = new ArrayList<Button>();
			for( String subName : subNames ) {
				subButton = new Button( context );
				subButton.setText( subName );
				subButton.setId( count++ );
				subButton.setVisibility( View.GONE );
				buttonList.add( subButton );
			}
			buttonMap.put( topButton, buttonList );
		}
			
		return buttonMap;
	}
	
	public void toggleTopLevelButton( Button topLevel, List<Button> subButtons ) {
		boolean buttonsVisible = topLevel.getVisibility() == View.VISIBLE;

				for( Button sub : subButtons ) {
			if( buttonsVisible ) {
				sub.setVisibility( View.GONE );
			} else {
				sub.setVisibility( View.VISIBLE );
			}
		}
			
	}
	
}
