package com.rj.pixelesque;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.rj.pixelesque.NumberPicker.OnChangedListener;

public class Dialogs {

	public static void showNewDialog(final PixelArtEditor p) {
		Log.d("PixelArt", "alertz shownew");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(p);
		builder.setTitle("Pick a size");
		LinearLayout layout = new LinearLayout(p);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layout.setGravity(Gravity.CENTER);
		final NumberPicker pickerx = new NumberPicker(p);
		final TextView text = new TextView(p);
		final NumberPicker pickery = new NumberPicker(p);
		pickerx.setRange(1, 50);
		pickery.setRange(1, 50);
		pickerx.setCurrent(p.art.width);
		pickery.setCurrent(p.art.height);
		text.setText("x");
		text.setTextSize(30);
		layout.addView(pickerx);
		layout.addView(text);
		layout.addView(pickery);
		
		builder.setView(layout);
		
		builder.setPositiveButton("New", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				int x = pickerx.getCurrent();
				int y = pickery.getCurrent();
				p.newArt(x, y);
			}
			}); 
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				dialog.dismiss();
			}
			}); 

		AlertDialog alert = builder.create();
		alert.show();
		
		Log.d("PixelArt", "alertz shownew");
	}
	
	
	public static void showSaveAs(final PixelArtEditor p) {
		Log.d("PixelArt", "alertz showsaveas");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(p);
		builder.setTitle("Save as");
		final EditText edit = new EditText(p);
		edit.setHint("Pick a name");
		builder.setView(edit);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				p.save(edit.getText().toString());
			}
			}); 
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				dialog.dismiss();
			}
			}); 

		AlertDialog alert = builder.create();
		alert.show();
		
		Log.d("PixelArt", "alertz shownew");
	}
	
	
	public static void showExport(final PixelArtEditor p) {
		Log.d("PixelArt", "alertz showexport");
		int[] lowxy = getResize(p.art.width, p.art.height, PixelArtEditor.EXPORT_SMALL_LONGSIDE);
		int[] medxy = getResize(p.art.width, p.art.height, PixelArtEditor.EXPORT_MEDIUM_LONGSIDE);
		int[] highxy = getResize(p.art.width, p.art.height, PixelArtEditor.EXPORT_LARGE_LONGSIDE);
		final String[] qualities = { 
				"Low ("+lowxy[0]+"x"+lowxy[1]+")", //Low (320x240) 
				"Medium ("+medxy[0]+"x"+medxy[1]+")", 
				"High ("+highxy[0]+"x"+highxy[1]+")",
				};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(p);
		builder.setTitle("Export");
		builder.setItems(qualities, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0)
					p.export(PixelArtEditor.EXPORT_SMALL_LONGSIDE, "small");
				else if (which == 1) 
					p.export(PixelArtEditor.EXPORT_MEDIUM_LONGSIDE, "medium");
				else
					p.export(PixelArtEditor.EXPORT_LARGE_LONGSIDE, "large");
			}
		});
		builder.setPositiveButton("Other", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				showExportCustom(p);
			}
			}); 
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				dialog.dismiss();
			}
			}); 

		AlertDialog alert = builder.create();
		alert.show();
		
		Log.d("PixelArt", "alertz shownew");
	}
	
	
	static int[] getResize(int width, int height, int longside) {
		int[] xy = new int[2];
		if (width > height) {
			xy[0] = longside;
			xy[1] = (height*longside)/width;
		} else {
			xy[0] = (width*longside)/height;
			xy[1] = longside;
		}
		return xy;
	}
	
	
	
	public static void showExportCustom(final PixelArtEditor p) {		
		AlertDialog.Builder builder = new AlertDialog.Builder(p);
		builder.setTitle("Pick a size to export to");
		LinearLayout layout = new LinearLayout(p);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layout.setGravity(Gravity.CENTER);
		final NumberPicker pickerx = new NumberPicker(p);
		final TextView text = new TextView(p);
		final NumberPicker pickery = new NumberPicker(p);
		pickerx.setRange(1, 2000);
		pickery.setRange(1, 2000);
		pickerx.setCurrent(p.art.width*60);
		pickery.setCurrent(p.art.height*60);
		pickerx.setOnChangeListener(new OnChangedListener() {
			@Override
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				int y = (newVal * p.art.height) / p.art.width;
				pickery.setCurrent(y);
			}
		});
		pickery.setOnChangeListener(new OnChangedListener() {
			@Override
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				int x = (newVal * p.art.width) / p.art.height;
				pickerx.setCurrent(x);
			}
		});
		text.setText("x");
		text.setTextSize(30);
		layout.addView(pickerx);
		layout.addView(text);
		layout.addView(pickery);
		
		builder.setView(layout);
		
		builder.setPositiveButton("Export", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {
				int x = pickerx.getCurrent();
				int y = pickery.getCurrent();
				int max = Math.max(x,y);
				p.export(max, ""+x+"x"+y );
			}
			}); 
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				dialog.dismiss();
			}
			}); 

		AlertDialog alert = builder.create();
		alert.show();
		
	}


	
	
}
