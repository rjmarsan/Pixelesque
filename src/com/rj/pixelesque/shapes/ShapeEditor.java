package com.rj.pixelesque.shapes;

import java.util.HashMap;
import java.util.Map.Entry;

import processing.core.PApplet;

import com.rj.pixelesque.PixelArt;
import com.rj.processing.mt.Cursor;

public class ShapeEditor {
	public interface ShapeFactory {
		public Shape makeShape(PApplet p, PixelArt pix, Cursor cursor);
	}
	public HashMap<Integer, Shape> shapes = new HashMap<Integer, Shape>();
	public ShapeFactory factory;
	
	
	public void addCursor(PApplet p, PixelArt art, Cursor c) {
		if (factory == null) return;
		synchronized(shapes) {
			shapes.put(c.curId, factory.makeShape(p, art, c));
		}
	}
	
	public void commitCursor(Cursor c) {
		synchronized(shapes) {
			Shape s = shapes.remove(c.curId);
			if (s != null) s.commit();
		}
	}
	
	public void cancelCursor(Cursor c) {
		synchronized(shapes) {
			Shape s = shapes.remove(c.curId);
			if (s != null) s.cancel();
		}
	}

	
	public boolean hasCursor(Cursor c) {
		synchronized(shapes) {
			return shapes.containsKey(c.curId);
		}
	}
	
	public void clearCursors() {
		synchronized(shapes) {
			for (Entry<Integer, Shape> s : shapes.entrySet()) {
				s.getValue().cancel();
			}
			shapes.clear();
		}
	}
	
	public void update() {
		synchronized(shapes) {
			for (Entry<Integer, Shape> s : shapes.entrySet()) {
				s.getValue().update();
			}
		}
	}

	
	
	
}
