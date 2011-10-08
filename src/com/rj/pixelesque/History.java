package com.rj.pixelesque;

import java.util.ArrayList;
import java.util.Vector;

import android.util.Log;

import com.rj.pixelesque.PixelArt.ColorStack;


public class History  {

	private Vector<HistoryAction> history = new Vector<HistoryAction>();
	private int position = -1;
	private PixelArt data;
	
	public History(PixelArt data) {
		this.data = data;
	}
	
	
	/**
	 * Simply will pop something off of the stack
	 * @author rj
	 *
	 */
	public static class PointAndColor {
		int x;
		int y;
		int color;
		public PointAndColor(int x, int y, int color) {
			this.x = x; this.y = y; this.color = color;
		}
	}
	public static class HistoryAction {
		ArrayList<PointAndColor> points = new ArrayList<PointAndColor>();
		public HistoryAction() {
			
		}
		public HistoryAction(int x, int y, int color) {
			addPoint(x,y,color);
		}
		public void addPoint(int x, int y, int color) {
			PointAndColor p = new PointAndColor(x, y, color);
			points.add(p);
		}
		public void undo(PixelArt data) {
			for (PointAndColor p : points) {
				ColorStack s = data.data[p.x][p.y];
				if (s.getLastColor() == p.color) s.popColor();
			}
		}
		public void redo(PixelArt data) {
			for (PointAndColor p : points) {
				ColorStack s = data.data[p.x][p.y];
				s.pushColor(p.color);
			}
		}
	}
	
	public void add(HistoryAction action) {
		/**
		 * clear the backstack of anything past the current position
		 */
		while ( position < history.size() - 1) {
			history.remove(history.size()-1);
		}
		history.add(action);
		position = history.size() - 1;
	}
	
	public void undo() {
//		Log.d("History", "UNDO position: "+position+" max:"+history.size());
//		Log.d("History", data.dumpBoard());
		if (!canUndo()) return;
		HistoryAction action = history.get(position);
		action.undo(data);
		position -= 1;
		if (data.drawer != null) data.drawer.scheduleRedraw();
	}
	
	public void redo() {
//		Log.d("History", "REDO position: "+position+" max:"+history.size());
//		Log.d("History", data.dumpBoard());
		if (!canRedo()) return;
		HistoryAction action = history.get(position+1);
		action.redo(data);
		position += 1;
		if (data.drawer != null) data.drawer.scheduleRedraw();
	}
	
	public boolean canUndo() {
		return position >= 0;
		
	}
	
	public boolean canRedo() {
		return position < history.size()-1;
	}
	
	
	

}
