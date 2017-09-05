package com.nahi.vn.mobilefinder.handler;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.activity.LockScreenActivity;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.Action;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class LockDevice.
 */
public class LockDevice {

	/** The context. */
	private Context context;
	
	/** The Constant NEW_PASS_CODE. */
	public final static String NEW_PASS_CODE = "new_pass_code";
	
	/** The Constant SELECT_DEFAULT_LAUNCH_APP_REQUEST. */
	public final static int SELECT_DEFAULT_LAUNCH_APP_REQUEST = 9991;
	
	/** The Constant SHOW_DETAIL_HOME_APP_SETTING_REQUEST. */
	public final static int SHOW_DETAIL_HOME_APP_SETTING_REQUEST = 9992;
	
	/**
	 * Instantiates a new lock device.
	 *
	 * @param context the context
	 */
	public LockDevice(Context context){
		this.context = context;
	}
	
	/**
	 * Lock device.
	 *
	 * @param password the password
	 */
	public void lockDevice(String password){
		if(!TextUtils.isEmpty(password)){
			if(TextUtils.isDigitsOnly(password)){
				//Enable Home
				enableHome(context);
				//Start Lock screen
				Bundle bundle = new Bundle();
				bundle.putString(NEW_PASS_CODE, password);
				AppUtil.startActionExtraActivity(context, LockScreenActivity.class, Action.LOCK_DEVICE.toString(), bundle);
			}
		}
	}
	
	/**
	 * Enable home.
	 *
	 * @param context the context
	 */
	public static void enableHome(Context context){
		try{
	        PackageManager localPackageManager = context.getPackageManager();
	        localPackageManager.setComponentEnabledSetting(new ComponentName(context.getPackageName(), 
	        		LockScreenActivity.class.getName()), 
	        		PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	        
	        Intent localIntent = new Intent(Intent.ACTION_MAIN, null);
	        localIntent.addCategory(Intent.CATEGORY_HOME);
	        final ResolveInfo localResolveInfo = localPackageManager.resolveActivity(localIntent, 0);
	        if (localResolveInfo == null)
	            return;
		} catch(Exception e){
			Log.e("Error :" + e.toString());
		}
	}
	
	/**
	 * Disable home.
	 *
	 * @param context the context
	 */
	public static void disableHome(Context context){
		context.getPackageManager().setComponentEnabledSetting(
				new ComponentName(context.getPackageName(), LockScreenActivity.class.getName()), 
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}
	
	/**
	 * Checks if is my launcher default.
	 *
	 * @param context the context
	 * @return true, if is my launcher default
	 */
	public static boolean isMyLauncherDefault(Context context) {
	    final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
	    filter.addCategory(Intent.CATEGORY_HOME);

	    List<IntentFilter> filters = new ArrayList<IntentFilter>();
	    filters.add(filter);

	    final String myPackageName = context.getPackageName();
	    List<ComponentName> activities = new ArrayList<ComponentName>();
	    final PackageManager packageManager = (PackageManager) context.getPackageManager();

	    // You can use name of your package here as third argument
	    packageManager.getPreferredActivities(filters, activities, null);

	    for (ComponentName activity : activities) {
	        if (myPackageName.equals(activity.getPackageName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Gets the other home launch app name.
	 *
	 * @param context the context
	 * @return the other home launch app name
	 */
	public static String getOtherHomeLaunchAppName(Context context) {
	    final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
	    filter.addCategory(Intent.CATEGORY_HOME);

	    List<IntentFilter> filters = new ArrayList<IntentFilter>();
	    filters.add(filter);

	    final String myPackageName = context.getPackageName();
	    List<ComponentName> activities = new ArrayList<ComponentName>();
	    final PackageManager packageManager = (PackageManager) context.getPackageManager();

	    // You can use name of your package here as third argument
	    packageManager.getPreferredActivities(filters, activities, null);

	    for (ComponentName activity : activities) {
	        if (!myPackageName.equals(activity.getPackageName()) && activity.getPackageName().contains("launcher")) {
	            return activity.getPackageName();
	        }
	    }
	    return null;
	}
	
	/**
	 * Select default launch app app.
	 *
	 * @param activity the activity
	 * @param requestCode the request code
	 */
	public static void selectDefaultLaunchApp(Activity activity, int requestCode){
		if(activity == null)
			return;
		//Enable Home
		LockDevice.enableHome(activity);
		
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		activity.startActivityForResult(intent, requestCode);
	}
}
