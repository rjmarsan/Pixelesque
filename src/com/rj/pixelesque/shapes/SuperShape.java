package com.rj.pixelesque.shapes;

import processing.core.PApplet;
import android.graphics.Point;
import android.util.Log;

import com.rj.pixelesque.PixelArt;
import com.rj.pixelesque.History.HistoryAction;
import com.rj.processing.mt.Cursor;

public abstract class SuperShape extends Shape {	
	Point startCoord;
	Point endCoord;
	boolean fill;
	
	public SuperShape(PApplet p, PixelArt art, Cursor c, int color, boolean fill) {
		super(p, art,c, color);
		this.fill = fill;
		this.highlightCursorStart = true;
		startCoord = getPointFromCurrent(c);
		endCoord = new Point(startCoord);
		updatePointArea();
	}
	
	Point getPointFromCurrent(Cursor c) {
		int[] start = art.getDataCoordsFromXY(p, c.currentPoint.x, c.currentPoint.y);
		return new Point(start[0], start[1]);
	}
	
	public void update() {
		Point newCoord = getPointFromCurrent(cursor);
		//Log.d("SuperShape", "New coordinate: "+newCoord);
		if (newCoord.equals(endCoord)) {
			
		} else {
			endCoord = newCoord;
			updatePointArea();
		}
	}
	
	public abstract void updatePointArea();
	
	public void cancel() {
		super.cancel();
	}
	
	public boolean commit() {
		if (!super.commit()) return false;
		if (fill) 
			fillShape();
		else
			setAllPoints();
		return true;
	}
	
	public abstract void fillShape();	
}
