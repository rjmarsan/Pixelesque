package com.rj.pixelesque.shapes;

import processing.core.PApplet;
import android.graphics.Point;

import com.rj.pixelesque.PixelArt;
import com.rj.processing.mt.Cursor;

public class Rectangle extends SuperShape {	
	public Rectangle(PApplet p, PixelArt art, Cursor c, int color, boolean fill) {
		super(p, art,c, color, fill);
	}

	public void updatePointArea() {
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
		
		
		for (int i=0; i<=width; i++) {
			int nx = lowx + i;
			selectedPoints.add(new Point(nx, highy));
			selectedPoints.add(new Point(nx, lowy));
		}

		for (int i=1; i<height; i++) {
			int ny = lowy + i;
			selectedPoints.add(new Point(highx, ny));
			selectedPoints.add(new Point(lowx, ny));
		}
		//selectedPoints.add(new Point(highx, highy));

	}
	
	public void fillShape() {
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
		
		for (int i=0; i<=width; i++) {
			int nx = lowx + i;
			for (int j=0; j<=height; j++) {
				int ny = lowy + j;
				art.setColor(nx, ny, this.color, false);
			}
		}
		art.history.add();
		
	}

	
}
