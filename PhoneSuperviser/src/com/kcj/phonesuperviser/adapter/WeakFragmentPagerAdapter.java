package com.kcj.phonesuperviser.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * @ClassName: WeakFragmentPagerAdapter
 * @Description: 
 * @author: KouChengjian
 * @date: 5015-5-23
 */
abstract public class WeakFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<WeakReference<Fragment>> mList = new ArrayList<WeakReference<Fragment>>();

	public List<WeakReference<Fragment>> getFragments() {
		for (int i = mList.size() - 1; i >= 0; --i) {
			if (mList.get(i).get() == null) {
				mList.remove(i);
			}
		}

		return mList;
	}

	protected void saveFragment(Fragment fragment) {
		if (fragment == null) {
			return;
		}

		for (WeakReference<Fragment> item : mList) {
			if (item.get() == fragment) {
				return;
			}
		}

		mList.add(new WeakReference<Fragment>(fragment));
	}

	protected WeakFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

}
