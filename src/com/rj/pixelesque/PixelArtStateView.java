package com.rj.pixelesque;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import de.devmil.common.ui.color.ColorSelectorActivity;
import de.devmil.common.ui.color.ColorSelectorDialog;


public class PixelArtStateView  extends LinearLayout {


	PixelArtState state;
	PixelArt data;
	PixelArtEditor pixelart;
	
	ImageView pencilmode;
	ImageView erasermode;
	ImageView pointermode;
	ImageView rectanglemode;
	ImageView circlemode;
	ImageView linemode;
	ImageView bucketmode;
	ViewGroup shapeselector;
	ImageView shapesmode;
	View shapescontainer;
	
	boolean shapeselectoropen = false;
	int shapeselectorchoice = -1;
	
	View colorpicker;
	View colorindicator;
	View undo;
	View redo;
	
	
	View parent;
	
	public PixelArtStateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setParent(View parent) {
		this.parent = parent;
	}

	
	public void init() {
		
		pencilmode = (ImageView)parent.findViewById(R.id.pencilmode);
		pencilmode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.DRAW;
				updateFromState();
			}});
		erasermode = (ImageView)parent.findViewById(R.id.erasermode);
		erasermode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.ERASER;
				updateFromState();
			}});
		pointermode = (ImageView)parent.findViewById(R.id.pointermode);
		pointermode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.PENCIL;
				updateFromState();
			}});
		rectanglemode = (ImageView)parent.findViewById(R.id.rectanglemode);
		rectanglemode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.RECTANGLE_FILL;
				updateFromState();
			}});
		circlemode = (ImageView)parent.findViewById(R.id.circlemode);
		circlemode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.CIRCLE_FILL;
				updateFromState();
			}});
		linemode = (ImageView)parent.findViewById(R.id.linemode);
		linemode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.LINE;
				updateFromState();
			}});
		
		bucketmode = (ImageView)parent.findViewById(R.id.bucketmode);
		bucketmode.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				state.mode = PixelArtState.BUCKET;
				updateFromState();
			}});
		
		shapeselector = (ViewGroup)parent.findViewById(R.id.shapesmenu);
		shapescontainer = parent.findViewById(R.id.shapesholder);
		shapesmode = (ImageView)findViewById(R.id.shapesmode);
		shapesmode.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setPressed(true);
					shapeselectoropen  = true;
					shapeselector.setVisibility(View.VISIBLE);
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)shapeselector.getLayoutParams();
					if (PixelArtEditor.isHorizontal()) {
						params.topMargin = shapescontainer.getTop(); 
						//params.width = shapescontainer.getWidth()*2;
						//params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					} else {
						params.leftMargin = shapescontainer.getLeft()-shapesmode.getWidth()/2;
						//params.width = shapescontainer.getWidth()*2;
						//params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					}
					//Log.d("StateView", String.format("params: width:%d height:%d marginLeft:%d ", params.width, params.height, params.leftMargin));
					shapeselector.setLayoutParams(params);
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP){
					shapeselector.setVisibility(View.GONE);
					v.setPressed(false);
					shapeselectoropen  = false;
					updateFromState();
					return true;
				}
				
				int x = (int)event.getRawX();
				int y = (int)event.getRawY();
				for (int i=0; i<shapeselector.getChildCount(); i++) {
					View view = shapeselector.getChildAt(i);
					selectButtonFromMovingSelectorView(view, x, y);
				}
				selectButtonFromMovingSelectorView(shapesmode, x, y);
			
				
				
				
				updateFromState();
				return true;
			}
		});

		
		
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
				int side = PixelArtEditor.isHorizontal() ? ColorSelectorDialog.RIGHT : ColorSelectorDialog.BOTTOM;
				int offset = PixelArtEditor.isHorizontal()? getWidth() : getHeight();
				int color = (state != null) ? state.selectedColor : Color.WHITE;
				Intent intent = new Intent(getContext(), ColorSelectorActivity.class);
				intent.putExtra(ColorSelectorActivity.COLOR, color);
				intent.putExtra(ColorSelectorActivity.SIDE, side);
				intent.putExtra(ColorSelectorActivity.OFFSET, offset);
				pixelart.startActivityForResult(intent, PixelArtEditor.COLOR_ACTIVITY);
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
	
	
	public void selectButtonFromMovingSelectorView(View view, int x, int y) {
		int[] coords = new int[2];
		view.getLocationOnScreen(coords);
		int viewx = coords[0];
		int viewy = coords[1];
		int viewxmax = viewx + view.getWidth();
		int viewymax = viewy + view.getHeight();
		if (viewx < x && viewxmax > x && 
				viewy < y && viewymax > y) {
			//Log.d("StateView", "Detected hit: "+view);
			view.performClick();
			view.setPressed(true);
		} else {
			view.setPressed(false);
		}
	}
	
	public void setState(PixelArtState state, PixelArt data, PixelArtEditor pixelart) {
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
		
		erasermode.setSelected(false);
		pointermode.setSelected(false);
		pencilmode.setSelected(false);
		rectanglemode.setSelected(false);
		circlemode.setSelected(false);
		linemode.setSelected(false);
		bucketmode.setSelected(false);
		//shapeselector;
		shapesmode.setSelected(false);

		if (shapeselectoropen)  {
			if (state.mode != PixelArtState.RECTANGLE_FILL && state.mode != PixelArtState.CIRCLE_FILL
						&& state.mode != PixelArtState.LINE && state.mode != PixelArtState.BUCKET) {
				state.mode = shapeselectorchoice; 
			}
		}
				
		
		if (state.mode == PixelArtState.DRAW) {
			pencilmode.setSelected(true);
		}
		else if (state.mode == PixelArtState.PENCIL) {
			pointermode.setSelected(true);
		}
		else if (state.mode == PixelArtState.ERASER) {
			erasermode.setSelected(true);
		}
		else if (state.mode == PixelArtState.RECTANGLE_FILL) {
			rectanglemode.setSelected(true);
			shapeselectorchoice = state.mode;
			shapesmode.setImageDrawable(rectanglemode.getDrawable());
		}
		else if (state.mode == PixelArtState.CIRCLE_FILL) {
			circlemode.setSelected(true);
			shapeselectorchoice = state.mode;
			shapesmode.setImageDrawable(circlemode.getDrawable());
		}
		else if (state.mode == PixelArtState.LINE) {
			linemode.setSelected(true);
			shapeselectorchoice = state.mode;
			shapesmode.setImageDrawable(linemode.getDrawable());
		}
		else if (state.mode == PixelArtState.BUCKET) {
			bucketmode.setSelected(true);
			shapeselectorchoice = state.mode;
			shapesmode.setImageDrawable(bucketmode.getDrawable());
		}

		
		colorindicator.setBackgroundColor(state.selectedColor);
 
	}
	
	public void checkHistoryButtons() {
		if (data == null || data.history == null) return;
		
		undo.setEnabled(data.history.canUndo());
		redo.setEnabled(data.history.canRedo());
	}
	
}
