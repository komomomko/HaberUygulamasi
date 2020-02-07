package com.haberuygulamasi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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

public class KategoriHaberiAdapter extends
		ArrayAdapter<HashMap<String, Object>> {
	ArrayList<HashMap<String, Object>> arrayHaber;
	HashMap<String, Object> data;
	ImageLoader imageLoader = ImageLoader.getInstance();
	Activity activity;

	public KategoriHaberiAdapter(Activity a, int resource, int list,
			ArrayList<HashMap<String, Object>> arrayHaberList) {
		super(a, resource, list);
		arrayHaber = arrayHaberList;
		activity = a;
		if (AnasayfaHaberAdapter.imageOptions == null) {
			AnasayfaHaberAdapter.imageOptions = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.logo).resetViewBeforeLoading()
					.cacheInMemory().cacheOnDisc().build();
		}
	}

	public class ViewHolder {
		public TextView kategoriHaberiTitle;
		public ImageView kategoriHaberiResim;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			vi = inflater.inflate(R.layout.kategori_haberi_list_item, null);
			holder = new ViewHolder();
			holder.kategoriHaberiTitle = (TextView) vi
					.findViewById(R.id.kategoriHaberiTitle);
			holder.kategoriHaberiResim = (ImageView) vi
					.findViewById(R.id.kategoriHaberiResmi);
			vi.setTag(holder);
		}
		data = arrayHaber.get(position);
		holder = (ViewHolder) vi.getTag();
		holder.kategoriHaberiTitle.setText((String) data.get("HaberTitle"));
		holder.kategoriHaberiResim.setTag(data.get("HaberImage"));
		imageLoader.displayImage((String) data.get("HaberImage"),
				holder.kategoriHaberiResim, AnasayfaHaberAdapter.imageOptions);
		return vi;
	}

	@Override
	public int getCount() {
		return arrayHaber.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		return arrayHaber.get(position);
	}
}
