package com.rj.pixelesque;

import java.util.ArrayList;
import java.util.Arrays;


public class History  {
	public final static int MAX_HISTORY = 200;
	private int position = 0;
	private PixelArt art;
	
	public History(PixelArt data) {
		this.art = data;
	}
	
	
	public void cancel() {
		ArrayList<int[]> history = this.art.historydata;
		int[] data = history.get(position);
		art.setData(data);
		if (art.drawer != null) art.drawer.scheduleRedraw();	
	}
	
	public void add() {
		int[] curdata = new int[this.art.workingdata.length];
		System.arraycopy(this.art.workingdata, 0, curdata, 0, this.art.workingdata.length);
		ArrayList<int[]> history = this.art.historydata;
		/**
		 * clear the backstack of anything past the current position
		 */
		while ( position < history.size() - 1) {
			history.remove(history.size()-1);
		}
		history.add(curdata);
		while (history.size() > MAX_HISTORY) {
			history.remove(0);
		}
		position = history.size() - 1;

	}
	
	public void undo() {
//		Log.d("History", "UNDO position: "+position+" max:"+history.size());
//		Log.d("History", data.dumpBoard());
		if (!canUndo()) return;
		ArrayList<int[]> history = this.art.historydata;
		int[] data = history.get(position-1);
		art.setData(data);
		position -= 1;
		if (art.drawer != null) art.drawer.scheduleRedraw();
	}
	
	public void redo() {
//		Log.d("History", "REDO position: "+position+" max:"+history.size());
//		Log.d("History", data.dumpBoard());
		if (!canRedo()) return;
		
		ArrayList<int[]> history = this.art.historydata;
		int[] data = history.get(position+1);
		art.setData(data);
		position += 1;
		if (art.drawer != null) art.drawer.scheduleRedraw();
	}
	
	public boolean canUndo() {
		return position >= 1;
		
	}
	
	public boolean canRedo() {
		ArrayList<int[]> history = this.art.historydata;
		return position < history.size()-1;
	}
	
	
	

}
