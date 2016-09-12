package tripin.com.tripin_shipper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tripin.com.tripin_shipper.fragment.TodayFragment;
import tripin.com.tripin_shipper.fragment.TomorrowFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new TodayFragment();
		case 1:
			// Games fragment activity
			return new TomorrowFragment();
	/*	case 2:
			// Movies fragment activity
			return new MoviesFragment();*/
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
