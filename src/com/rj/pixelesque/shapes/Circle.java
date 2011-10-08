package com.rj.pixelesque.shapes;

import processing.core.PApplet;
import android.graphics.Point;
import android.util.Log;

import com.rj.pixelesque.PixelArt;
import com.rj.pixelesque.History.HistoryAction;
import com.rj.processing.mt.Cursor;

public class Circle extends SuperShape {	
	public Circle(PApplet p, PixelArt art, Cursor c, int color, boolean fill) {
		super(p, art,c, color, fill);
	}

	public void updatePointArea() {
		selectedPoints.clear();
		int diffx  = startCoord.x - endCoord.x;
		int diffy = startCoord.y - endCoord.y;
		int distsqr = diffx*diffx + diffy*diffy;
		float dist = (float)Math.sqrt(distsqr);
		rasterCircle(startCoord.x, startCoord.y, (int)dist);
	}
	
	public void fillShape() {
		int maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE, minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE;
		
		for (Point p : selectedPoints) {
			if (p.x > maxx) maxx = p.x;
			if (p.y > maxy) maxy = p.y;
			if (p.x < minx) minx = p.x;
			if (p.y < miny) miny = p.y;
		}
		HistoryAction action = new HistoryAction();
		for (int row=miny; row<=maxy; row++) {
			int rowmin = Integer.MAX_VALUE;
			int rowmax = Integer.MIN_VALUE;
			for (Point p : selectedPoints) {
				if (p.y == row) {
					if (p.x > rowmax) rowmax = p.x;
					if (p.x < rowmin) rowmin = p.x;
				}
			}
			for (int x = rowmin; x <= rowmax; x++ ) {
				art.setColor(x, row, this.color, action);
			}
		}
		art.history.add(action);
	}
	
	/**
	 * pt1
	 * 78
	 * jazzchord
	 * coolside
	 * pt2
	 * methyl
	 * warpzone
	 * keanu
	 * wsc
	 */
	
	/**
	 * pt1
	 * 78
	 * jazzchord
	 * warp zone
	 * pt2
	 * coolside
	 * keanu
	 * methyl
	 * whitespace
	 */
	
	
	
	
	
	/**
	 * FROM WIKIPEDIA:
	 * http://en.wikipedia.org/wiki/Midpoint_circle_algorithm
	 */

	void rasterCircle(int x0, int y0, int radius) {
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;

		setPixel(x0, y0 + radius);
		setPixel(x0, y0 - radius);
		setPixel(x0 + radius, y0);
		setPixel(x0 - radius, y0);

		while (x < y) {
			// ddF_x == 2 * x + 1;
			// ddF_y == -2 * y;
			// f == x*x + y*y - radius*radius + 2*x - y + 1;
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;
			setPixel(x0 + x, y0 + y);
			setPixel(x0 - x, y0 + y);
			setPixel(x0 + x, y0 - y);
			setPixel(x0 - x, y0 - y);
			setPixel(x0 + y, y0 + x);
			setPixel(x0 - y, y0 + x);
			setPixel(x0 + y, y0 - x);
			setPixel(x0 - y, y0 - x);
		}
	}
	
	void setPixel(int x, int y) {
		selectedPoints.add(new Point(x,y));
	}

	
	
	
	
	
	
	

}
