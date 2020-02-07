package com.haberuygulamasi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.google.gson.Gson;
import com.haberuygulamasi.adapter.ImagePagerAdapter;
import com.haberuygulamasi.json.JSONFotografGaleriIcindekiler;
import com.haberuygulamasi.json.JSONHaber;
import com.kodlab.R;

public class ImagePager extends Activity {
	private ViewPager pager;
	private String galeriID;
	private String urlImageGallery = "http://www.teknolojioku.com/index.php?page=42&param=get_gallery_details&gallery_id=";
	private Activity activity;
	private Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_pager);
		activity = ImagePager.this;
		intent = getIntent();
		galeriID = intent.getStringExtra("GaleriID");
		getPictures();
	}

	private void getPictures() {
		Gson gson = new Gson();
		JSONFotografGaleriIcindekiler jsonFotoGaleri = new JSONFotografGaleriIcindekiler();
		try {
			jsonFotoGaleri = gson.fromJson(
					JSONHaber.jsonCoord(urlImageGallery + galeriID),
					JSONFotografGaleriIcindekiler.class);
			pager = (ViewPager) findViewById(R.id.pagerImage);
			pager.setAdapter(new ImagePagerAdapter(activity, jsonFotoGaleri));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}