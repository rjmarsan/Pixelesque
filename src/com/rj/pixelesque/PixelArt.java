package com.rj.pixelesque;

import java.util.ArrayList;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PImage;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import com.rj.pixelesque.History.HistoryAction;
import com.rj.pixelesque.shapes.Shape;
import com.rj.pixelesque.shapes.ShapeEditor;

public class PixelArt {
	public static final int MAX_BACKSTACK = 3;
	public ColorStack[] data;
	public int width; public int height;
	public History history;
	public float scale;
	public float topx, topy;
	public boolean outline;
	public float outlineThresh = 17; //boxsize before outlines go into effect
	public String name;
	public Drawer drawer;
	public ShapeEditor shapeeditor = new ShapeEditor();
	public boolean canvasLock = false;
	
	public boolean preview = true;
	

	
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
			if (ints.size() > 0)
				return ints.get(ints.size()-1);
			return Color.TRANSPARENT;
		}
		
		public void clearStack(int tocolor) {
			ints.clear();
			ints.add(tocolor);
		}
		
		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			for (int i : ints) {
				b.append(i+",");
			}
			return b.toString();
		}
	}

	public PixelArt() {
		this(12,16);
	}
	
	public PixelArt(PImage image, String name) {
		this(image.width, image.height);
		image.loadPixels();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = image.get(i, j);
				data[i*width+j].pushColor(color);
			}
		}
		this.name = name;
	}
	
	public PixelArt(Bitmap image, String name) {
		this(image.getWidth(), image.getHeight());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = image.getPixel(i, j);
				data[i*width+j].pushColor(color);
			}
		}
		this.name = name;
	}

	
	public PixelArt(int width, int height) {
		this.width = width;
		this.height = height;
		data = new ColorStack[width*height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				data[i*width+j] = new ColorStack();
				data[i*width+j].pushColor(Color.TRANSPARENT);
			}
		}
		topx = 0;
		topy = 0;
		scale = 1;
		outline = false;
		history = new History(this);
	}
	
	public static PixelArt makeFromShortside(int screenwidth, int screenheight, int shortside) {
		float width = screenwidth;
		float height = screenheight;
		if (screenwidth > screenheight) {
			width = screenheight;
			height = screenwidth;
		}
		float boxsize = width / shortside;
		int longside = (int) (height / boxsize);
		
		if (screenwidth > screenheight) {
			return new PixelArt(longside, shortside);
		} else {
			return new PixelArt(shortside, longside);
		}
		
		
	}
	public void setDrawer(Drawer drawer) {
		this.drawer = drawer;
	}

	public Bitmap render(PApplet papp) {
		return renderInternal(papp, width, height);
	}
	
	public Bitmap render(PApplet papp, int width, int height) {
		Log.d("Renderer", "Starting render...");
		Bitmap  map = renderInternal(papp, this.width, this.height);
		Log.d("Renderer", "Done with initial render.");
		Bitmap scaled = Bitmap.createScaledBitmap(map, width, height, false);
		Log.d("Renderer", "Scaled.");
		return scaled;
	}
	
	public Bitmap renderInternal(PApplet papp, int width, int height) {
		Bitmap image = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		float boxsize = getBoxsize(width, height, 1);
		Log.d("PixelData", "Rendering: boxsize "+boxsize+" widthheight:"+width+"x"+height+"   ");
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = data[x*width+y].getLastColor();
				image.setPixel(x, y, color);
			}
		}

		return image;
	}
	
	
	public void draw(PApplet p) {
		if (data == null || data[0] == null) return;
		float topx = this.topx;
		float topy = this.topy;
		float scale = this.scale;
		
		float boxsize = getBoxsize(p.width-2, p.height-2, scale);
		outline = false;
		if (boxsize > outlineThresh) outline = true;

		if (!outline && data.length * boxsize < p.height) {
			p.stroke(127);
			p.fill(0, 40, 40);
			p.rect(-1, data.length * boxsize, p.width+1, p.height+1);
		}
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float left = topx + boxsize * x + 1;
				float top = topy + boxsize * y + 1;
				if (top + boxsize > 0 && left + boxsize > 0 && top < p.height && left < p.width) { 
					if (outline) p.stroke(127);
					else p.noStroke();
					int color = data[x*width+y].getLastColor();
					p.fill(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
					p.rect(left, top, boxsize, boxsize);
				}
			}
		}
		if (preview) {
			p.stroke(127);
			p.fill(0);
			p.rect(p.width - width - 1, p.height - height - 1, width+1, height+1);
			p.rect(p.width - width*3 - 2, p.height - height*2 - 1, width*2+1, height*2+1);
			p.noStroke();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int color = data[x*width+y].getLastColor();
					p.fill(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
					p.rect(p.width - width + x, p.height - height + y,1,1);
					p.rect(p.width - width-width-width - 1 + x+x, p.height - height-height + y+y,2,2);
				}
			}
		}
		synchronized(shapeeditor.shapes) {
			for (Entry<Integer,Shape> s : shapeeditor.shapes.entrySet()) {
				s.getValue().draw(p, this, topx, topy, boxsize);
			}
		}
		if (canvasLock) {
			p.noStroke();
			p.fill(255,50);
			p.rect(0, 0, p.width, p.height);
		}
	}
	
	public String dumpBoard() {
		StringBuilder b = new StringBuilder();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				b.append(data[x*width+y].toString());
				b.append("|");
			}
			b.append("\n");
		}
		return b.toString();
	}
	
	
	public float getBoxsize(float width, float height) {
		return getBoxsize(width, height, scale);
	}
	public float getBoxsize(float width, float height, float scale) {
		float boxwidth = width / this.width * scale;
		float boxheight = height / this.height * scale;
		float boxsize = PApplet.min(boxwidth, boxheight);
		return boxsize;
	}
	
	public float getWidth(PApplet p) {
		return getBoxsize(p.width, p.height) * this.width;
	}
	public float getHeight(PApplet p) {
		if (preview)
			return getBoxsize(p.width, p.height) * this.height + this.height*2;
		else
			return getBoxsize(p.width, p.height) * this.height;
	}
	
	int[] coords = new int[2];
	public int[] getDataCoordsFromXY(PApplet p, float mousex, float mousey) {
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
	
	public float[] getDataCoordsFloatFromXY(PApplet p, float mousex, float mousey) {
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
	
	public float[] getXYFromDataCoordsFloat(PApplet p, float x, float y) {

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
			int c = data[x*width+y].getLastColor();
			if (c == tocolor) {
				int color = data[x*width+y].pushUnderneathColor();
				history.add(new History.HistoryAction(x,y,color));
			} else {
				data[x*width+y].pushColor(tocolor);
				history.add(new History.HistoryAction(x,y,tocolor));
			}
		} else {
		}
		if (drawer != null) drawer.scheduleRedraw();
	}
	
	
	public void eraseColor(int x, int y, HistoryAction action) {
		setColor(x,y,Color.TRANSPARENT, action);
	}
	public void setColor(int x, int y, int color, HistoryAction action) {
		if (isValid(x,y)) {
			data[x*width+y].pushColor(color);
			if (action == null) history.add(new History.HistoryAction(x,y,color));
			else action.addPoint(x, y, color);
		}
		if (drawer != null) drawer.scheduleRedraw();
	}
	
	public void rectangle(int x, int y, int width, int height, int color) {
		Log.d("PixelData", "Rectangle: x"+x+" y:"+y+" width:"+width+" height:"+height+" color:"+color+" ");
		if (isValid(x,y) && isValid(x+width, y+height)) {
			Log.d("PixelData", "Rectangle Valid : x"+x+" y:"+y+" width:"+width+" height:"+height+" color:"+color+" ");
			HistoryAction action = new HistoryAction();
			for (int i=x; i<=x+width; i++) {
				for (int j=y; j<=y+height; j++) {
					data[i*width+j].pushColor(color);
					action.addPoint(i, j, color);
				}
			}
			history.add(action);
		}
		if (drawer != null) drawer.scheduleRedraw();
	}
	
	public boolean isValid(int x, int y) {
		return 	x >= 0 && x < this.width && y >= 0 && y < this.height;
	}
	public boolean isValid(int[] coords) {
		return isValid(coords[0], coords[1]);
	}
	

	public void startCalculatingBrush() {
		
	}
	
	public void stopCalculatingBrush() {
		
	}
	
	public void canvasLock() {
		canvasLock = true;
		if (drawer != null) drawer.scheduleRedraw();
	}
	
	public void canvasUnlock() {
		canvasLock = false;
		if (drawer != null) {
			drawer.scheduleRedraw();
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
