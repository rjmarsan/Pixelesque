package com.rj.pixelesque;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class SaveTask extends AsyncTask<Void, Void, Void> {
	String name;
	int width, height;
	PixelData data;
	ProgressDialog dialog;
	File location;
	File file;
	boolean export;
	PixelArt context;
	
	public SaveTask(String name, int width, int height, PixelData data, PixelArt context) {
		this(name, width, height, data, context, null, false);			
	}
	public SaveTask(String name, int width, int height, PixelData data, PixelArt context, File location, boolean export) {
		this.name = name; 
		this.width = width; 
		this.height = height; 
		this.data = data; 
		this.export = export;
		this.context = context;
		this.location = location;
		if (location == null) {
			this.location = StorageUtils.getSaveDirectory(context);
		}
		if (name == null) {
			String time = System.currentTimeMillis()+"";
			name = "Pixelesque-"+time.substring(time.length()-5);
		}
		this.location.mkdirs();
		file = new File(this.location, name+".png");
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(context, "Saving...", "Just a moment");
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			Bitmap image;
			if (width < 0 && height < 0)
				image = data.render(context);
			else {
				if (width < 0)
					width = (height * data.width) / data.height;
				if (height < 0) 
					height = (width * data.height) / data.width;
				image = data.render(context, width, height);
			}
			
			StorageUtils.saveFile(name, file, image, context);
			
			if (export) {
				new MediaScanTask().execute();
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		}				
		return null;

	}
	
	public class MediaScanTask extends AsyncTask<String, Void, Void> {		
		@Override
		protected Void doInBackground(String... params) {
			// Tell the media scanner about the new file so that it is
	        // immediately available to the user.
	        MediaScannerConnection.scanFile(context,
	                new String[] { file.toString() }, null,
	                new MediaScannerConnection.OnScanCompletedListener() {
	            public void onScanCompleted(String path, Uri uri) {
	                Log.i("ExternalStorage", "Scanned " + path + ":");
	                Log.i("ExternalStorage", "-> uri=" + uri);
	            }
	        });
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		dialog.dismiss();
		if (export) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "image/*");

			// tells your intent to get the contents
			// opens the URI for your image directory on your sdcard
			context.startActivityForResult(intent, 1);
		} else {
			context.artChangedName();
		}
	}
	
}