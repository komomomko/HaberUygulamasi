package com.haberuygulamasi.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.haberuygulamasi.AnaEkranActivity;
import com.haberuygulamasi.adapter.KategoriHaberiAdapter;
import com.haberuygulamasi.json.JSONHaber;
import com.kodlab.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class KategoriHaberiFragment extends Fragment {
	private Activity activity;
	private ArrayList<HashMap<String, Object>> arrayHaberList;
	private JSONHaber jsonHaberler;
	private KategoriHaberiAdapter adapterHaber;
	private TextView haberKategoriTitle;
	private ListView listGundem;
	private Button btnLoadMore;
	private String haberKategoriID;
	private int offset = 0, haberLimit = 25;
	private String urlHaberKategori = "http://www.teknolojioku.com/index.php?page=42&param=get_news&category_id=";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		View view = inflater.inflate(R.layout.listview_with_title, container,
				false);
		listGundem = (ListView) view.findViewById(R.id.haberKategoriList);
		haberKategoriTitle = (TextView) view
				.findViewById(R.id.haberKategorisiTitle);
		haberKategoriID = getArguments().getString("HaberKategoriID");
		haberKategoriTitle.setText(getArguments().getString("HaberKategori"));
		btnLoadMore = new Button(activity.getApplicationContext());
		btnLoadMore.setText(R.string.load_more);
		btnLoadMore.setBackgroundResource(R.layout.load_more);
		btnLoadMore.setTextColor(Color.WHITE);
		listGundem.addFooterView(btnLoadMore);
		AnaEkranActivity.arrangeTop("NORMAL");
		getGundem();

		btnLoadMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreNews();
			}
		});
		listGundem.setOnItemClickListener(onItemClickListener);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void getGundem() {
		arrayHaberList = new ArrayList<HashMap<String, Object>>();
		Gson myGson = new Gson();
		jsonHaberler = new JSONHaber();
		try {
			jsonHaberler = myGson.fromJson(
					JSONHaber.jsonCoord(urlHaberKategori + haberKategoriID),
					JSONHaber.class);
			adapterHaber = new KategoriHaberiAdapter(activity,
					android.R.layout.simple_list_item_1,
					R.id.haberKategoriList, arrayHaberList);

			addJsontoList(jsonHaberler);
			listGundem.setAdapter(adapterHaber);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Fragment newInstance() {
		KategoriHaberiFragment frag = new KategoriHaberiFragment();
		return frag;
	}

	public void loadMoreNews() {
		new loadMoreNewsTask().execute();
	}

	private class loadMoreNewsTask extends AsyncTask<String, Void, Void> {

		private final ProgressDialog pDialog = new ProgressDialog(activity);

		@Override
		protected void onPreExecute() {
			pDialog.setMessage(getText(R.string.loading));
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			SystemClock.sleep(50);
			Gson myGson = new Gson();
			try {
				offset += haberLimit;
				JSONHaber moreNews = myGson.fromJson(
						JSONHaber.jsonCoord(urlHaberKategori + haberKategoriID
								+ "&l=" + haberLimit + "&o=" + offset),
						JSONHaber.class);
				addJsontoList(moreNews);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapterHaber.notifyDataSetChanged();
			if (pDialog.isShowing()) {
				try {
					pDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void gotoHaberDetay(HashMap<String, String> hashmap) {
		new gotoHaberDetayTask(hashmap).execute();
	}

	private class gotoHaberDetayTask extends AsyncTask<String, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(activity);
		public HashMap<String, String> data;

		public gotoHaberDetayTask(HashMap<String, String> hashmap) {
			data = hashmap;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage(getText(R.string.loading));
			dialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			SystemClock.sleep(50);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			HaberDetayFragment newFragment = HaberDetayFragment.newInstance();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			Bundle args = new Bundle();
			args.putString("HaberURL", data.get("HaberURL"));
			args.putString("urlHaber", data.get("urlHaber"));
			args.putString("HaberTitle", data.get("HaberTitle"));
			newFragment.setArguments(args);
			transaction.replace(R.id.fragment_placeholder, newFragment);
			transaction.addToBackStack("tag").commit();

			if (dialog.isShowing()) {
				try {
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> data = (HashMap<String, String>) listGundem
					.getItemAtPosition(pos);
			gotoHaberDetay(data);
		}
	};

	public void addJsontoList(JSONHaber jHaber) {
		for (int i = 0; i < jHaber.row.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("HaberURL", jHaber.row[i].url);
			map.put("HaberTitle", jHaber.row[i].title);
			map.put("HaberImage", jHaber.row[i].image);
			map.put("urlHaber", jHaber.row[i].mobil_detail_url);

			arrayHaberList.add(map);
		}
	}

}
