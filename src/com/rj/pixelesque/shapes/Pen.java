package com.rj.pixelesque.shapes;

import processing.core.PApplet;
import android.graphics.Color;

import com.rj.pixelesque.History.HistoryAction;
import com.rj.pixelesque.PixelArt;
import com.rj.pixelesque.PixelArt.ColorStack;
import com.rj.pixelesque.shapes.ShapeEditor.ShapeFactory;
import com.rj.processing.mt.Cursor;

public class Pen extends Shape {	
	HistoryAction action = new HistoryAction();
	
	public Pen(PApplet p, PixelArt art, Cursor c, int color) {
		super(p, art,c, color);
		update();
	}
	
	public void update() {
		super.update();
		if (cursor == null || art == null || action == null) return;
		int[] coords = art.getDataCoordsFromXY(p, cursor.currentPoint.x, cursor.currentPoint.y);
		if ( ! art.isValid(coords)) return;
		ColorStack s = art.data[coords[0]][coords[1]];
		int color = s.getLastColor();
		if (color != this.color) {
			art.setColor(coords[0], coords[1], this.color, action);
		}
	}
	
	public void cancel() {
		super.cancel();
		action.undo(art);
	}
	
	public boolean commit() {
		if (!super.commit()) return false;
		update();
		art.history.add(action);
		action = null; // no more updates;
		return true;
	}
	
}
