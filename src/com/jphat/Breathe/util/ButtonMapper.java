package com.jphat.Breathe.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ButtonMapper {
	private final static String SERIALIZED_FILE = "buttonInfoMap.ser";
	private final static String BUTTON_DIR = "button_map";
	private Map<Button, List<Button>> buttonMap = null;
	private Map<Button,String> buttonToAsset = null;
	private final ButtonPlayer buttonPlayer;
	
	private List<String> topLevelNamesRaw;
	private Map<String,List<String>> topNamesToSubNameMap;
	
	public ButtonMapper( LinearLayout layout, ButtonPlayer bPlayer ) {
		this.buttonPlayer = bPlayer;
		buttonMap = new LinkedHashMap<Button,List<Button>>();
		buttonToAsset = new HashMap<Button,String>();
		topLevelNamesRaw = new ArrayList<String>();
		topNamesToSubNameMap = new HashMap<String,List<String>>();
	}

	/**
	 * 
	 * Get the buttons in an ordered fashion
	 * 
	 * @return
	 */
	public List<Button> getAllButtons() {
		List<Button> allButtons = new ArrayList<Button>();
		for( Button topButton: buttonMap.keySet() ) {
			allButtons.add( topButton );
			allButtons.addAll( buttonMap.get( topButton ));
		}
		return allButtons;
	}
	
	public Map<String,List<ButtonInfo>> createRawFileMap( AssetManager assetManager ) throws IOException {
		String[] topLevelNames = assetManager.list( BUTTON_DIR );
		String[] subNames;
		String path;
		
		Map<String,List<ButtonInfo>> orderedButtonMap = new LinkedHashMap<String,List<ButtonInfo>>();
		List<ButtonInfo> subButtonList;
		
		ButtonInfo buttonInfo;
		
		for( String topName : topLevelNames ) {
			path = BUTTON_DIR+"/"+topName;
			subNames = assetManager.list( path );
			subButtonList = new ArrayList<ButtonInfo>();
					
			for( String subName : subNames ) {
				buttonInfo = new ButtonInfo();
				buttonInfo.setCleanName(cleanSubName(subName));
				buttonInfo.setPath(path+"/"+subName);
				subButtonList.add( buttonInfo );
			}
			orderedButtonMap.put( topName, subButtonList );
		}
		return orderedButtonMap;
	}
	
	public void createButtons( AssetManager assetManager, Context context ) throws IOException, ClassNotFoundException {
		Map<String, List<ButtonInfo>> buttonInfoMap;
		
		if( buttonMapCached( context ) ) {
			buttonInfoMap = deserializeButtonInfoMap(context);
		} else {
			buttonInfoMap = createRawFileMap( assetManager );
			serializeButtonInfoMap( buttonInfoMap, context );
		}
		createButtonsFromButtonInfoMap( buttonInfoMap, context, assetManager);
	}
	
	private void serializeButtonInfoMap(Map<String,List<ButtonInfo>> buttonInfoMap, Context context ) throws IOException {
		FileOutputStream fos = context.openFileOutput(SERIALIZED_FILE, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(buttonInfoMap);
		os.close();
	}
	
	private Map<String, List<ButtonInfo>> deserializeButtonInfoMap( Context context ) throws IOException, ClassNotFoundException {
		FileInputStream fis = context.openFileInput(SERIALIZED_FILE);
		ObjectInputStream is = new ObjectInputStream(fis);
		Map<String, List<ButtonInfo>> buttonInfoMap = (Map<String,List<ButtonInfo>>) is.readObject();
		is.close();
		return buttonInfoMap;
	}
	
	private boolean buttonMapCached(Context context) {
		File file = context.getFileStreamPath(SERIALIZED_FILE);
		return file.exists();
	}
	
	public void createButtonsFromButtonInfoMap( Map<String, List<ButtonInfo>> infoMap, Context context, AssetManager assetManager) {
		
		List<Button> buttonList = null;

		Button topButton;
		Button subButton;
		int count = 1;
		
		
		for( String topName : infoMap.keySet() ) {
			topButton = createTopLevelButton( context, cleanName( topName ) );
			topButton.setVisibility(View.VISIBLE);
			topButton.setOnClickListener( new TopButtonListener() );
			topButton.setId(count++);
			
			buttonList = new ArrayList<Button>();

			for( ButtonInfo info: infoMap.get(topName)) {
				subButton = createButton( context, info.getCleanName());
				subButton.setId( count++ );
				subButton.setVisibility( View.GONE );
				subButton.setOnClickListener(new SubButtonListener( assetManager ));

			    buttonToAsset.put( subButton, info.getPath() );
				buttonList.add( subButton );
			}
			buttonMap.put( topButton, buttonList );
		}
		
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
		button.setTextColor(Color.WHITE);
		int[] gColors = { /* 0xFF5656C0, */ 0xFF3030C0, 0xFF000080 };
		
		button.setBackgroundDrawable(createGradient( gColors ));
		return button;
	}
	private Button createButton( Context context, String text ) {
		Button butt = new Button( context );
		butt.setText( text );
		butt.setTextColor(Color.WHITE);
		LayoutParams buttonParams = new LayoutParams(
	            LinearLayout.LayoutParams.MATCH_PARENT,
	            LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonParams.setMargins(5, 3, 5, 2);
	    butt.setLayoutParams(buttonParams);
	    int[] gColors = { /* 0xFFDBBD63, */ 0xFFDBB237, 0xFFB78900  };
	    butt.setBackgroundDrawable(createGradient( gColors ));
	    return butt;
	}
	
	private GradientDrawable createGradient( int[] gradientColors ) {
		
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, 
				gradientColors );
		gd.setCornerRadius(5f);
		gd.setShape(GradientDrawable.RECTANGLE);
		gd.setStroke(2, gradientColors[0] );
		return gd;
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
