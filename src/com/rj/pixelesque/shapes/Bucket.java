package com.rj.pixelesque.shapes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import processing.core.PApplet;
import android.graphics.Point;

import com.rj.pixelesque.PixelArt;
import com.rj.processing.mt.Cursor;

public class Bucket extends SuperShape {	
	public Bucket(PApplet p, PixelArt art, Cursor c, int color, boolean fill) {
		super(p, art,c, color, fill);
		this.highlightCursorStart=false;
	}

	public void updatePointArea() {
		selectedPoints.clear();
		if (!art.isValid(endCoord.x, endCoord.y)) return;
		
		int currentcolor = art.data[endCoord.x][endCoord.y].getLastColor();
		Queue<Point> toExplore = new LinkedList<Point>();
		ArrayList<Point> explored = new ArrayList<Point>();
		toExplore.add(endCoord);
		while (!toExplore.isEmpty()) {
			Point p = toExplore.remove();
			if (art.isValid(p.x,p.y)) {
				int color = art.data[p.x][p.y].getLastColor();
				if (color == currentcolor)
					selectedPoints.add(p);
			}
			Point[] neighbors = new Point[] {
				new Point(p.x, p.y-1),
				new Point(p.x, p.y+1),
				new Point(p.x-1, p.y),
				new Point(p.x+1, p.y),
			};
			for (Point pp : neighbors) {
				if (!explored.contains(pp)) { 
					if (art.isValid(p.x,p.y) && art.data[p.x][p.y].getLastColor() == currentcolor ) {
						toExplore.add(pp);
						explored.add(pp);
					}
				}
			}
			
		}
	}

	@Override
	public void fillShape() {
		setAllPoints();
	}
	
	
	
	
}
