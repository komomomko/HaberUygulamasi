package com.haberuygulamasi.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.haberuygulamasi.adapter.KategoriHaberiAdapter;
import com.haberuygulamasi.json.JSONArama;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;

public class AramaFragment extends Fragment {
	private Activity activity;
	private ArrayList<HashMap<String, Object>> arraySearchList;
	private JSONArama jsonSearch;
	private KategoriHaberiAdapter adapterSearch;
	private ListView listHaber;
	private Button btnLoadMore;
	private int offsetFirst = 0, haberLimit = 25;
	private int offset;
	private String urlSearch = "http://www.teknolojioku.com/index.php?page=42&param=get_search&l=";
	private String searchTerm;
	private EditText searchBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		View view = inflater.inflate(R.layout.search, container, false);
		listHaber = (ListView) view.findViewById(R.id.searchList);
		searchBar = (EditText) view.findViewById(R.id.searchBar);
		btnLoadMore = new Button(activity.getApplicationContext());
		btnLoadMore.setText(R.string.load_more);
		btnLoadMore.setBackgroundResource(R.layout.load_more);
		btnLoadMore.setTextColor(Color.WHITE);
		listHaber.addFooterView(btnLoadMore);
		arraySearchList = new ArrayList<HashMap<String, Object>>();
		searchBar.setOnEditorActionListener(onEditorActionListener);
		btnLoadMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new MyAsyncTask("loadMore").execute();
			}
		});
		listHaber.setOnItemClickListener(onItemClickListener);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void getSearch(String tag) {
		offset = offsetFirst;
		Gson myGson = new Gson();
		jsonSearch = new JSONArama();
		try {
			jsonSearch = myGson.fromJson(
					JSONHaber.jsonCoord(urlSearch + haberLimit + "&o="
							+ offsetFirst + "&news_keys=" + searchTerm),
					JSONArama.class);
			if (jsonSearch.row.length < haberLimit) {
				btnLoadMore.setVisibility(View.GONE);
			}
			addJsontoList(jsonSearch);
			if (tag.equals("ilk")) {
				adapterSearch = new KategoriHaberiAdapter(activity,
						android.R.layout.simple_list_item_1, R.id.haberKategoriList,
						arraySearchList);
				listHaber.setAdapter(adapterSearch);
				getActivity().findViewById(R.id.appRefresh).setOnClickListener(
						onClickListener);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Fragment newInstance() {
		AramaFragment frag = new AramaFragment();
		return frag;
	}

	private void gotoHaberDetay(String string) {
		new MyAsyncTask(string).execute();
	}

	private class MyAsyncTask extends AsyncTask<String, Void, Void> {

		private final ProgressDialog pDialog = new ProgressDialog(activity);
		String asyncType;

		private MyAsyncTask(String situation) {
			asyncType = situation;
		}

		@Override
		protected void onPreExecute() {
			pDialog.setMessage(getText(R.string.loading));
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			if (asyncType.equals("loadMore")) {
				SystemClock.sleep(50);
				Gson myGson = new Gson();
				try {
					offset += haberLimit;
					JSONArama moreNews = myGson.fromJson(
							JSONHaber.jsonCoord(urlSearch + haberLimit + "&o="
									+ offset + "&news_keys=" + searchTerm),
							JSONArama.class);
					if (moreNews.row.length < haberLimit) {
						btnLoadMore.setVisibility(View.GONE);
					}
					addJsontoList(moreNews);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (asyncType.equals("refresh")) {
				getSearch("refresh");
			} else {
				SystemClock.sleep(50);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (asyncType.equals("loadMore")) {
				adapterSearch.notifyDataSetChanged();
			} else if (asyncType.equals("refresh")) {
				adapterSearch.notifyDataSetChanged();
				listHaber.setSelection(0);
			} else {
				HaberDetayFragment newFragment = HaberDetayFragment
						.newInstance();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();

				Bundle args = new Bundle();
				args.putString("HaberID", asyncType);
				newFragment.setArguments(args);
				transaction.replace(R.id.fragment_placeholder, newFragment);
				transaction.addToBackStack("tag").commit();
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

	private void addJsontoList(JSONArama jHaber) {
		for (int i = 0; i < jHaber.row.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("HaberID", jHaber.row[i].id);
			map.put("HaberTitle", jHaber.row[i].title);
			map.put("HaberImage", jHaber.row[i].image);

			arraySearchList.add(map);
		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> data = (HashMap<String, String>) listHaber
					.getItemAtPosition(pos);
			gotoHaberDetay(data.get("HaberID"));
		}
	};
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.appRefresh:
				arraySearchList.clear();
				new MyAsyncTask("refresh").execute();
			case R.id.searchIcon:
				arraySearchList.clear();
				searchTerm = searchBar.getText().toString();
				getSearch("ilk");
			}
		}
	};
	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			Log.i("ads", "asd");
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				arraySearchList.clear();
				Log.i("ads", "asd");
				searchTerm = v.getText().toString();
				getSearch("ilk");
			}
			return false;
		}
	};
}
