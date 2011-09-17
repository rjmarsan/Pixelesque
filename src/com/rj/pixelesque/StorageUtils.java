package com.rj.pixelesque;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import com.rj.pixelesque.ArtListFragment.ArtElement;


public class StorageUtils {

	public static final String PREFS_NAME = "files_prefs";
	public static final String RECENTLY_OPENED = "recent";
	
	public static File getExportDirectory(Context context) {
		File exportloc;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)){
            File sdCard = Environment.getExternalStorageDirectory();
            exportloc = new File (sdCard.getAbsolutePath(), "pixelesque");
            exportloc.mkdirs();
        }else{
            exportloc = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (exportloc == null) {
                exportloc = context.getFilesDir();
            }
        }
        if (exportloc != null) exportloc.mkdirs();
        return exportloc;
	}
	
	public static File getSaveDirectory(Context context) {
		File location = new File(context.getFilesDir(), "saves");
		location.mkdirs();
		return location;
	}
	
	
	public static ArrayList<ArtElement> getSavedFiles(Context context) {
		ArrayList<ArtElement> elements = new ArrayList<ArtElement>();
		
		File folder = getSaveDirectory(context);
		for (File file : folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				if (filename.toLowerCase().endsWith(".png"))
					return true;
				return false;
			}
			})) {
			
			ArtElement e = new ArtElement(file, file.getName().substring(0, file.getName().length()-4));
			elements.add(e);
			Log.d("ArtList", "Adding list item: "+file.getAbsolutePath());
			
		}
		
		return elements;
	}
	
	public static void saveFile(String name, File file, Bitmap image, Context context, boolean addToRecent) {
		try {
			Log.d("SaveTask", "Saving: "+file.getAbsolutePath());
			FileOutputStream fios = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fios);
			image.compress(CompressFormat.PNG, 100, bos);
			bos.flush();
			Log.d("SaveTask", "saved: "+file.getAbsolutePath());
			if (addToRecent) setLastOpened(file.getAbsolutePath(), context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Bitmap loadFile(PApplet p, String path, Context context, boolean addToRecent) {
		if (addToRecent) setLastOpened(path, context);
//		return p.loadImage(path);
		return BitmapFactory.decodeFile(path);
	}
	
	
	public static void setLastOpened(String path, Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(RECENTLY_OPENED, path);
		edit.commit();
	}

//	public static PImage getLastOpened(PApplet p, Context context) {
//		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
//		if (! prefs.contains(RECENTLY_OPENED)) 
//			return null;
//		
//		String path = prefs.getString(RECENTLY_OPENED, "");
//		PImage image = loadFile(p, path, context, false);
//		
//		return image;
//	}
	
	public static String getLastOpenedFile(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		if (! prefs.contains(RECENTLY_OPENED)) 
			return null;
		
		String path = prefs.getString(RECENTLY_OPENED, "");
		return path;
	}

}