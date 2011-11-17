package com.rj.pixelesque;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.rj.pixelesque.NumberPicker.OnChangedListener;

public class Dialogs {

	public static void showNewDialog(final PixelArtEditor p) {
		Log.d("PixelArt", "alertz shownew");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(p);
		builder.setTitle(R.string.new_title);
		LinearLayout layout = new LinearLayout(p);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layout.setGravity(Gravity.CENTER);
		final NumberPicker pickerx = new NumberPicker(p);
		final TextView text = new TextView(p);
		final NumberPicker pickery = new NumberPicker(p);
		pickerx.setRange(1, 64);
		pickery.setRange(1, 64);
		pickerx.setCurrent(p.art.width);
		pickery.setCurrent(p.art.height);
		text.setText("x");
		text.setTextSize(30);
		layout.addView(pickerx);
		layout.addView(text);
		layout.addView(pickery);
		
		builder.setView(layout);
		
		builder.setPositiveButton(R.string.new_button_new, new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				int x = pickerx.getCurrent();
				int y = pickery.getCurrent();
				p.newArt(x, y);
			}
			}); 
		builder.setNegativeButton(R.string.new_button_cancel, new DialogInterface.OnClickListener() {  
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
		builder.setTitle(R.string.saveas_title);
		final EditText edit = new EditText(p);
		edit.setHint("Pick a name");
		builder.setView(edit);
		builder.setPositiveButton(R.string.saveas_button_new, new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				p.save(edit.getText().toString());
			}
			}); 
		builder.setNegativeButton(R.string.saveas_button_cancel, new DialogInterface.OnClickListener() {  
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
				String.format(p.getResources().getString(R.string.export_size_low),lowxy[0],lowxy[1]), //Low (320x240) 
				String.format(p.getResources().getString(R.string.export_size_medium),medxy[0],medxy[1]), //Low (320x240) 
				String.format(p.getResources().getString(R.string.export_size_high),highxy[0],highxy[1]), //Low (320x240) 
				};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(p);
		builder.setTitle(R.string.export_title);
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
		builder.setPositiveButton(R.string.export_button_other, new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				showExportCustom(p);
			}
			}); 
		builder.setNegativeButton(R.string.export_button_cancel, new DialogInterface.OnClickListener() {  
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
		builder.setTitle(R.string.exportcustom_title);
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
		
		builder.setPositiveButton(R.string.exportcustom_button_export, new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {
				int x = pickerx.getCurrent();
				int y = pickery.getCurrent();
				int max = Math.max(x,y);
				p.export(max, ""+x+"x"+y );
			}
			}); 
		builder.setNegativeButton(R.string.exportcustom_button_cancel, new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				dialog.dismiss();
			}
			}); 

		AlertDialog alert = builder.create();
		alert.show();
		
	}

	
	
	
	
	public static void showAboutDialog(final PixelArtEditor p) {
		Log.d("PixelArt", "alertz showabout");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(p);
		builder.setTitle(R.string.about_title);
		TextView textcontent = new TextView(p);
		textcontent.setMovementMethod(LinkMovementMethod.getInstance());
		textcontent.setText(Html.fromHtml(p.getResources().getString(R.string.about)));
		textcontent.setLinkTextColor(Color.GREEN);
		textcontent.setPadding(5,5,5,5);
		textcontent.setTextSize(15);
		builder.setView(textcontent);
		builder.setPositiveButton(R.string.about_button_market, new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) { 
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id="+p.getApplication().getPackageName()));
				p.startActivity(intent);
				dialog.dismiss();
			}
			}); 
		builder.setNegativeButton(R.string.about_button_ok, new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				dialog.dismiss();
			}
			}); 

		AlertDialog alert = builder.create();
		alert.show();
		
		Log.d("PixelArt", "alertz shownew");
	}

	
	
	
	public static void showImportBackgroundDialog(final PixelArtEditor p) {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		p.startActivityForResult(i, PixelArtEditor.IMAGE_ACTIVITY); 
	}

	
	
}
