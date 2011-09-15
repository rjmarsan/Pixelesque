package com.rj.pixelesque;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ArtListFragment extends Fragment {

	public static class ArtElement {
		File image;
		String name;
		public ArtElement(File image, String name) {
			this.image = image; this.name = name;
		}
	}

	public interface ArtItemSelectedListener {
		public void onArtItemSelected(ArtElement element);
	}

	
	
	
	GridView gridview;
	ArtAdapter adapter;
	ArtItemSelectedListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.artlist, null);
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		
		gridview = (GridView) getView().findViewById(R.id.list);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (listener != null) {
					listener.onArtItemSelected(adapter.getItem(arg2));
				}
			}
		});
		adapter = new ArtAdapter(getActivity(), 0);
		gridview.setAdapter(adapter);
		File path = new File(getActivity().getFilesDir(), "saves");
		new ImageListLoader().execute(path.getAbsolutePath());
	}
	
	public void setListener(ArtItemSelectedListener listen) {
		this.listener = listen;
	}
		
	public class ArtAdapter extends ArrayAdapter<ArtElement> {

		@Override
		public void addAll(Collection<? extends ArtElement> collection) {
			for (ArtElement e : collection)
				add(e);
		}
		
		public ArtAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater(getArguments()).inflate(R.layout.artlistitem, null);
			}
			ArtElement element = getItem(position);
			ImageView image = (ImageView) convertView.findViewById(R.id.thumb);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			
			//image.setScaleType(ScaleType.)
			image.setImageURI(Uri.fromFile(element.image));
			title.setText(element.name);
			
			return convertView;
		}
		
	}
	
	
	
	
	public class ImageListLoader extends AsyncTask<String, Void, ArrayList<ArtElement>> {

		@Override
		protected ArrayList<ArtElement> doInBackground(String... params) {
			try {
				String path = params[0];
				ArrayList<ArtElement> elements = new ArrayList<ArtElement>();
				
				File folder = new File(path);
				for (File file : folder.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String filename) {
						if (filename.toLowerCase().endsWith(".png"))
							return true;
						return false;
					}
					})) {
					
					ArtElement e = new ArtElement(file, file.getName().substring(0, file.getName().length()-4));
					elements.add(e);
					Log.d("ArtList", "Adding list item: "+file.getAbsolutePath());
					
				}
				
				return elements;
			} catch (Exception e) {
				e.printStackTrace();
				//Toast.makeText(getActivity(), "Error loading folder!", Toast.LENGTH_SHORT);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ArtElement> elements) {
			if (elements != null) {
				adapter.addAll(elements);
				adapter.notifyDataSetChanged();
			}
		}
		
	}
	
	
	
	
	
}
