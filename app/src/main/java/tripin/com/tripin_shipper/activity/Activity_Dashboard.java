package tripin.com.tripin_shipper.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.adapter.ViewPagerAdapter;
import tripin.com.tripin_shipper.fragment.AddressesFragment;
import tripin.com.tripin_shipper.fragment.FirstFragment;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.widget.SlidingTabLayout;

/**
 * Created by Android on 30/08/16.
 */
public class Activity_Dashboard extends ActionBarActivity implements FirstFragment.OnFragmentInteractionListener{

    // Declaring Your View and Variables
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"BOOK NOW","TRUCK STATUS"};
    int Numboftabs =2;
    public AddressesFragment addressesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_land);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(android.R.color.holo_blue_light);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        AddressList.mainActivity = this;
        //setAddressesFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void setAddressesFragment(int placer) {
        addressesFragment = new AddressesFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(placer, addressesFragment)
                .addToBackStack(null)
                .commit();
    }

}
