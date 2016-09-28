package tripin.com.tripin_shipper.model;

import android.app.Activity;

import java.util.ArrayList;

import tripin.com.tripin_shipper.activity.goodsBilling;

/**
 * Created by designtripin on 09/09/16.
 */
public class AddressList {
    public static goodsBilling goodsBillingActivity;

    public static final String SELECT_DIFFERENT_ROUTE = "SELECT DIFFERENT ROUTE";
    public static final String SHOW_CHEAPEST_ROUTE = "SHOW CHEAPEST ROUTE";
    public static boolean allowPickUpSwipe =false;
    public static boolean allowDropSwipe =false;
    public static final float diabledAlphaValue = 0.7f;
    public static Activity mainActivity;
    public static ArrayList<AddressObj> pickUpList = new ArrayList<>();
    public static ArrayList<AddressObj> dropList = new ArrayList<>();

    public static ArrayList<AddressObj> cheapestRoutePickUpList = new ArrayList<>();
    public static ArrayList<AddressObj> cheapestRouteDropList = new ArrayList<>();

    public static String addressesBundle ;
}
