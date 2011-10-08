package com.rj.pixelesque.shapes;

import processing.core.PApplet;
import android.graphics.Point;
import android.util.Log;

import com.rj.pixelesque.PixelArt;
import com.rj.pixelesque.History.HistoryAction;
import com.rj.processing.mt.Cursor;

public class Line extends SuperShape {	
	public Line(PApplet p, PixelArt art, Cursor c, int color, boolean fill) {
		super(p, art,c, color, fill);
	}

	public void updatePointArea() {
		selectedPoints.clear();
		line(startCoord.x, startCoord.y, endCoord.x, endCoord.y);
	}
	
	
	
	
	/**
	 * From wikipedia
	 * 
	 */
	void line(int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx, sy;
		if (x0 < x1)
			sx = 1;
		else
			sx = -1;
		if (y0 < y1)
			sy = 1;
		else
			sy = -1;
		int err = dx - dy;
		int e2;

		while (true) {
			selectedPoints.add(new Point(x0, y0));
			if (x0 == x1 && y0 == y1)
				break;
			e2 = 2 * err;
			if (e2 > -dy) {
				err = err - dy;
				x0 = x0 + sx;
			}
			if (e2 < dx) {
				err = err + dx;
				y0 = y0 + sy;
			}
		}
	}	
	
	
	
	public void fillShape() {
		setAllPoints();
	}

	
}
