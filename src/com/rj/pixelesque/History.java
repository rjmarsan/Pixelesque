package com.rj.pixelesque;

import java.util.ArrayList;
import java.util.Vector;

import com.rj.pixelesque.PixelData.ColorStack;


public class History  {

	private Vector<HistoryAction> history = new Vector<HistoryAction>();
	private int position = -1;
	private PixelData data;
	
	public History(PixelData data) {
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
		public void undo(PixelData data) {
			for (PointAndColor p : points) {
				ColorStack s = data.data[p.x][p.y];
				s.popColor();
			}
		}
		public void redo(PixelData data) {
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
		position = position + 1;
	}
	
	public void undo() {
		if (!canUndo()) return;
		HistoryAction action = history.get(position);
		action.undo(data);
		position -= 1;
	}
	
	public void redo() {
		if (!canRedo()) return;
		HistoryAction action = history.get(position+1);
		action.redo(data);
		position += 1;
	}
	
	public boolean canUndo() {
		return position >= 0;
		
	}
	
	public boolean canRedo() {
		return position < history.size()-1;
	}
	
	
	

}
