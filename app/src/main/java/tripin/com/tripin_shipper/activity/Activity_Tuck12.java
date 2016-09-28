package tripin.com.tripin_shipper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.adapter.TruckCartadapter;
import tripin.com.tripin_shipper.adapter.TruckCartadapter1;
import tripin.com.tripin_shipper.fragment.TodayFragment;
import tripin.com.tripin_shipper.fragment.TomorrowFragment;
import tripin.com.tripin_shipper.model.TruckCartList;
import tripin.com.tripin_shipper.model.TruckCartObj;

/**
 * Created by Android on 09/09/16.
 */
public class Activity_Tuck12 extends FragmentActivity {
    private FragmentTabHost mTabHost;
    public ViewGroup addressesAlert;
    public int deletePosition;
    private TruckCartadapter truckCartAdapter;
    private TruckCartadapter1 truckCartAdapter2;
    private final String DELETE_MESSAGE = "Are you sure want to delete";
    private LayoutInflater inflater;
    private ViewGroup currTodayContainer, currTomorrowContainer;
    private ViewGroup deleteContainerVG, deleteItemVg;
    private View header;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck12);
        header = (View) findViewById(R.id.truckHeader);
        back = (ImageButton) header.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Tuck12.this, Activity_Dashboard.class);
                startActivity(i);
            }
        });
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Today"+ "\n"+" 23/02/2016", null),
             //   TodayFragment.class, null);
                TodayFragment.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Tomorrow", null),
                TomorrowFragment.class, null);
      /*  mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Tab 3", null),
                FragmentTab.class, null);*/

        initiateFragmentVars();
        setCommonAlertDialog();
    }
    public void deleteFromTodayList(int position){
        this.deletePosition = position;
        setAlertDialog(0,DELETE_MESSAGE, "DELETE", "CANCEL");
    }

    public void deleteFromTomorrowList(int position){
        this.deletePosition = position;
        setAlertDialog(1,DELETE_MESSAGE, "DELETE", "CANCEL");
    }



    public void setAlertDialog(final int type, String headingText, String positiveText, String negativeText) {
        addressesAlert.setVisibility(View.VISIBLE);
        TextView headingTv = (TextView) addressesAlert.findViewById(R.id.heading);
        TextView positiveTv = (TextView) addressesAlert.findViewById(R.id.positiveBtn);
        TextView negativeTv = (TextView) addressesAlert.findViewById(R.id.negativeBtn);


        headingTv.setText(headingText);
        positiveTv.setText(positiveText);
        negativeTv.setText(negativeText);

        positiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressesAlert.setVisibility(View.GONE);

                if (type == 0) {
                    //TruckCartList.todayList.remove(deletePosition);
                   // truckCartAdapter = (TruckCartadapter) TodayFragment.lToday.getAdapter();
                   // truckCartAdapter.notifyDataSetChanged();
                    truckCartAdapter.deleteEntry(deletePosition);
                }else if (type == 1) {
                    //TruckCartList.tomorrowList.remove(deletePosition);
                    //truckCartAdapter2 = (TruckCartadapter1) TodayFragment.ltomorrow.getAdapter();
                    //truckCartAdapter2.notifyDataSetChanged();
                    truckCartAdapter2.deleteEntry(deletePosition);
                }else if (type == 2) {
                    if(TruckCartList.todayList.size() > 0){
                        TruckCartList.todayList.remove(deletePosition);
                        deleteContainerVG.removeView(deleteItemVg);
                        showLists(0);
                    }

                }else if (type == 3) {
                    if(TruckCartList.tomorrowList.size() > 0){
                        TruckCartList.tomorrowList.remove(deletePosition);
                        deleteContainerVG.removeView(deleteItemVg);
                        showLists(1);
                    }

                }
            }
        });

        negativeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressesAlert.setVisibility(View.GONE);

            }
        });
    }

    private void setCommonAlertDialog(){
        addressesAlert = (ViewGroup) findViewById(R.id.common_alert);
        addressesAlert.setVisibility(View.GONE);
    }

    private void initiateFragmentVars(){
        TruckCartList.todayList = new ArrayList<>();
        TruckCartList.tomorrowList = new ArrayList<>();
    }

    private void closeSlidePanelOfToday(){
        if (TodayFragment.mLayout != null &&
                (TodayFragment.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || TodayFragment.mLayout.getPanelState() ==
                        SlidingUpPanelLayout.PanelState.ANCHORED)) {

            TodayFragment.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } else {

            //super.onBackPressed();
        }
    }

    private void closeSlidePanelOfTomorrow(){
        if (TomorrowFragment.mLayout != null &&
                (TomorrowFragment.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || TomorrowFragment.mLayout.getPanelState() ==
                        SlidingUpPanelLayout.PanelState.ANCHORED)) {

            TomorrowFragment.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } else {

            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (TodayFragment.mLayout != null &&
                (TodayFragment.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || TodayFragment.mLayout.getPanelState() ==
                        SlidingUpPanelLayout.PanelState.ANCHORED)) {

            TodayFragment.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        }else  if (TomorrowFragment.mLayout != null &&
                (TomorrowFragment.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || TomorrowFragment.mLayout.getPanelState() ==
                        SlidingUpPanelLayout.PanelState.ANCHORED)) {

            TomorrowFragment.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        }else if(TodayFragment.rel.getVisibility() == View.VISIBLE){
            TodayFragment.rel.setVisibility(View.GONE);
        }else if(TomorrowFragment.rel != null ){
            if(TomorrowFragment.rel.getVisibility() == View.VISIBLE){
                TomorrowFragment.rel.setVisibility(View.GONE);
            }else{
                finish();
            }

        }else{
            finish();
        }



    }


    public void showLists(int type){
/*      truckCartAdapter = new TruckCartadapter(this, TruckCartList.todayList, TodayFragment.fragment);
        TodayFragment.lToday.setAdapter(truckCartAdapter);


        truckCartAdapter2 = new TruckCartadapter1(this, TruckCartList.tomorrowList, TomorrowFragment.fragment);
        TodayFragment.ltomorrow.setAdapter(truckCartAdapter2);

        if(TomorrowFragment.ltomorrow != null){
            final TruckCartadapter truckCartAdapter_1 = new TruckCartadapter(this, TruckCartList.todayList, TodayFragment.fragment);
            TomorrowFragment.lToday.setAdapter(truckCartAdapter_1);

            final TruckCartadapter1 truckCartAdapter2_1 = new TruckCartadapter1(this, TruckCartList.tomorrowList, TomorrowFragment.fragment);
            TomorrowFragment.ltomorrow.setAdapter(truckCartAdapter2_1);
        }*/

        TodayFragment.lToday.setVisibility(View.GONE);
        TodayFragment.ltomorrow.setVisibility(View.GONE);

        if(TomorrowFragment.lToday != null){
            TomorrowFragment.lToday.setVisibility(View.GONE);
            TomorrowFragment.ltomorrow.setVisibility(View.GONE);
        }


         currTodayContainer= null;
        currTomorrowContainer = null;


        if(type == 0){
            currTodayContainer = TodayFragment.todayContainer;
            currTomorrowContainer = TodayFragment.tomorrowContainer;

        }else if(type == 1){
            if(TomorrowFragment.lToday != null){
                currTodayContainer = TomorrowFragment.todayContainer;
                currTomorrowContainer = TomorrowFragment.tomorrowContainer;
                currTomorrowContainer.removeAllViews();
            }

        }

        currTodayContainer.removeAllViews();

        for(int i=0;i<TruckCartList.todayList.size();i++){
            final int finalPos= i;
            TruckCartObj truckCartObj = TruckCartList.todayList.get(i);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewGroup convertView = (ViewGroup) inflater.inflate(R.layout.single_item_truck_cart, null);

            TextView weightLine1Tv = (TextView) convertView.findViewById(R.id.weightLine1Tv);
            TextView weightLine2Tv = (TextView) convertView.findViewById(R.id.weightLine2Tv);
            TextView rateTv = (TextView) convertView.findViewById(R.id.rateTv);
            TextView priceTv = (TextView) convertView.findViewById(R.id.priceTv) ;

            // getting movie data for the row
            ImageView deleteBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);
            if(deleteBtn != null){
                deleteBtn.setVisibility(View.VISIBLE);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TruckCartList.todayList.remove(finalPos);
                        //currTodayContainer.removeView(convertView);
                        deletePosition = finalPos;
                        deleteContainerVG = currTodayContainer;
                        deleteItemVg = convertView;
                        setAlertDialog(2,DELETE_MESSAGE, "DELETE", "CANCEL");
                    }
                });
            }
            currTodayContainer.addView(convertView);


 /*       weightLine1Tv.setText((truckCartObj.getPayload()));
        weightLine2Tv.setText((truckCartObj.getPayLoadTotal()));
        rateTv.setText((truckCartObj.getPayLoadPricePerTon()));
        // double totalPrice = Double.parseDouble(truckCartObj.getPayLoadTotal()) *  Double.parseDouble(truckCartObj.getPayLoadPricePerTon());
        priceTv.setText(String.valueOf(395000));*/


            weightLine1Tv.setText("20.5 tonnes");
            weightLine2Tv.setText(("21 ft Half solid & fullcaged"));
            rateTv.setText((String.valueOf(19000)));
            // double totalPrice = Double.parseDouble(item.getPayLoadTotal()) *  Double.parseDouble(item.getPayLoadPricePerTon());
            priceTv.setText(String.valueOf("3,95,000"));
        }

        if(TomorrowFragment.lToday != null){
            for(int i=0;i<TruckCartList.tomorrowList.size();i++){
                final int finalPos= i;
                TruckCartObj truckCartObj = TruckCartList.tomorrowList.get(i);
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final ViewGroup convertView = (ViewGroup) inflater.inflate(R.layout.single_item_truck_cart, null);

                TextView weightLine1Tv = (TextView) convertView.findViewById(R.id.weightLine1Tv);
                TextView weightLine2Tv = (TextView) convertView.findViewById(R.id.weightLine2Tv);
                TextView rateTv = (TextView) convertView.findViewById(R.id.rateTv);
                TextView priceTv = (TextView) convertView.findViewById(R.id.priceTv) ;

                // getting movie data for the row
                ImageView deleteBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);
                if(deleteBtn != null){
                    deleteBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TruckCartList.tomorrowList.remove(finalPos);
                            //currTomorrowContainer.removeView(convertView);

                            deletePosition = finalPos;
                            deleteContainerVG = currTomorrowContainer;
                            deleteItemVg = convertView;
                            setAlertDialog(3,DELETE_MESSAGE, "DELETE", "CANCEL");
                        }
                    });
                }
                currTomorrowContainer.addView(convertView);


 /*       weightLine1Tv.setText((truckCartObj.getPayload()));
        weightLine2Tv.setText((truckCartObj.getPayLoadTotal()));
        rateTv.setText((truckCartObj.getPayLoadPricePerTon()));
        // double totalPrice = Double.parseDouble(truckCartObj.getPayLoadTotal()) *  Double.parseDouble(truckCartObj.getPayLoadPricePerTon());
        priceTv.setText(String.valueOf(395000));*/


                weightLine1Tv.setText("20.5 tonnes");
                weightLine2Tv.setText(("21 ft Half solid & fullcaged"));
                rateTv.setText((String.valueOf(19000)));
                // double totalPrice = Double.parseDouble(item.getPayLoadTotal()) *  Double.parseDouble(item.getPayLoadPricePerTon());
                priceTv.setText(String.valueOf("3,95,000"));
            }
        }

    }

    public void goToNextPage(){
        if(TruckCartList.getTodayList().size() > 0 || TruckCartList.getTomorrowList().size() > 0) {
            AppController.getInstance().addTodayData();
            AppController.getInstance().addTomorrowData();
            Intent i = new Intent(this, goodsBilling.class);
            startActivity(i);
        }else{
            Toast.makeText(this, "Please add Data", Toast.LENGTH_LONG).show();
        }

    }
}