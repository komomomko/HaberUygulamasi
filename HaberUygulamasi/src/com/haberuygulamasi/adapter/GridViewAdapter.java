package com.haberuygulamasi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kodlab.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GridViewAdapter extends BaseAdapter {
	Activity activity;
	View gridView;
	ImageLoader imageLoader = ImageLoader.getInstance();
	ArrayList<HashMap<String, Object>> arrayList;
	HashMap<String, Object> data;
	String objectType;

	public GridViewAdapter(Activity a,
			ArrayList<HashMap<String, Object>> aGaleriList, String oType) {
		activity = a;
		objectType = oType;
		arrayList = aGaleriList;

		if (AnasayfaHaberAdapter.imageOptions == null) {
			AnasayfaHaberAdapter.imageOptions = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.logo).resetViewBeforeLoading()
					.cacheInMemory().cacheOnDisc().build();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			gridView = LayoutInflater.from(activity.getApplicationContext())
					.inflate(R.layout.grid_with_text_list_item, null);
		} else {
			gridView = convertView;
		}
		ImageView ivGrid = (ImageView) gridView.findViewById(R.id.program_logo);
		TextView tvGrid = (TextView) gridView.findViewById(R.id.grid_text);
		if (objectType.contains("ResimList")) {
			if (tvGrid.getVisibility() == View.GONE)
				try {
					tvGrid.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			data = arrayList.get(position);
			tvGrid.setText((String) data.get("ResimGaleriTitle"));
			ivGrid.setTag(data.get("ResimGaleriImage"));
			imageLoader.displayImage((String) data.get("ResimGaleriImage"),
					ivGrid, AnasayfaHaberAdapter.imageOptions);
		} else if (objectType.contains("VideoList")) {
			if (tvGrid.getVisibility() == View.GONE)
				try {
					tvGrid.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			data = arrayList.get(position);
			tvGrid.setText((String) data.get("VideoGaleriTitle"));
			ivGrid.setTag(data.get("VideoGaleriImage"));
			imageLoader.displayImage((String) data.get("VideoGaleriImage"),
					ivGrid, AnasayfaHaberAdapter.imageOptions);
		}
		return gridView;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}