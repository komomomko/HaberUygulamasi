package com.haberuygulamasi.adapter;

import java.util.ArrayList;

import com.kodlab.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* ***Anasayfadaki haberlerin listelenmiþ þekilde getirilmesini saðlar*** */
public class LeftPanelAdapter extends BaseAdapter {
	Activity activity;
	ArrayList<String> arrayListCategory;
	public static DisplayImageOptions imageOptions;
	public TextView categoryText;

	public LeftPanelAdapter(Activity a, ArrayList<String> aListCategory) {
		arrayListCategory = aListCategory;
		activity = a;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.left_panel_list_item, parent, false);
			categoryText = (TextView) vi
					.findViewById(R.id.left_panel_category_text);
		}
		categoryText.setText(arrayListCategory.get(position));
		return vi;
	}

	@Override
	public int getCount() {
		return arrayListCategory.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
}
