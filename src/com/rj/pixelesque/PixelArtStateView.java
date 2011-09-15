package com.rj.pixelesque;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.rj.pixelesque.ColorPickerDialog.OnColorChangedListener;


public class PixelArtStateView  extends LinearLayout {


	PixelArtState state;
	PixelData data;
	
	View pencilmode;
	View erasermode;
	View rectanglemode;
	
	View colorpicker;
	View undo;
	View redo;
	
	public PixelArtStateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	public void init() {
		
		pencilmode = findViewById(R.id.pencilmode);
		erasermode = findViewById(R.id.erasermode);
		rectanglemode = findViewById(R.id.rectanglemode);
		
		colorpicker = findViewById(R.id.colorpicker);
		colorpicker.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
                new ColorPickerDialog(getContext(), new ColorPickerDialog.OnColorChangedListener() {
					@Override
					public void colorChanged(int color) {
						if (state != null) state.selectedColor = color;
						updateFromState();
					}                	
                }, (state != null) ? state.selectedColor : Color.WHITE).show();
			}});
		
		
		undo = findViewById(R.id.undo);
		undo.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				data.history.undo();
				updateFromState();
			}});
		redo = findViewById(R.id.redo);
		redo.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				data.history.redo();
				updateFromState();
			}});


	}
	
	public void setState(PixelArtState state, PixelData data) {
		this.state = state;
		this.data = data;
		updateFromState();
	}
	
	public void updateFromState() {
		if (pencilmode == null) init();
		checkHistoryButtons();
	}
	
	public void checkHistoryButtons() {
		if (data == null || data.history == null) return;
		
		undo.setClickable(data.history.canUndo());
		redo.setClickable(data.history.canRedo());
	}
	
}
