package com.haberuygulamasi.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.haberuygulamasi.AnaEkranActivity;
import com.haberuygulamasi.YouTubePlayerActivity;
import com.haberuygulamasi.adapter.GridViewAdapter;
import com.haberuygulamasi.json.JSONHaber;
import com.haberuygulamasi.json.JSONVideoGaleri;
import com.kodlab.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

public class VideoGaleriFragment extends Fragment {
	private Activity activity;
	private GridView gridView;
	private JSONVideoGaleri jsonVideolar;
	private GridViewAdapter adapterGrid;
	private ArrayList<HashMap<String, Object>> arrayVideoList;
	private int videoOffset = 0;
	private int videoLimit = 20;
	private int position;
	private boolean isOK = true;
	private boolean isMore = true;
	private String urlVideoGaleri = "http://www.teknolojioku.com/index.php?page=42&param=get_videos&l="
			+ videoLimit + "&o=";
	private String youtubeID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static VideoGaleriFragment newInstance() {
		VideoGaleriFragment frag = new VideoGaleriFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		View view = inflater.inflate(R.layout.gridview, container, false);
		TextView tvGridMainTitle = (TextView) view
				.findViewById(R.id.gridViewTitle);
		tvGridMainTitle.setText(R.string.yan_video);
		gridView = (GridView) view.findViewById(R.id.gridView);
		getVideoGaleri();

		gridView.setOnScrollListener(onScrollListener);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				HashMap<String, Object> data = arrayVideoList.get(pos);
				youtubeID = (String) data.get("youtubeID");
				AnaEkranActivity.arrangeTop("NORMAL");
				gotoVideoGaleri();
			}
		});
		return view;
	}

	private void getVideoGaleri() {
		arrayVideoList = new ArrayList<HashMap<String, Object>>();
		Gson gson = new Gson();
		jsonVideolar = new JSONVideoGaleri();
		try {
			jsonVideolar = gson.fromJson(
					JSONHaber.jsonCoord(urlVideoGaleri + videoOffset),
					JSONVideoGaleri.class);
			if (jsonVideolar.row.length < videoLimit) {
				isMore = false;
				isOK = false;
			} else {
				videoOffset += videoLimit;
			}
			addJsontoList(jsonVideolar);
			adapterGrid = new GridViewAdapter(activity, arrayVideoList,
					"VideoList");
			gridView.setAdapter(adapterGrid);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			if (gridView.getLastVisiblePosition() + 1 == arrayVideoList.size()) {
				loadMoreVideo();
			}
		}
	};

	public void addJsontoList(JSONVideoGaleri jFoto) {
		for (int i = 0; i < jFoto.row.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("VideoGaleriTitle", jFoto.row[i].title);
			map.put("VideoGaleriImage", jFoto.row[i].image);
			String youtubeID = jFoto.row[i].orginal_link.replace(
					"http://www.youtube.com/watch?v=", "");
			map.put("youtubeID", youtubeID);
			arrayVideoList.add(map);
		}
	}

	public void loadMoreVideo() {
		if (isOK & isMore) {
			Log.i("Load More", "Activated");
			isOK = false;
			new loadMoreVideoTask().execute();
		} else if (!isOK) {
			Log.i("Too much", "Request");
		} else {
			Toast.makeText(getActivity(), R.string.no_more_photos,
					Toast.LENGTH_SHORT).show();
		}
	}

	private class loadMoreVideoTask extends AsyncTask<String, Void, Void> {
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
				JSONVideoGaleri moreNews = myGson.fromJson(
						JSONHaber.jsonCoord(urlVideoGaleri + videoOffset),
						JSONVideoGaleri.class);
				if (moreNews.row.length < videoLimit) {
					isMore = false;
				} else {
					videoOffset += 20;
				}
				addJsontoList(moreNews);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				adapterGrid.notifyDataSetChanged();
				if (pDialog.isShowing()) {
					try {
						pDialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			isOK = true;
		}
	}

	private void gotoVideoGaleri() {
		new gotoVideoGaleriTask().execute();
	}

	private class gotoVideoGaleriTask extends AsyncTask<String, Void, Void> {

		private final ProgressDialog pDialog = new ProgressDialog(activity);

		@Override
		protected void onPreExecute() {
			pDialog.setMessage(getText(R.string.loading));
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			Log.i("youtubeID"+youtubeID,"youtubeID");
			Intent gotoVideoPager = new Intent(null, Uri.parse("ytv://"
					+ youtubeID), getActivity(),
					YouTubePlayerActivity.class);
			startActivity(gotoVideoPager);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
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
