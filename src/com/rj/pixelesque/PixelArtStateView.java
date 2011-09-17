package com.rj.pixelesque;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import de.devmil.common.ui.color.ColorSelectorActivity;
import de.devmil.common.ui.color.ColorSelectorDialog;


public class PixelArtStateView  extends LinearLayout {


	PixelArtState state;
	PixelData data;
	PixelArt pixelart;
	
	View pencilmode;
	View erasermode;
	View rectanglemode;
	
	View colorpicker;
	View colorindicator;
	View undo;
	View redo;
	
	public PixelArtStateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	public void init() {
		
		pencilmode = findViewById(R.id.pencilmode);
		pencilmode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.DRAW;
				updateFromState();
			}});
		erasermode = findViewById(R.id.erasermode);
		erasermode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.ERASER;
				updateFromState();
			}});
		rectanglemode = findViewById(R.id.pointermode);
		rectanglemode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.PENCIL;
				updateFromState();
			}});

		
		colorpicker = findViewById(R.id.colorpicker);
		colorindicator = findViewById(R.id.colorindicator);
		colorpicker.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
//                new ColorSelectorDialog(getContext(), new ColorSelectorDialog.OnColorChangedListener() {
//					@Override
//					public void colorChanged(int color) {
//						if (state != null) state.selectedColor = color;
//						updateFromState();
//					}                	
//                }, (state != null) ? state.selectedColor : Color.WHITE, 
//                		PixelArt.isHorizontal() ? ColorSelectorDialog.RIGHT : ColorSelectorDialog.BOTTOM, 
//                				getWidth()).show();
				int side = PixelArt.isHorizontal() ? ColorSelectorDialog.RIGHT : ColorSelectorDialog.BOTTOM;
				int offset = PixelArt.isHorizontal()? getWidth() : getHeight();
				int color = (state != null) ? state.selectedColor : Color.WHITE;
				Intent intent = new Intent(getContext(), ColorSelectorActivity.class);
				intent.putExtra(ColorSelectorActivity.COLOR, color);
				intent.putExtra(ColorSelectorActivity.SIDE, side);
				intent.putExtra(ColorSelectorActivity.OFFSET, offset);
				pixelart.startActivityForResult(intent, PixelArt.COLOR_ACTIVITY);
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
	
	public void setState(PixelArtState state, PixelData data, PixelArt pixelart) {
		this.state = state;
		this.data = data;
		this.pixelart = pixelart;
		getHandler().post(new Runnable() {
			public void run() {updateFromState(); }
		});
	} 
	
	public void updateFromState() {
		if (pencilmode == null) init();
		checkHistoryButtons();
		
		
		if (state.mode == PixelArtState.DRAW) {
			pencilmode.setSelected(true);
			erasermode.setSelected(false);
			rectanglemode.setSelected(false);
		}
		if (state.mode == PixelArtState.PENCIL) {
			pencilmode.setSelected(false);
			erasermode.setSelected(false);
			rectanglemode.setSelected(true);
		}
		if (state.mode == PixelArtState.ERASER) {
			pencilmode.setSelected(false);
			erasermode.setSelected(true);
			rectanglemode.setSelected(false);
		}
		
		colorindicator.setBackgroundColor(state.selectedColor);
 
	}
	
	public void checkHistoryButtons() {
		if (data == null || data.history == null) return;
		
		undo.setEnabled(data.history.canUndo());
		redo.setEnabled(data.history.canRedo());
	}
	
}
