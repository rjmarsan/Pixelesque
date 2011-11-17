package com.rj.pixelesque;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PApplet;
import android.content.Context;
import android.util.Log;

public class ArtExtras {
	public static String filename = "extras.json";
	
	private static File getSaveFile(Context context) {
		File savedir = StorageUtils.getSaveDirectory(context);
		return new File(savedir, filename);
	}
	
	public static void populateExtras(Context context, PApplet p, PixelArt art, String tag) throws Exception {
		JSONObject presets = getExtras(context);
		if (presets == null) {
			Log.d("Presets", "No Presets!");
			return; //nothing to do anymore
		} else if (!presets.has(tag)) {
			Log.d("Presets", "Preset not present: "+tag);
			return;
		}
		JSONObject preset = presets.getJSONObject(tag);
		art.extrasFromJSON(context, p, preset);
	}
	
	private static JSONObject getExtras(Context context) throws Exception {
		File jsonFile = getSaveFile(context);
		if (!jsonFile.exists()) return null;
	    byte[] buffer = new byte[(int) jsonFile.length()];
	    BufferedInputStream f = new BufferedInputStream(new FileInputStream(jsonFile));
	    f.read(buffer);		
		String jsonString = new String(buffer);
		//Log.d("Presets", "Presets:\n" +jsonString);
		return new JSONObject(jsonString);
	}
	
	
	public static void saveExtras(Context context, PixelArt art, String tag) throws Exception {
		File jsonFile = getSaveFile(context);
		JSONObject json = getExtras(context);
		if (json == null) json = new JSONObject();
		JSONObject extra = art.extrasToJSON();
		json.put(tag, extra);
		String out = json.toString(4);
		//Log.d("Presets", "Presets:"+out);
	    BufferedOutputStream f = new BufferedOutputStream(new FileOutputStream(jsonFile));
	    f.write(out.getBytes());
	    f.flush();
	    f.close();

	}
}
