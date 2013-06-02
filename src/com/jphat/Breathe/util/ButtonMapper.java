package com.jphat.Breathe.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.PorterDuff;
import android.view.View;
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

	public void createButtonsFromDirectory( AssetManager assetManager, Context context ) throws IOException {

		List<Button> buttonList = null;
		Button topButton = null;
		Button subButton = null;
		int count = 1;
		
		String[] topLevelNames = assetManager.list( BUTTON_DIR );
		String[] subNames = null;
		String path = null;
		
		for( String topName : topLevelNames ) { 
			topButton = createTopLevelButton( context, cleanName( topName ) );
			topButton.setVisibility(View.VISIBLE);
			topButton.setId(count++);
			topButton.setOnClickListener(new TopButtonListener());

			path = BUTTON_DIR+"/"+topName;
			subNames = assetManager.list( path );
			buttonList = new ArrayList<Button>();
			
			for( String subName : subNames ) {
				subButton = createButton( context, cleanSubName( subName ));
				subButton.setId( count++ );
				subButton.setVisibility( View.GONE );
				subButton.setOnClickListener(new SubButtonListener( assetManager ));
			    
			    buttonToAsset.put( subButton, path+"/"+subName );
				buttonList.add( subButton );
			}
			buttonMap.put( topButton, buttonList );
		}
	
	}
	private Button createTopLevelButton( Context context, String text ) {
		Button button = createButton( context, text );
//		button.setTextColor(#0000FF);
		button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		return button;
	}
	private Button createButton( Context context, String text ) {
		Button butt = new Button( context );
		butt.setText( text );
	    butt.setLayoutParams(new LayoutParams(
	            LinearLayout.LayoutParams.MATCH_PARENT,
	            LinearLayout.LayoutParams.WRAP_CONTENT));
//		butt.setTextColor(0xFFFFFF);
		butt.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);

		layout.addView( butt );
	    return butt;
	}
	public static String cleanName( String name ) {
		StringBuilder cleaned = new StringBuilder();
		String[] parts = name.split("_");
		int i = Character.isDigit( parts[0].charAt(0) )  ? 1 : 0;
		for( ; i < parts.length; i++ ) {
			String part = parts[i];
			char first = Character.toUpperCase( part.charAt( 0 ));
			char[] chars = part.toCharArray();
			chars[0] = first;
			cleaned.append( chars )
				.append( " " );
		}
		return cleaned.toString();
	}
	
	public static String cleanSubName( String name ) {
		String almostClean = cleanName( name );
		int dotIndex = almostClean.indexOf('.');
		return dotIndex == -1 ? almostClean 
				: almostClean.substring(0, dotIndex);
	}
	
	public void toggleTopLevelButton( Button topLevel ) {
		List<Button> subButtons = buttonMap.get( topLevel );
		if( subButtons.get(0).getVisibility() == View.VISIBLE ) {
			for( Button sub : subButtons ) {
				sub.setVisibility( View.GONE );
			}			
		}else {
			for( Button sub : subButtons ) {
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
		private final AssetManager assetMan;
		
		public SubButtonListener( AssetManager assetMan ) {
			this.assetMan = assetMan;
		}
		
		@Override
		public void onClick(View view) {
			Button clicked = (Button) view;
			String fileString = buttonToAsset.get( clicked );
			AssetFileDescriptor afd = null;
			try {
				afd = assetMan.openFd( fileString );
			} catch (IOException e) {
				e.printStackTrace();
			}
			buttonPlayer.playFile( afd );
		}
	}
}
