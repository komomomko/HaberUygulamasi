package com.haberuygulamasi.adapter;

import com.haberuygulamasi.fragment.AnasayfaCaptionFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AnasayfaPagerAdapter extends FragmentPagerAdapter {
	int limitCaptions;

	public AnasayfaPagerAdapter(FragmentManager fm, int limit) {
		super(fm);
		limitCaptions = limit;
	}

	@Override
	public Fragment getItem(int position) {
		return AnasayfaCaptionFragment.newInstance(position);
	}

	@Override
	public int getCount() {
		return limitCaptions;
	}
}