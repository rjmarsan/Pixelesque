package com.rj.pixelesque;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import android.graphics.Color;
import android.util.Log;

import com.rj.pixelesque.History.HistoryAction;
import com.rj.processing.mt.Cursor;

public class PixelData {
	public static final int MAX_BACKSTACK = 3;
	public ColorStack[][] data;
	int width; int height;
	public History history;
	public ArrayList<Cursor> cursors = new ArrayList<Cursor>();
	public float scale;
	public float topx, topy;
	public boolean outline;
	

	
	public static class ColorStack {
		private ArrayList<Integer> ints = new ArrayList<Integer>(MAX_BACKSTACK);
		
		public void pushColor(int color) {
			ints.add(color);
		}
		
		public int pushUnderneathColor() {
			if (ints.size() > 1) { 
				int lastcolor = ints.get(ints.size()-2);
				ints.add(lastcolor);
				return lastcolor;
			}
			return 0;
		}
		
		public void popColor() {
			if (ints.size() > 0) {
				ints.remove(ints.size()-1);
			}
		}
		
		public int getLastColor() {
			return ints.get(ints.size()-1);
		}
		
		public void clearStack(int tocolor) {
			ints.clear();
			ints.add(tocolor);
		}
	}

	public PixelData() {
		this(10,8);
	}
	
	public PixelData(PImage image) {
		
	}
	
	public PixelData(int width, int height) {
		this.width = width;
		this.height = height;
		data = new ColorStack[width][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new ColorStack[height];
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = new ColorStack();
				data[i][j].pushColor(Color.TRANSPARENT);
			}
		}
		topx = 0;
		topy = 0;
		scale = 1;
		outline = true;
		history = new History(this);
	}

	public PImage render(PApplet papp) {
		return render(papp, width, height);
	}
	public PImage render(PApplet papp, int width, int height) {
		PGraphics p = papp.createGraphics(width, height, PApplet.A2D);
		p.beginDraw();
		
		float boxsize = getBoxsize(width, height, 1);
		Log.d("PixelData", "Rendering: boxsize "+boxsize+" widthheight:"+width+"x"+height+"   ");
		
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {
				int color = data[x][y].getLastColor();
				p.noStroke();
				p.fill(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
				p.rect(boxsize * x, boxsize * y, boxsize, boxsize);
			}
		}

		p.endDraw();
		return p.get();
	}
	
	
	public void draw(PApplet p) {
		if (data == null || data[0] == null) return;
		
		float boxsize = getBoxsize(p.width, p.height);

		for (int x = 0; x < data.length; x++) {

			for (int y = 0; y < data[x].length; y++) {
				if (outline) p.stroke(127);
				int color = data[x][y].getLastColor();
				p.fill(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
				p.rect(topx + boxsize * x, topy + boxsize * y, boxsize, boxsize);
			}
		}
		synchronized(cursors) {
			for (Cursor c : cursors) {
				int[] coords = getDataCoordsFromXY(p, c.firstPoint.x, c.firstPoint.y);
				int x = coords[0]; int y = coords[1];
				if (isValid(x,y)) {
					float extra = 30;
					p.fill(255,255,255,80);
					p.rect(topx + boxsize * x - extra, topy + boxsize * y - extra, boxsize + extra*2, boxsize + extra*2);
				}
			}
		}
	}
	
	
	float getBoxsize(float width, float height) {
		return getBoxsize(width, height, scale);
	}
	float getBoxsize(float width, float height, float scale) {
		float boxwidth = width / this.width * scale;
		float boxheight = height / this.height * scale;
		float boxsize = PApplet.min(boxwidth, boxheight);
		return boxsize;
	}
	
	float getWidth(PApplet p) {
		return getBoxsize(p.width, p.height) * this.width;
	}
	float getHeight(PApplet p) {
		return getBoxsize(p.width, p.height) * this.height;
	}
	
	int[] coords = new int[2];
	int[] getDataCoordsFromXY(PApplet p, float mousex, float mousey) {
		mousex -= this.topx;
		mousey -= this.topy;

		float boxsize = this.getBoxsize(p.width, p.height);
		int x = (int) (mousex / boxsize);
		int y = (int) (mousey / boxsize);

		int[] coords = new int[2];
		coords[0] = x;
		coords[1] = y;
		return coords;
	}
	/*
	x = (fx-tx)/s
	s * x = (fx-tx)
	s * x + tx = fx
	*/
	
	float[] getDataCoordsFloatFromXY(PApplet p, float mousex, float mousey) {
		mousex -= this.topx;
		mousey -= this.topy;

		float boxsize = this.getBoxsize(p.width, p.height);
		float x = (mousex / boxsize);
		float y = (mousey / boxsize);

		float[] coords = new float[2];
		coords[0] = x;
		coords[1] = y;
		return coords;
	}
	
	float[] getXYFromDataCoordsFloat(PApplet p, float x, float y) {

		float boxsize = this.getBoxsize(p.width, p.height);
		float mousex = (x * boxsize);
		float mousey = (y * boxsize);

		mousex += this.topx;
		mousey += this.topy;
		
		float[] coords = new float[2];
		coords[0] = mousex;
		coords[1] = mousey;
		return coords;
	}




	public void flipColor(int x, int y, int tocolor) {
		if (isValid(x,y)) {
			int c = data[x][y].getLastColor();
			if (c == tocolor) {
				int color = data[x][y].pushUnderneathColor();
				history.add(new History.HistoryAction(x,y,color));
			} else {
				data[x][y].pushColor(tocolor);
				history.add(new History.HistoryAction(x,y,tocolor));
			}
		} else {
		}
	}
	
	public void eraseColor(int x, int y) {
		if (isValid(x,y)) {
			data[x][y].pushColor(Color.TRANSPARENT);
			history.add(new History.HistoryAction(x,y,Color.TRANSPARENT));
		}
	}
	
	public void rectangle(int x, int y, int width, int height, int color) {
		if (isValid(x,y) && isValid(x+width, y+height)) {
			HistoryAction action = new HistoryAction();
			for (int i=x; i<x+width; i++) {
				for (int j=y; j<y+height; j++) {
					data[x][y].pushColor(color);
					action.addPoint(x, y, color);
				}
			}
			history.add(action);
		}
	}
	
	public boolean isValid(int x, int y) {
		return 	x >= 0 && x < this.width && y >= 0 && y < this.height;
	}
	public boolean isValid(int[] coords) {
		return isValid(coords[0], coords[1]);
	}
	
	public void addCursor(Cursor c) {
		synchronized(cursors) {
			cursors.add(c);
		}
	}
	
	public void removeCursor(Cursor c) {
		synchronized(cursors) {
			cursors.remove(c);
		}
	}
	
	public boolean hasCursor(Cursor c) {
		return cursors.contains(c);
	}
	
	public void clearCursors() {
		synchronized(cursors) {
			cursors.clear();
		}
	}
	
}
