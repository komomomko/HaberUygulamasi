package com.haberuygulamasi;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.haberuygulamasi.json.JSONKategoriler;
import com.kodlab.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

public class Splash extends Activity {
	private Context context;
	public static JSONKategoriler jsonHaberKategorileri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		checkForStrictMode();
		context = Splash.this;
		checkConn(context);
	}

	private void checkConn(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null || !i.isConnected() || !i.isAvailable()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			TextView msg = new TextView(context);
			msg.setText(R.string.no_connection);
			msg.setGravity(Gravity.CENTER);
			msg.setTextSize(18);
			msg.setPadding(15, 15, 15, 15);
			builder.setView(msg);
			builder.setPositiveButton(R.string.try_again,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							checkConn(context);
						};
					});
			builder.setNegativeButton(R.string.quit_app, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.show();
		} else {
			getHaberKategorileri();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	private void getHaberKategorileri() {
		new getHaberKategorileriTask().execute();
	}

	private class getHaberKategorileriTask extends
			AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent aIntent = new Intent(Splash.this, AnaEkranActivity.class);
			Splash.this.startActivity(aIntent);
		}

		@Override
		protected Void doInBackground(String... params) {
			SystemClock.sleep(1000);
			return null;
		}
	}
	private void checkForStrictMode() {
		try {
			Class<?> strictModeClass = Class.forName("android.os.StrictMode",
					true, Thread.currentThread().getContextClassLoader());

			Class<?> threadPolicyClass = Class.forName(
					"android.os.StrictMode$ThreadPolicy", true, Thread
							.currentThread().getContextClassLoader());

			Class<?> threadPolicyBuilderClass = Class.forName(
					"android.os.StrictMode$ThreadPolicy$Builder", true, Thread
							.currentThread().getContextClassLoader());

			Method setThreadPolicyMethod = strictModeClass.getMethod(
					"setThreadPolicy", threadPolicyClass);

			Method detectAllMethod = threadPolicyBuilderClass
					.getMethod("detectAll");
			Method penaltyMethod = threadPolicyBuilderClass
					.getMethod("penaltyLog");
			Method buildMethod = threadPolicyBuilderClass.getMethod("build");

			Constructor<?> threadPolicyBuilderConstructor = threadPolicyBuilderClass
					.getConstructor();
			Object threadPolicyBuilderObject = threadPolicyBuilderConstructor
					.newInstance();

			Object obj = detectAllMethod.invoke(threadPolicyBuilderObject);

			obj = penaltyMethod.invoke(obj);
			Object threadPolicyObject = buildMethod.invoke(obj);
			setThreadPolicyMethod.invoke(strictModeClass, threadPolicyObject);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}