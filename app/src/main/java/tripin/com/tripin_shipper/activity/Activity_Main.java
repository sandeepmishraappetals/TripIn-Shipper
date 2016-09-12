package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.utils.NetworkUtils;
import tripin.com.tripin_shipper.widget.CircleIndicator;

/**
 * Created by SUMEET on 07-08-2016.
 */
public class Activity_Main extends Activity implements View.OnClickListener{
    private List<View> viewList;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    LinearLayout SignUp;
    //  PagerAdapter pagerAdapter;
    public TextView already_text;
    Integer[] image = {
          /*  R.drawable.splash,*/
            R.drawable.view2,
            R.drawable.splash,

    };
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
    Activity Maincontext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);

        SignUp = (LinearLayout)findViewById(R.id.ll_signup);
        SignUp.setOnClickListener(this);
        already_text = (TextView)findViewById(R.id.textView2);
        already_text.setOnClickListener(this);
    }
    private void initData(){

        viewList = new ArrayList<View>();
        Random random = new Random();
        for(int i=0;i<3;i++){
            View view = new View(this);

            view.setBackgroundResource(image[0]);

            viewList.add(view);
        }

        already_text = (TextView)findViewById(R.id.textView2);
        already_text.setOnClickListener(this);
        String name = getColoredSpanned("Already have an Account?", "#A9A9A9");
        String surName = getColoredSpanned("LOG IN","#34bba8");
        already_text.setText(Html.fromHtml(name+" "+surName));
     }

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {

            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));

        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "title";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));

            return viewList.get(position);
        }

    };

    /*@Override
    public void onClick(View v) {

       Intent i = new Intent (this, SignUpActivity.class);
                startActivity(i);
        Log.e("@@", "SignUpClick");
    }*/

    //implement the onClick method here
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.ll_signup:
                if (!NetworkUtils.isNetworkConnectionAvailable(Maincontext)) {
                    NetworkUtils.showNoInternetDialog(Maincontext);
                } else {
                    //SignUp Activity
                  //  Intent i = new Intent (this, SignUp_Activity.class);
                    Intent i = new Intent(this, Activity_Tuck12.class);
                    startActivity(i);
                }

                Log.e("@@", "SignUpClick");
                break;
            case R.id.textView2:
                if (!NetworkUtils.isNetworkConnectionAvailable(Maincontext)) {
                    NetworkUtils.showNoInternetDialog(Maincontext);
                } else {
                    //SignIn Activity
                    Intent signin = new Intent (this, SignIn_Activity.class);
                    startActivity(signin);
                    Log.e("@@", "SignInClick");
                }

                break;
        }
    }
 }

