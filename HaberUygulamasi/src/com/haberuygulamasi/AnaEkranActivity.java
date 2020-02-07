package com.haberuygulamasi;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.haberuygulamasi.adapter.LeftPanelAdapter;
import com.haberuygulamasi.fragment.AnasayfaFragment;
import com.haberuygulamasi.fragment.AramaFragment;
import com.haberuygulamasi.fragment.FotoGaleriFragment;
import com.haberuygulamasi.fragment.KategoriHaberiFragment;
import com.haberuygulamasi.fragment.VideoGaleriFragment;
import com.haberuygulamasi.json.JSONHaber;
import com.haberuygulamasi.json.JSONKategoriler;
import com.kodlab.R;
import com.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AnaEkranActivity extends FragmentActivity {
	private ImageView ivAppMenu, ivSearchButton;
	private ListView listKategori;
	private static String pageType = "";
	private static ImageView ivAppRefresh, ivAppBack;
	private Activity activity;
	private View headerView;
	private JSONKategoriler jsonHaberKategorileri;
	private ArrayList<String> arrayListCategory = new ArrayList<String>();
	private String urlHaberKategorileri = "http://www.teknolojioku.com/index.php?page=42&param=get_categories";
	private LeftPanelAdapter panelAdapter;
	private SlidingMenu slidingMenu;
	String strGetCategories = "getCategories";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ana_ekran_container);

		activity = AnaEkranActivity.this;
		slidingMenu = (SlidingMenu) findViewById(R.id.blahblah);
		slidingMenu.setSlidingEnabled(false);
		slidingMenu.setViewAbove(R.layout.normal_panel);
		slidingMenu.setViewBehind(R.layout.left_panel);
		slidingMenu.showAbove();

		headerView = getLayoutInflater().inflate(R.layout.left_panel_top, null);
		ivSearchButton = (ImageView) headerView
				.findViewById(R.id.ivSearchButton);

		ivAppRefresh = (ImageView) findViewById(R.id.appRefresh);
		ivAppMenu = (ImageView) findViewById(R.id.appMenu);
		listKategori = (ListView) findViewById(R.id.list_yan_kategori);
		listKategori.setOnItemClickListener(onItemClickListener);
		updateDisplay();

		ivAppMenu.setOnClickListener(onClickListener);
		ivAppRefresh.setOnClickListener(onClickListener);
		ivSearchButton.setOnClickListener(onClickListenerforFragment);
	}

	private void updateDisplay() {
		new GetReaderContentTask().execute();
	}

	private class GetReaderContentTask extends AsyncTask<String, Void, Void> {
		private final ProgressDialog pDialog = new ProgressDialog(activity);

		@Override
		protected void onPreExecute() {
			pDialog.setMessage(getText(R.string.loading));
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			Gson gson = new Gson();
			try {
				jsonHaberKategorileri = gson.fromJson(
						JSONHaber.jsonCoord(urlHaberKategorileri),
						JSONKategoriler.class);
				arrayListCategory.add(getString(R.string.cat_anasayfa));
				arrayListCategory.add(getString(R.string.cat_teknoloji_tv));
				arrayListCategory.add(getString(R.string.cat_foto_galeri));
				for (int i = 0; i < jsonHaberKategorileri.row.length; i++) {
					arrayListCategory.add(jsonHaberKategorileri.row[i].name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			panelAdapter = new LeftPanelAdapter(activity, arrayListCategory);
			listKategori.setAdapter(panelAdapter);
			Fragment newFragment = AnasayfaFragment.newInstance();
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_placeholder, newFragment)
					.commit();
			if (pDialog.isShowing()) {
				try {
					pDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void openExplicitsURL(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	public OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.appMenu:
				if (slidingMenu.isBehindShowing()) {
					slidingMenu.showAbove();
				} else {
					slidingMenu.showBehind();
				}
				break;
			case R.id.appRefresh:
				FragmentManager fManager = getSupportFragmentManager();
				Fragment currentFragment = fManager
						.findFragmentById(R.id.fragment_placeholder);
				FragmentTransaction transaction2 = getSupportFragmentManager()
						.beginTransaction();
				transaction2
						.replace(R.id.fragment_placeholder, currentFragment)
						.commit();
				break;
			}
		}
	};
	private OnClickListener onClickListenerforFragment = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new MyAsyncTask(v.getId()).execute();
		}
	};

	private class MyAsyncTask extends AsyncTask<String, Void, Void> {
		int layoutID;

		public MyAsyncTask(int layID) {
			layoutID = layID;
		}

		private final ProgressDialog pDialog = new ProgressDialog(activity);

		@Override
		protected void onPreExecute() {
			pDialog.setMessage(getText(R.string.loading));
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			SystemClock.sleep(50);
			Fragment newFragment = null;
			switch (layoutID) {
			case R.id.ivSearchButton:
				newFragment = AramaFragment.newInstance();
				break;
			}
			if (newFragment != null) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.fragment_placeholder, newFragment);
				transaction.addToBackStack(null).commit();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				slidingMenu.showAbove();
				if (pDialog.isShowing()) {
					pDialog.dismiss();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void arrangeTop(String durum) {
		try {
			if (durum.equals(pageType)) {
				// Deðiþiklik yapma
			} else {
				pageType = durum;
				if (durum.equals("ANASAYFA")) {
					Log.i("Anasayfa Style", "Anasayfa Style");
					ivAppRefresh.setVisibility(View.VISIBLE);
					ivAppBack.setVisibility(View.VISIBLE);

				} else if (durum.equals("NORMAL")) {
					Log.i("Normal Style", "Normal Style");
					ivAppRefresh.setVisibility(View.VISIBLE);
					ivAppBack.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			Fragment newFragment = null;
			Bundle args = new Bundle();
			Log.i("pos:" + pos, "pos");
			if (pos == 0) {
				newFragment = AnasayfaFragment.newInstance();
			} else if (pos == 1) {
				newFragment = VideoGaleriFragment.newInstance();
			} else if (pos == 2) {
				newFragment = FotoGaleriFragment.newInstance();
			} else {
				args.putString("HaberKategoriID",
						jsonHaberKategorileri.row[pos - 3].id);
				args.putString("HaberKategori",
						jsonHaberKategorileri.row[pos - 3].name);

				newFragment = KategoriHaberiFragment.newInstance();
			}
			newFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_placeholder, newFragment);
			transaction.addToBackStack(null).commit();
			try {
				slidingMenu.showAbove();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onBackPressed() {
		if (slidingMenu.isBehindShowing()) {
			slidingMenu.showAbove();
		} else {
			super.onBackPressed();
		}
	}
}
