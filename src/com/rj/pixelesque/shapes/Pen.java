package com.rj.pixelesque.shapes;

import processing.core.PApplet;

import com.rj.pixelesque.PixelArt;
import com.rj.processing.mt.Cursor;

public class Pen extends Shape {		
	public Pen(PApplet p, PixelArt art, Cursor c, int color) {
		super(p, art,c, color);
		update();
	}
	
	public void update() {
		super.update();
		if (cursor == null || art == null) return;
		int[] coords = art.getDataCoordsFromXY(p, cursor.currentPoint.x, cursor.currentPoint.y);
		if ( ! art.isValid(coords)) return;
		int color = art.workingdata[coords[0]*art.width+coords[1]];
		if (color != this.color) {
			art.setColor(coords[0], coords[1], this.color, false);
		}
	}
	
	public void cancel() {
		super.cancel();
		art.history.cancel();
	}
	
	public boolean commit() {
		if (!super.commit()) return false;
		update();
		art.history.add();
		return true;
	}
	
}
