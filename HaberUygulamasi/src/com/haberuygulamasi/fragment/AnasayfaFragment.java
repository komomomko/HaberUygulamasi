package com.haberuygulamasi.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.haberuygulamasi.adapter.AnasayfaHaberAdapter;
import com.haberuygulamasi.adapter.AnasayfaPagerAdapter;
import com.haberuygulamasi.json.JSONHaber;
import com.kodlab.R;
import com.viewpagerindicator.CirclePageIndicator;

public class AnasayfaFragment extends Fragment {
	private ViewPager pager;
	private CirclePageIndicator indicator;
	private Activity activity;
	private AnasayfaPagerAdapter adapterPager;
	private AnasayfaHaberAdapter adapterHaber;
	private ListView listHaber;
	private Button btnLoadMore;
	private JSONHaber jsonHaberler;
	private ArrayList<HashMap<String, Object>> arrayHaberList,
			arrayCaptionList;
	private HashMap<String, String> data;
	public static JSONHaber jsonUstHaber;
	private int offsetFirst = 10, haberLimit = 25, pagerPosition;
	private int offset;
	private String urlHaber = "http://www.teknolojioku.com/index.php?page=42&param=get_captions&l="
			+ haberLimit + "&o=";
	private String urlCaptions = "http://www.teknolojioku.com/index.php?page=42&param=get_captions&l="
			+ offsetFirst;
	private float oldX = 0, newX, sens = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static Fragment newInstance() {
		AnasayfaFragment frag = new AnasayfaFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		View view = inflater.inflate(R.layout.anasayfa, container, false);
		listHaber = (ListView) view.findViewById(R.id.listHaber);
		pager = (ViewPager) view.findViewById(R.id.viewpager);
		indicator = (CirclePageIndicator) view
				.findViewById(R.id.circleIndicator);
		pager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					oldX = event.getX();
					break;

				case MotionEvent.ACTION_UP:
					newX = event.getX();
					if (Math.abs(oldX - newX) < sens) {
						new MyAsyncTask("pager").execute();
						return true;
					}
					oldX = 0;
					newX = 0;
					break;
				}
				return false;
			}
		});

		btnLoadMore = new Button(activity.getApplicationContext());
		btnLoadMore.setText(R.string.load_more);
		btnLoadMore.setBackgroundResource(R.layout.load_more);
		btnLoadMore.setTextColor(Color.WHITE);
		listHaber.addFooterView(btnLoadMore);
		arrayHaberList = new ArrayList<HashMap<String, Object>>();
		arrayCaptionList = new ArrayList<HashMap<String, Object>>();
		getNews("ilk");
		indicator.setOnPageChangeListener(onPageChangeListener);
		btnLoadMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new MyAsyncTask("loadMore").execute();
			}
		});
		return view;
	}

	private void getNews(String situation) {
		offset = offsetFirst;
		try {
			Gson myGson = new Gson();

			jsonUstHaber = myGson.fromJson(JSONHaber.jsonCoord(urlCaptions),
					JSONHaber.class);
			jsonHaberler = myGson.fromJson(
					JSONHaber.jsonCoord(urlHaber + offsetFirst),
					JSONHaber.class);

			addJsontoList(jsonHaberler, "haber");
			addJsontoList(jsonUstHaber, "caption");

			if (situation == "ilk") {
				adapterHaber = new AnasayfaHaberAdapter(activity,
						android.R.layout.simple_list_item_1, R.id.listHaber,
						arrayHaberList);
				listHaber.setAdapter(adapterHaber);

				adapterPager = new AnasayfaPagerAdapter(
						getChildFragmentManager(), offsetFirst);
				pager.setAdapter(adapterPager);
				indicator.setViewPager(pager);

				getActivity().findViewById(R.id.appRefresh).setOnClickListener(
						onClickListener);
				listHaber.setOnItemClickListener(onItemClickListener);
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void gotoHaberDetay(String string) {
		new MyAsyncTask(string).execute();
	}

	private class MyAsyncTask extends AsyncTask<String, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(activity);
		public String asyncType;

		public MyAsyncTask(String string) {
			asyncType = string;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage(getText(R.string.loading));
			dialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			if (asyncType.equals("refresh")) {
				getNews("refresh");
			} else if (asyncType.equals("loadMore")) {
				SystemClock.sleep(50);
				Gson myGson = new Gson();
				try {
					offset += haberLimit;
					JSONHaber moreNews = myGson.fromJson(
							JSONHaber.jsonCoord(urlHaber + offset),
							JSONHaber.class);
					addJsontoList(moreNews, "haber");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				SystemClock.sleep(50);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (asyncType.equals("loadMore")) {
				adapterHaber.notifyDataSetChanged();
			} else if (asyncType.equals("refresh")) {
				adapterHaber.notifyDataSetChanged();
				adapterPager.notifyDataSetChanged();
				listHaber.setSelection(0);
				pager.setCurrentItem(0);
			} else if (asyncType.equals("pager") || asyncType.equals("haber")) {
				HaberDetayFragment newFragment = HaberDetayFragment
						.newInstance();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				Bundle args = new Bundle();
				if (asyncType.equals("pager")) {
					args.putString("HaberURL",
							jsonUstHaber.row[pagerPosition].url);
					args.putString("urlHaber",
							jsonUstHaber.row[pagerPosition].mobil_detail_url);
					args.putString("HaberTitle",
							jsonUstHaber.row[pagerPosition].title);
				} else {
					args.putString("HaberURL", data.get("HaberURL"));
					args.putString("urlHaber", data.get("urlHaber"));
					args.putString("HaberTitle", data.get("HaberTitle"));
				}
				newFragment.setArguments(args);
				transaction.replace(R.id.fragment_placeholder, newFragment);
				transaction.addToBackStack("tag").commit();
			}
			if (dialog.isShowing()) {
				try {
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void addJsontoList(JSONHaber jHaber, String string) {
		for (int i = 0; i < jHaber.row.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("HaberURL", jHaber.row[i].url);
			map.put("HaberTitle", jHaber.row[i].title);
			map.put("Image", jHaber.row[i].image);
			map.put("urlHaber", jHaber.row[i].mobil_detail_url);
			map.put("Category", jHaber.row[i].category_name + " - "
					+ jHaber.row[i].publish_date);
			if (string.equals("haber")) {
				arrayHaberList.add(map);
			} else if (string.equals("caption")) {
				arrayCaptionList.add(map);
			}
		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			data = (HashMap<String, String>) listHaber.getItemAtPosition(pos);
			gotoHaberDetay("haber");
		}
	};

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			pagerPosition = position;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			arrayHaberList.clear();
			new MyAsyncTask("refresh").execute();
		}
	};

}
