package com.haberuygulamasi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.haberuygulamasi.json.JSONHaber;
import com.kodlab.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* ***Anasayfadaki haberlerin listelenmiþ þekilde getirilmesini saðlar*** */
public class AnasayfaHaberAdapter extends ArrayAdapter<HashMap<String, Object>> {
	ArrayList<HashMap<String, Object>> arrayHaberList;
	HashMap<String, Object> data;
	ImageLoader imageLoader = ImageLoader.getInstance();
	Activity activity;
	JSONHaber jsonHaber, jsonHaberEk;
	public static DisplayImageOptions imageOptions;

	public AnasayfaHaberAdapter(Activity a, int resource, int listViewId,
			ArrayList<HashMap<String, Object>> arrayHaberList2) {
		super(a, resource, listViewId);
		arrayHaberList = arrayHaberList2;
		activity = a;
		if (imageOptions == null) {
			imageOptions = new DisplayImageOptions.Builder()
					.resetViewBeforeLoading()
					.cacheInMemory().cacheOnDisc().build();
		}
	}

	public class ViewHolder {
		public TextView haberKategori;
		public TextView haberBasligi;
		public ImageView haberResmi;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.anasayfa_list_item, parent, false);
			holder = new ViewHolder();
			holder.haberKategori = (TextView) vi.findViewById(R.id.haberBilgi);
			holder.haberBasligi = (TextView) vi.findViewById(R.id.haberBaslik);
			holder.haberResmi = (ImageView) vi.findViewById(R.id.haberResmi);
			vi.setTag(holder);
		}
		data = arrayHaberList.get(position);
		holder = (ViewHolder) vi.getTag();
		holder.haberKategori.setText((String) data.get("Category"));
		holder.haberBasligi.setText((String) data.get("HaberTitle"));
		imageLoader.displayImage((String) data.get("Image"),
				holder.haberResmi, imageOptions);
		return vi;
	}

	@Override
	public int getCount() {
		return arrayHaberList.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		return arrayHaberList.get(position);
	}
	
	
}
