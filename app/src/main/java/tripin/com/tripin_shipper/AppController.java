package tripin.com.tripin_shipper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tripin.com.tripin_shipper.Connectivity.ConnectivityReceiver;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.AddressObj;
import tripin.com.tripin_shipper.model.TruckCartList;
import tripin.com.tripin_shipper.volley.LruBitmapCache;

/*
* 	We are creating a Application Singleton Object by extending Application, so it should be declared as a application in the "AndroidMainFests" file
* */

public class AppController extends Application
{
	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;
	public JSONObject orderSummaryObj;



	@Override
	public void onCreate()
	{
		super.onCreate();
		printHashKey();
		MultiDex.install(this);
		VolleyLog.DEBUG = false;
		mInstance = this;
	}

	public void createBlankOrderSummaryObject(){
		orderSummaryObj = new JSONObject();

		try {
			orderSummaryObj.put("Consigner", new JSONObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addConsignerAddress(AddressObj addressObj){
		try {
			addressObj = AddressList.pickUpList.get(0);
			JSONObject consignerObj = new JSONObject();
			consignerObj.put("name", addressObj.getName());
			consignerObj.put("address", addressObj.getAddress());
			orderSummaryObj.put("Consigner", consignerObj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addConsigneeAddress(AddressObj addressObj){

		try {
			addressObj = AddressList.dropList.get(0);
			JSONObject consignerObj = new JSONObject();
			consignerObj.put("name", addressObj.getName());
			consignerObj.put("address", addressObj.getAddress());
			orderSummaryObj.put("Consignee", consignerObj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addTodayData(){
		if(TruckCartList.getTodayList().size() > 0){
			try {
				if(orderSummaryObj != null){
					orderSummaryObj.put("SelectedTodayData", TruckCartList.getTodayList());
				}else{
					Log.e("orderSummaryObj.", "is null");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	public void addTomorrowData(){
		if(TruckCartList.getTomorrowList().size() > 0){
			if(orderSummaryObj != null){
				try {
					orderSummaryObj.put("SelectedTomorrowData", TruckCartList.getTomorrowList());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void addBillingAddress(AddressObj addressObj){
		try {
			JSONObject consignerObj = new JSONObject();
			consignerObj.put("name", addressObj.getName());
			consignerObj.put("address", addressObj.getAddress());
			orderSummaryObj.put("BillingAddress", consignerObj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addGoods(String goodsName, String qty){
		try {
			JSONObject goodsObj = new JSONObject();
			goodsObj.put("name", goodsName);
			goodsObj.put("qty", qty);
			orderSummaryObj.put("GoodsInf", goodsObj);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	 public void addComment(String comment)
	 {
		 try {
			 orderSummaryObj.put("commentInf", comment);
		 } catch (JSONException e) {
			 e.printStackTrace();
		 }
	 }



	public static synchronized AppController getInstance()
	{
		return mInstance;
	}

	public RequestQueue getRequestQueue()
	{
		if (mRequestQueue == null)
		{
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public ImageLoader getImageLoader()
	{
		getRequestQueue();
		if (mImageLoader == null)
		{
			mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req)
	{
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag)
	{
		if (mRequestQueue != null)
		{
			mRequestQueue.cancelAll(tag);
		}
	}
	public void printHashKey(){
		// Add code to print out the key hash
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"tripin.com.tripin_shipper",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	public  boolean isConnected(Context context) {
		ConnectivityManager
				cm = (ConnectivityManager) AppController.getInstance().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		if (isConnected){

		}
else {
			Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
		}
		return isConnected;

	}


	public void hideSoftKeyboard(Activity context) {
		if(context.getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
		}
	}
	public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
		ConnectivityReceiver.connectivityReceiverListener = listener;
	}

}
