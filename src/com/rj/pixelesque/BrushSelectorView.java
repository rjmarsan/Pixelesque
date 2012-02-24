package com.rj.pixelesque;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BrushSelectorView extends LinearLayout {
	View aligntarget;
	
	public BrushSelectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	
	
	public void setTarget(View target) {
		this.aligntarget = target;
		this.requestLayout();
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		if (aligntarget != null) { 
//			r = aligntarget.getRight();
//			l = aligntarget.getLeft();
//		}
		super.onLayout(changed, l, t, r, b);
	}
	
	
	
	

}
