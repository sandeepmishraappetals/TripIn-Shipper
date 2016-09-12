package tripin.com.tripin_shipper.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.fragment.TodayFragment;
import tripin.com.tripin_shipper.fragment.TomorrowFragment;

/**
 * Created by Android on 09/09/16.
 */
public class Activity_Tuck12 extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.truck12);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Today"+ "/n"+" 23/02/2016", null),
             //   TodayFragment.class, null);
                TodayFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Tomorrow", null),
                TomorrowFragment.class, null);
      /*  mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Tab 3", null),
                FragmentTab.class, null);*/
    }
}