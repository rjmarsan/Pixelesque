package com.rj.pixelesque.shapes;

import processing.core.PApplet;
import android.graphics.Point;
import android.util.Log;

import com.rj.pixelesque.PixelArt;
import com.rj.processing.mt.Cursor;

public class Rectangle extends Shape {	
	Point startCoord;
	Point endCoord;
	
	public Rectangle(PApplet p, PixelArt art, Cursor c, int color) {
		super(p, art,c, color);
		this.highlightCursorStart = true;
		startCoord = getPointFromCurrent(c);
		endCoord = new Point(startCoord);
		updatePointRect();
	}
	
	Point getPointFromCurrent(Cursor c) {
		int[] start = art.getDataCoordsFromXY(p, c.currentPoint.x, c.currentPoint.y);
		return new Point(start[0], start[1]);
	}
	
	public void update() {
		Point newCoord = getPointFromCurrent(cursor);
		Log.d("Rectangle", "New coordinate: "+newCoord);
		if (newCoord.equals(endCoord)) {
			
		} else {
			endCoord = newCoord;
			updatePointRect();
		}
	}
	
	public void updatePointRect() {
		selectedPoints.clear();
		int lowx = endCoord.x;
		int highx = startCoord.x;
		int lowy = endCoord.y;
		int highy = startCoord.y;
		if (lowy > highy) {
			lowy = startCoord.y;
			highy = endCoord.y;
		}
		if (lowx > highx) {
			lowx = startCoord.x;
			highx = endCoord.x;
		}
		int width = highx - lowx;
		int height = highy - lowy;
		
		
		for (int i=0; i<width; i++) {
			int nx = lowx + i;
			selectedPoints.add(new Point(nx, highy));
			selectedPoints.add(new Point(nx, lowy));
		}

		for (int i=0; i<height; i++) {
			int ny = lowy + i;
			selectedPoints.add(new Point(highx, ny));
			selectedPoints.add(new Point(lowx, ny));
		}
		selectedPoints.add(new Point(highx, highy));

	}
	
	public void cancel() {
		super.cancel();
	}
	
	public void commit() {
		super.commit();
		setAllPoints();
	}
	
}
