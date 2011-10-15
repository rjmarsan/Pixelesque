package com.rj.pixelesque.shapes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Point;

import com.rj.pixelesque.PixelArt;
import com.rj.pixelesque.PixelArtEditor;
import com.rj.processing.mt.Cursor;

public class Bucket extends SuperShape {
	PixelArtEditor p;
	public Bucket(PixelArtEditor p, PixelArt art, Cursor c, int color, boolean fill) {
		super(p, art,c, color, fill);
		this.p = p;
		this.highlightCursorStart=false;
	}

	public void updatePointArea() {
		if (thread != null) thread.keepgoing = false;
		thread = new FillThread();
		thread.start();
	}
	
	public FillThread thread;
	
	public class FillThread extends Thread {
		public boolean keepgoing = true;
		public void run() {
			lockCalculatingBrush();
			ArrayList<Point> selectedPoints = new ArrayList<Point>();
			Point endCoord = Bucket.this.endCoord;
			
			if (!art.isValid(endCoord.x, endCoord.y)) return;
			
			
			Point[][] points = new Point[art.width][];
			for (int i=0; i<points.length; i++) {
				points[i] = new Point[art.height]; 
				for (int j=0; j<art.height; j++) {
					points[i][j] = new Point(i,j);
				}
			}
			
			
			int currentcolor = art.data[endCoord.x][endCoord.y].getLastColor();
			Queue<Point> toExplore = new LinkedList<Point>();
			HashSet<Point> explored = new HashSet<Point>();
			toExplore.add(endCoord);
			while (!toExplore.isEmpty() && keepgoing) {
				Point p = toExplore.remove();
				
				selectedPoints.add(p);
				
	//			Point[] neighbors = new Point[] {
	//				new Point(p.x, p.y-1),
	//				new Point(p.x, p.y+1),
	//				new Point(p.x-1, p.y),
	//				new Point(p.x+1, p.y),
	//			};
	//			for (Point pp : neighbors) {
	//				if (!explored.contains(pp)) { 
	//					if (art.isValid(pp.x,pp.y) && art.data[pp.x][pp.y].getLastColor() == currentcolor ) {
	//						toExplore.add(pp);
	//					}
	//					explored.add(pp);
	//				}
	//			}
				
				Point pp;
				if (art.isValid(p.x,p.y-1)) {
					pp = points[p.x][p.y-1];
					if (!explored.contains(pp)) { 
						if (art.data[pp.x][pp.y].getLastColor() == currentcolor )
							toExplore.add(pp);
						explored.add(pp);
					}
				}
				
				if (art.isValid(p.x,p.y+1)) {
					pp = points[p.x][p.y+1];
					if (!explored.contains(pp)) { 
						if (art.data[pp.x][pp.y].getLastColor() == currentcolor )
							toExplore.add(pp);
						explored.add(pp);
					}
				}
				
				if (art.isValid(p.x-1,p.y)) {
					pp = points[p.x-1][p.y];
					if (!explored.contains(pp)) { 
						if (art.data[pp.x][pp.y].getLastColor() == currentcolor )
							toExplore.add(pp);
						explored.add(pp);
					}
				}
	
				if (art.isValid(p.x+1,p.y)) {
					pp = points[p.x+1][p.y];
					if (!explored.contains(pp)) { 
						if (art.data[pp.x][pp.y].getLastColor() == currentcolor )
							toExplore.add(pp);
						explored.add(pp);
					}
				}
			}

			if (keepgoing) Bucket.this.selectedPoints = selectedPoints;
			unlockCalculatingBrush();
		}
	}
	
	
	

	@Override
	public void fillShape() {
		setAllPoints();
	}
	
	@Override
	public boolean commit() {
		if (super.commit()) {
			p.scheduleUIRedraw();
		}
		return true;
	}
	
	
	
	
}
