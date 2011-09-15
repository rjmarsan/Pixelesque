package com.rj.pixelesque;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Dialogs {

	public static void showNewDialog(final PixelArt p) {
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
	
	
	public static void showSaveAs(final PixelArt p) {
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
	
	
}
