package com.haberuygulamasi.fragment;

import com.kodlab.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ImageView;

public class HaberDetayFragment extends Fragment {
	private ImageView haberShare, haberTextBuyut, haberTextKucult;
	private WebView webView;
	private WebSettings webSetting;
	private String urlHaber;
	private int textSize;
	private String haberURL, haberTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Activity activity = getActivity();
		View view = inflater.inflate(R.layout.haber_detay, container, false);
		haberTextBuyut = (ImageView) view.findViewById(R.id.yazi_buyut_button);
		haberTextKucult = (ImageView) view
				.findViewById(R.id.yazi_kucult_button);
		haberShare = (ImageView) view.findViewById(R.id.share_button);
		webView = (WebView) view.findViewById(R.id.haberDetayWeb);
		urlHaber = getArguments().getString("urlHaber");
		haberTitle = getArguments().getString("HaberTitle");
		haberURL = getArguments().getString("HaberURL");
		getNewsDetail();

		webSetting = webView.getSettings();
		final ProgressDialog pDialog = new ProgressDialog(activity);
		pDialog.setMessage(activity.getText(R.string.loading));
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReachedMaxAppCacheSize(long spaceNeeded,
					long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(spaceNeeded * 2);
			}

			@Override
			public void onProgressChanged(WebView view, int progress) {
				if (progress < 100 && !pDialog.isShowing()) {
					pDialog.show();
				} else if (progress == 100) {
					try {
						pDialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		webSetting.setDomStorageEnabled(true);
		// Set cache size to 8 mb by default. should be more than enough
		webSetting.setAppCacheMaxSize(1024 * 1024 * 8);

		String appCachePath = activity.getCacheDir().getAbsolutePath();

		webSetting.setAppCachePath(appCachePath);
		webSetting.setAllowFileAccess(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);

		if (urlHaber != null) {
			getNewsDetail();
		}
		haberShare.setOnClickListener(onClickListener);
		haberTextBuyut.setOnClickListener(onClickListener);
		haberTextKucult.setOnClickListener(onClickListener);
		return view;
	}

	public static HaberDetayFragment newInstance() {
		HaberDetayFragment frag = new HaberDetayFragment();
		return frag;
	}

	private void getNewsDetail() {
		try {
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			webView.loadUrl(urlHaber);
			webView.setBackgroundColor(0x00000000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.yazi_buyut_button:
				if (textSize < 32) {
					textSize = webSetting.getDefaultFontSize();
					webSetting.setDefaultFixedFontSize(textSize++);
					webSetting.setDefaultFontSize(textSize++);
					Log.i("TextSize: " + textSize, "deneme");
				}
				break;
			case R.id.yazi_kucult_button:
				if (textSize > 12) {
					textSize = webSetting.getDefaultFontSize();
					webSetting.setDefaultFixedFontSize(textSize--);
					webSetting.setDefaultFontSize(textSize--);
					Log.i("TextSize: " + textSize, "deneme");
				}
				break;
			case R.id.share_button:
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				String textToSend = haberTitle + " - " + haberURL;
				sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				break;
			}
		}
	};
}