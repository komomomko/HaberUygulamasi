package com.haberuygulamasi.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.haberuygulamasi.json.JSONVideoGaleriIcindekiler;
import com.kodlab.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class VideoPagerAdapter extends PagerAdapter {
	JSONVideoGaleriIcindekiler jsonVideoGalerisi;
	ImageLoader videoImageLoader = ImageLoader.getInstance();
	Activity activity;
	LayoutInflater inflater;
	DisplayImageOptions options;
	ProgressBar spinner;

	public VideoPagerAdapter(Activity a, JSONVideoGaleriIcindekiler jVideoGalerisi) {
		activity = a;
		jsonVideoGalerisi = jVideoGalerisi;
		inflater = activity.getLayoutInflater();
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public int getCount() {
		return jsonVideoGalerisi.row.length;
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = inflater.inflate(R.layout.image_pager_item, view,
				false);
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
		videoImageLoader.displayImage(jsonVideoGalerisi.row[position].image,
				imageView, options, imageLoadingListener);
		((ViewPager) view).addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}

	public SimpleImageLoadingListener imageLoadingListener = new SimpleImageLoadingListener() {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			spinner.setVisibility(View.VISIBLE);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			String message = null;
			switch (failReason.getType()) {
			case IO_ERROR:
				message = "Input/Output error";
				break;
			case DECODING_ERROR:
				message = "Image can't be decoded";
				break;
			case NETWORK_DENIED:
				message = "Downloads are denied";
				break;
			case OUT_OF_MEMORY:
				message = "Out Of Memory error";
				break;
			case UNKNOWN:
				message = "Unknown error";
				break;
			}
			Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			spinner.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			spinner.setVisibility(View.GONE);
		}
	};
}
