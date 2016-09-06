package tripin.com.tripin_shipper.volley;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
* 	We are creating a Application Singleton Object by extending Application, so it should be declared as a application in the "AndroidMainFests" file
* */

public class AppController extends Application
{
	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;

	@Override
	public void onCreate()
	{
		super.onCreate();
		printHashKey();
		MultiDex.install(this);
		VolleyLog.DEBUG = false;
		mInstance = this;
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
	public void hideSoftKeyboard(Activity context) {
		if(context.getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
		}
	}

}
