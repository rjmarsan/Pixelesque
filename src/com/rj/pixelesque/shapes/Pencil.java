package com.rj.pixelesque.shapes;

import processing.core.PApplet;

import com.rj.pixelesque.PixelArt;
import com.rj.processing.mt.Cursor;

public class Pencil extends Shape {	
	
	public Pencil(PApplet p, PixelArt art, Cursor c, int color) {
		super(p, art,c, color);
	}
	
	public void update() {
		super.update();
	}
	
	public void cancel() {
		super.cancel();
	}
	
	public boolean commit() {
		if (!super.commit()) return false;
		if (cursor == null || art == null) return false;
		int[] coords = art.getDataCoordsFromXY(p, cursor.firstPoint.x, cursor.firstPoint.y);
//		art.flipColor(coords[0], coords[1], this.color);
		return true;
	}
	
}
