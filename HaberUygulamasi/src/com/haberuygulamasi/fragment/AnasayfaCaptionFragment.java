package com.haberuygulamasi.fragment;

import com.haberuygulamasi.adapter.AnasayfaHaberAdapter;
import com.kodlab.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AnasayfaCaptionFragment extends Fragment {
	private TextView tvBaslik;
	private ImageView ivHaberResim;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt("page");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public static Fragment newInstance(int pos) {
		AnasayfaCaptionFragment frag = new AnasayfaCaptionFragment();
		Bundle args = new Bundle();
		args.putInt("page", pos);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (AnasayfaHaberAdapter.imageOptions == null) {
			AnasayfaHaberAdapter.imageOptions = new DisplayImageOptions.Builder()
					.resetViewBeforeLoading()
					.cacheInMemory().cacheOnDisc().build();
		}
		View view = inflater.inflate(R.layout.anasayfa_captions, container,
				false);
		tvBaslik = (TextView) view.findViewById(R.id.ustHaberBaslik);
		ivHaberResim = (ImageView) view.findViewById(R.id.usthaberResmi);
		try {
			tvBaslik.setText(AnasayfaFragment.jsonUstHaber.row[position].title);
			ivHaberResim
					.setTag(AnasayfaFragment.jsonUstHaber.row[position].image);
			imageLoader.displayImage(
					AnasayfaFragment.jsonUstHaber.row[position].image,
					ivHaberResim, AnasayfaHaberAdapter.imageOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}
