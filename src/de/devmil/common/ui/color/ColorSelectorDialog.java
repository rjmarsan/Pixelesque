/*
 * Copyright (C) 2011 Devmil (Michael Lamers) 
 * Mail: develmil@googlemail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devmil.common.ui.color;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.rj.pixelesque.R;

public class ColorSelectorDialog extends Dialog {
	public final static int BOTTOM = 1;
	public final static int RIGHT = 2;
	public final static int TOP = 3;
	public final static int LEFT = 4;
	public final static int CENTER = 0;
	
	private ColorSelectorView content;
	private OnColorChangedListener listener;
	private int initColor;
	private int color;
	private Button btnOld;
	private Button btnNew;
	private HistorySelectorView history;
	private int side;
	private int offset;
	
	public ColorSelectorDialog(Context context, OnColorChangedListener listener, int initColor, int side, int offset) {
		super(context, R.style.myBackgroundStyle);
		this.listener = listener;
		this.initColor = initColor;
		this.side = side;
		this.offset = offset;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.colordialog);
		
		if (this.side == RIGHT) {
			getWindow().setGravity(Gravity.RIGHT);
			android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
			p.x = offset;
			getWindow().setAttributes(p);
		} else if (this.side == BOTTOM ) {
//			getWindow().setGravity(Gravity.BOTTOM);
//			android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
//			p.y = offset;
//			getWindow().setAttributes(p);
		}

		btnOld = (Button)findViewById(R.id.button_old);
		btnOld.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		btnNew = (Button)findViewById(R.id.button_new);
		btnNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null) {
					listener.colorChanged(color);
				}
				history.selectColor(color);
				dismiss();
			}
		});
		
		content = (ColorSelectorView)findViewById(R.id.content);
		content.setDialog(this);
		content.setOnColorChangedListener(new ColorSelectorView.OnColorChangedListener() {
			@Override
			public void colorChanged(int color) {
				colorChangedInternal(color);
			}
		});
	
		history = (HistorySelectorView) findViewById(R.id.historyselector);
		history.setOnColorChangedListener(new HistorySelectorView.OnColorChangedListener() {
			@Override
			public void colorChanged(int color) {
				colorChangedInternal(color);
				content.setColor(color);
			}
		});

		
		btnOld.setBackgroundColor(initColor);
		btnOld.setTextColor(~initColor | 0xFF000000);
		
		content.setColor(initColor);
	}
	
	private void colorChangedInternal(int color)
	{
		btnNew.setBackgroundColor(color);
		btnNew.setTextColor(~color | 0xFF000000); //without alpha
		this.color = color;
	}
	
	public void setColor(int color)
	{
		content.setColor(color);
	}
	
	public interface OnColorChangedListener
	{
		public void colorChanged(int color);
	}
	
	@Override
	 public boolean onTouchEvent(MotionEvent event)
	 {
			
	        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
	         System.out.println("TOuch outside the dialog ******************** ");
	                this.dismiss();
	        }
	        return super.onTouchEvent(event);
	 }

}
