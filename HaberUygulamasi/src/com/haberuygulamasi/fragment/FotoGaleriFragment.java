package com.haberuygulamasi.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.haberuygulamasi.AnaEkranActivity;
import com.haberuygulamasi.ImagePager;
import com.haberuygulamasi.adapter.GridViewAdapter;
import com.haberuygulamasi.json.JSONFotografGaleri;
import com.haberuygulamasi.json.JSONHaber;
import com.kodlab.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class FotoGaleriFragment extends Fragment {
	private Activity activity;
	private GridView gridView;
	private JSONFotografGaleri jsonFotograflar;
	private GridViewAdapter adapterGrid;
	private ArrayList<HashMap<String, Object>> arrayResimList;
	private int videoOffset = 0;
	private int fotoLimit = 20;
	private int position;
	private boolean isOK = true;
	private boolean isMore = true;
	private String urlGaleri;
	private String strLoadMore = "LoadMore", strGotoDetay = "Detay";
	private String haberID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static FotoGaleriFragment newInstance() {
		FotoGaleriFragment frag = new FotoGaleriFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		try {
			haberID = getArguments().getString("haberID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		urlGaleri = "http://www.teknolojioku.com/index.php?page=42&param=get_galleries&l="
				+ fotoLimit + "&o=";

		View view = inflater.inflate(R.layout.gridview, container, false);
		TextView tvGridMainTitle = (TextView) view
				.findViewById(R.id.gridViewTitle);
		tvGridMainTitle.setText(R.string.yan_fotograflar);

		gridView = (GridView) view.findViewById(R.id.gridView);
		AnaEkranActivity.arrangeTop("NORMAL");
		getFotoGaleri();

		gridView.setOnScrollListener(onScrollListener);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				position = pos;
				gotoResimGaleri();
			}
		});
		return view;
	}

	private void getFotoGaleri() {
		arrayResimList = new ArrayList<HashMap<String, Object>>();
		Gson gson = new Gson();
		jsonFotograflar = new JSONFotografGaleri();

		if (haberID == null || haberID.equals("null")) {
			try {
				Log.i("url:" + urlGaleri + videoOffset, "url");
				jsonFotograflar = gson.fromJson(
						JSONHaber.jsonCoord(urlGaleri + videoOffset),
						JSONFotografGaleri.class);
				if (jsonFotograflar.row.length < fotoLimit) {
					isMore = false;
					isOK = false;
				} else {
					videoOffset += fotoLimit;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				jsonFotograflar = gson.fromJson(JSONHaber.jsonCoord(urlGaleri),
						JSONFotografGaleri.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		addJsontoList(jsonFotograflar);
		adapterGrid = new GridViewAdapter(activity, arrayResimList, "ResimList");
		gridView.setAdapter(adapterGrid);
	}

	private OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			if (gridView.getLastVisiblePosition() + 1 == arrayResimList.size()) {
				loadMoreResim();
			}
		}
	};

	private void addJsontoList(JSONFotografGaleri jFoto) {
		for (int i = 0; i < jFoto.row.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ResimGaleriTitle", jFoto.row[i].title);
			map.put("ResimGaleriImage", jFoto.row[i].image);
			arrayResimList.add(map);
		}
	}

	private void loadMoreResim() {
		if (isOK & isMore) {
			Log.i("Load More", "Activated");
			isOK = false;
			new MyAsyncTask(strLoadMore).execute();
		} else if (!isOK) {
			Log.i("Too much", "Request");
		} else {
			Toast.makeText(getActivity(), R.string.no_more_photos,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void gotoResimGaleri() {
		new MyAsyncTask(strGotoDetay).execute();
	}

	private class MyAsyncTask extends AsyncTask<String, Void, Void> {
		private final ProgressDialog pDialog = new ProgressDialog(activity);
		private String asyncType;

		public MyAsyncTask(String asynctype) {
			asyncType = asynctype;
		}

		@Override
		protected void onPreExecute() {
			pDialog.setMessage(getText(R.string.loading));
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			SystemClock.sleep(50);
			if (asyncType.equals(strLoadMore)) {
				Gson myGson = new Gson();
				try {

					JSONFotografGaleri moreNews = myGson.fromJson(
							JSONHaber.jsonCoord(urlGaleri + videoOffset),
							JSONFotografGaleri.class);
					if (moreNews.row.length < fotoLimit) {
						isMore = false;
					} else {
						videoOffset += 20;
					}
					addJsontoList(moreNews);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (asyncType.equals(strLoadMore)) {
				try {
					adapterGrid.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
				isOK = true;
			} else if (asyncType.equals(strGotoDetay)) {
				Intent gotoImagePager = new Intent(getActivity(),
						ImagePager.class);
				gotoImagePager.putExtra("GaleriID",
						jsonFotograflar.row[position].id);
				startActivity(gotoImagePager);
			}
			if (pDialog.isShowing()) {
				try {
					pDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
