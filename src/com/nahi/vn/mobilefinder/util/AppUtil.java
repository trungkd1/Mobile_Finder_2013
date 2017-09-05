package com.nahi.vn.mobilefinder.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.broadcast.DeviceAdmin;
import com.nahi.vn.mobilefinder.broadcast.NetworkUtil;
import com.nahi.vn.mobilefinder.dialog.NotifyDialog;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.service.SendMailService;


// TODO: Auto-generated Javadoc
/**
 * The Class AppUtil.
 */
public class AppUtil {
	
	/**
	 * Convert state.
	 *
	 * @param states the states
	 * @return the string
	 */
	public static String convertState(boolean[] states){
		StringBuilder builder = new StringBuilder();
		for(boolean state: states){
			if(state){
				builder.append("1");
			}
			else{
				builder.append("0");
			}
		}
		return builder.toString();
	}

	/**
	 * Convert state.
	 *
	 * @param s the s
	 * @return the boolean[]
	 */
	public static boolean[] convertState(String s){
		boolean[] states = new boolean[s.length()];
		int i = 0;
		for(char c: s.toCharArray()){
			if(c == '1'){
				states[i++] = true;
			} 
			else{
				states[i++] = false;
			}
		}
		return states;
	}

	/**
	 * Checks if is match phone.
	 *
	 * @param context the context
	 * @param number1 the number1
	 * @param number2 the number2
	 * @return true, if is match phone
	 */
	public static boolean isMatchPhone(Context context, String number1, String number2){
		if(TextUtils.isEmpty(number1) || TextUtils.isEmpty(number2)){
			return false;
		}
		return PhoneNumberUtils.compare(context, number1, number2);
	}
	
	/**
	 * Stop service.
	 *
	 * @param context the context
	 * @param c the c
	 */
	public static void stopService(Context context, Class<?> c){
		Intent intent = new Intent(context, c);
		context.stopService(intent);
	}
	
	/**
	 * Start service.
	 *
	 * @param context the context
	 * @param c the c
	 */
	public static void startService(Context context, Class<?> c){
		Intent intent = new Intent(context, c);
		context.startService(intent);
	}

	/**
	 * Start action activity.
	 *
	 * @param context the context
	 * @param c the c
	 * @param action the action
	 */
	public static void startActionActivity(Context context, Class<?> c, String action){
		startActionExtraActivity(context, c, action, null);
	}
	
	/**
	 * Start action extra activity.
	 *
	 * @param context the context
	 * @param c the c
	 * @param action the action
	 * @param bundle the bundle
	 */
	public static void startActionExtraActivity(Context context, Class<?> c, String action, Bundle bundle) {
		Intent intent = new Intent(context, c);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.setAction(action);
		if(bundle != null){
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}
	
	/**
	 * Start action.
	 *
	 * @param context the context
	 * @param action the action
	 */
	public static void startAction(Context context,String action){
		context.startActivity(new Intent(action));
	}

	/**
	 * Vibrate.
	 *
	 * @param context the context
	 */
	public static void vibrate(Context context){
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(50);
	}
	
	/**
	 * Turn wifi on.
	 *
	 * @param context the context
	 * @param on the on
	 */
	public static void turnWifi(Context context, boolean on) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(on);
	}
	
	/**
	 * Turn gps on.
	 *
	 * @param context the context
	 * @param on the on
	 */
	public static void turnGps(Context context, boolean on) {
		String provider = Settings.Secure.getString(
				context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (on) {
			if (!provider.contains("gps")) { // if gps is disabled
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings",
						"com.android.settings.widget.SettingsAppWidgetProvider");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				context.sendBroadcast(poke);
			}
		} else {
			if (provider.contains("gps")) { // if gps is enabled
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings",
						"com.android.settings.widget.SettingsAppWidgetProvider");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				context.sendBroadcast(poke);
			}
		}
	}
	
	/**
	 * Turn3g connection.
	 *
	 * @param context the context
	 * @param enabled the enabled
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void turn3gConnection(Context context, boolean enabled) {
		try {
			final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    Class conmanClass = Class.forName(conman.getClass().getName());
		    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		    iConnectivityManagerField.setAccessible(true);
		    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		    setMobileDataEnabledMethod.setAccessible(true);
		    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send json Array.
	 *
	 * @param jsonArray the json array
	 * @return the string
	 */
	public static String sendJSONObject(JSONArray jsonArray){
        InputStream inputStream = null;
        String result = "";
        try {
 
            HttpClient httpclient = new DefaultHttpClient();
 
            HttpPost httpPost = new HttpPost(Config.URL_POST_JSON);
 
            StringEntity se = new StringEntity(jsonArray.toString());
 
            httpPost.setEntity(se);
 
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
 
            HttpResponse httpResponse = httpclient.execute(httpPost);
 
            inputStream = httpResponse.getEntity().getContent();
 
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d(e.getLocalizedMessage());
        }
 
        return result;
    }
	
	/**
	 * Convert input stream to string.
	 *
	 * @param is the is
	 * @return the string
	 */
	public static String convertInputStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	
	/**
	 * Gets the time.
	 *
	 * @param date the date
	 * @return the time
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
		return sdf.format(date);
	}
	
	/**
	 * Start active admin.
	 *
	 * @param contex the contex
	 * @return the intent
	 */
	public static Intent startActiveAdmin(Context contex){
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
	        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
	        		deviceAdminComponent(contex));
	        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
	                "Additional text explaining why this needs to be added.");
		
	   return intent;
	}
	
	/**
	 * Removes the active admin.
	 *
	 * @param context the context
	 */
	public static void removeActiveAdmin(Context context){
		DevicePolicyManager mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDPM.removeActiveAdmin(deviceAdminComponent(context));
	}
	
	/**
	 * Checks if is admin active.
	 *
	 * @param context the context
	 * @return true, if is admin active
	 */
	public static boolean isAdminActive(Context context){
		DevicePolicyManager mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		return mDPM.isAdminActive(deviceAdminComponent(context));
	}
	
	
	/**
	 * Device admin component.
	 *
	 * @param context the context
	 * @return the component name
	 */
	public static ComponentName deviceAdminComponent(Context context){
		return new ComponentName(context, DeviceAdmin.class);
	}
	
	
	/**
	 * Wipe data.
	 *
	 * @param context the context
	 */
	public static void wipeData(Context context){
		DevicePolicyManager mDPM =
			    (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
	}
	
    /**
     * Check internet connection.
     *
     * @param context the context
     */
    public static void checkInternetConnection(Context context){
    	boolean isConnected = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
        	NetworkInfo[] info = connectivity.getAllNetworkInfo();
        	if (info != null){
        		for (int i = 0; i < info.length; i++){
        			if (info[i].getState() == NetworkInfo.State.CONNECTED){
        				isConnected = true;
        				break;
        			}
        		}
        	}
        }
        SaveData.getInstance(context).setInternetConnecting(isConnected);
    }
    
    /**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
    public static String getDeviceID (){
    	return android.os.Build.SERIAL;
    }
	
	  /**
     * Checks for sim card.
     *
     * @param context the context
     * @return true, if successful
     */
    public static boolean hasSimCard(Context context){
    	TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
     	if (manager.getSimState() != TelephonyManager.SIM_STATE_ABSENT){
     		//the phone has a sim card
     		return true;
      	} else {
      		//no sim card available
      		return false;
      	}
    }
    
    /**
     * Gets the app internal directory.
     *
     * @param packageName the package name
     * @return the app internal directory
     */
    public static String getAppInternalDirectory(String packageName){
    	File dir = new File(Environment.getDataDirectory(), Config.DATA_DIR + File.separator + packageName);
    	return dir.getAbsolutePath();
    }
    
	/**
	 * Creates the file.
	 *
	 * @param packageName the package name
	 * @param filename the filename
	 * @param content the content
	 * @return the string
	 */
	public static String createFile(String packageName, String filename, String content){
		File playNumbersDir = new File(getAppInternalDirectory(packageName), filename);
		if(!playNumbersDir.exists())
			try {
				playNumbersDir.createNewFile();
			} catch (IOException e1) {
			}
		
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(playNumbersDir);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(content);
			myOutWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return playNumbersDir.getAbsolutePath();
	}


	/**
	 * Send sms message.
	 *
	 * @param receiverNumber the receiver number
	 * @param body the body
	 * @return true, if successful
	 */
	public static boolean sendSmsMessage(final String receiverNumber, final String body) {
		if (TextUtils.isEmpty(receiverNumber)) {
			return false;
		}
		try {
			SmsManager sms = SmsManager.getDefault();
			ArrayList<String> parts = sms.divideMessage(body);
			sms.sendMultipartTextMessage(receiverNumber, null, parts, null, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Send email message.
	 *
	 * @param context the context
	 * @param toEmail the to email
	 * @param body the body
	 * @param listFiles the list files
	 */
	public static void sendEmailMessage(Context context, String toEmail, String body, ArrayList<String> listFiles){
		Intent intent = new Intent(context, SendMailService.class);
		intent.putExtra(SendMailService.EXTRA_ADDRESS, toEmail);
		intent.putExtra(SendMailService.EXTRA_BODY, body);
		intent.putStringArrayListExtra(SendMailService.EXTRA_FILENAMES, listFiles);
		context.startService(intent);
	}
	
	/**
	 * Split to array list.
	 *
	 * @param input the input
	 * @param pattern the pattern
	 * @return the array list
	 */
	public static ArrayList<String> splitToArrayList(String input, String pattern){
		ArrayList<String> list = new ArrayList<String>(); 
		if(!TextUtils.isEmpty(input)){
			String[] arr = input.split(pattern);
			if(arr.length > 0){
				Collections.addAll(list, arr);	
			}
			else if(arr.length == 0){
				list.add(input);
			}
		}
		return list;
	}
	
	/**
	 * Send broadcast.
	 * 
	 * @param context
	 *            the context
	 * @param action
	 *            the action
	 */
	public static void sendBroadcast(Context context, String action) {
		sendBroadcast(context, action, null);
	}
	
	/**
	 * Send broadcast.
	 *
	 * @param context the context
	 * @param action the action
	 * @param bundle the bundle
	 */
	public static void sendBroadcast(Context context, String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if(bundle != null){
			intent.putExtras(bundle);
		}
		context.sendBroadcast(intent);
	}
	
	/**
	 * Merge arr to string.
	 * 
	 * @param list
	 *            the list
	 * @param splitPattern
	 *            the split pattern
	 * @return the string
	 */
	public static String mergeArrToString(List<String> list, String splitPattern) {
		String text = "";
		if(list != null){
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					text += list.get(i);
					if (list.size() > 1 && i < list.size() - 1) {
						text += splitPattern;
					}
				}
			}
		}
		return text;
	}
	
	/**
	 * Gets the serial number.
	 *
	 * @param context the context
	 * @return the serial number
	 */
	public static String getSerialNumber(Context context){
		TelephonyManager telemamanger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String phone = telemamanger.getSimSerialNumber();
		return phone; 
	}
	
	
	/**
	 * Gets the address.
	 *
	 * @param context the context
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @return the address
	 */
	public static String getAddress(Context context,double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e(e.getMessage());
        }

        return result.toString();
    }
	
	/**
	 * Start fragment.
	 *
	 * @param activity the activity
	 * @param containerId the container id
	 * @param c the c
	 */
	public static void startFragment(FragmentActivity activity, int containerId, Class<?> c){
		try {
			FragmentManager manager = activity.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(containerId, (Fragment) c.newInstance());
			transaction.addToBackStack(null);
			transaction.commit();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Start fragment.
	 *
	 * @param activity the activity
	 * @param containerId the container id
	 * @param c the c
	 * @param tag the tag
	 */
	public static void startFragment(FragmentActivity activity, int containerId, Class<?> c, String tag){
		try {
			FragmentManager manager = activity.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(containerId, (Fragment) c.newInstance(), tag);
			transaction.addToBackStack(null);
			transaction.commit();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restart fragment.
	 *
	 * @param activity the activity
	 * @param containerId the container id
	 * @param c the c
	 * @param tag the tag
	 */
	public static void restartFragment(FragmentActivity activity, int containerId, Class<?> c, String tag){
		try {
			FragmentManager manager = activity.getSupportFragmentManager();
			Fragment fragment = manager.findFragmentByTag(tag);
			if(fragment != null){
				//Detach old fragment
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.detach(fragment);
				transaction.commit();
				//Add new fragment
				transaction.add(containerId, (Fragment) c.newInstance(), tag);
				transaction.commit();
			}
			else{
				startFragment(activity, containerId, c, tag);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Show check internet connection dialog.
	 *
	 * @param context the context
	 */
	public static void showCheckInternetConnectionDialog(Context context){
		NotifyDialog dialog = new NotifyDialog(context, new DialogListener() {

			@Override
			public void acceptListener(Dialog dialog) {
				dialog.dismiss();
			}

			@Override
			public void cancelListener(Dialog dialog) {
				dialog.dismiss();
			}
			
		});
		dialog.setTitle(context.getString(R.string.no_connection_title));
		dialog.setContent(context.getString(R.string.no_connection_msg));
		dialog.hasTwoButton(false);
		dialog.show();
	}
	
	
	/**
	 * Gets the number.
	 *
	 * @param context the context
	 * @param uri the uri
	 * @return the number
	 */
	public static String getNumber(Context context,Uri uri){
    	 Cursor cur = context.getContentResolver().query(uri, null, null, null, null);
    	 String number = null;
    	 if(cur != null && cur.moveToFirst())
         {
    		 number = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
         }     
    	 return number;
	}
	
	
	/**
	 * Sets the check.
	 *
	 * @param button the button
	 * @param text the text
	 * @param isCheck the is check
	 */
	public static void setCheck(View button,TextView text,boolean isCheck){
		if(isCheck){
			button.setBackgroundResource(R.drawable.btn_setting);
			text.setTextColor(button.getContext().getResources().getColor(R.color.green));
		}else{
			button.setBackgroundResource(R.drawable.btn_setting_onoff_highlighted);
			text.setTextColor(button.getContext().getResources().getColor(R.color.gray));
		}
		button.setEnabled(isCheck);

	}
	
	/**
	 * Show installed app details.
	 *
	 * @param context the context
	 * @param packageName the package name
	 */
	public static void showInstalledAppDetails(Context context, String packageName) {
	    Intent intent = new Intent();
	    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
	    try {
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) { 
		        Uri uri = Uri.fromParts("package", packageName, null);
		        intent.setData(uri);
		    } 
	    } catch ( ActivityNotFoundException e ) {
	    	Log.e("ActivityNotFoundException :" + e.toString());
	    } finally{
	    	context.startActivity(intent);
	    }
	}
	
	/**
	 * Show installed app details.
	 *
	 * @param activity the activity
	 * @param packageName the package name
	 * @param requestCode the request code
	 */
	public static void showInstalledAppDetails(Activity activity, String packageName, int requestCode) {
	    Intent intent = new Intent();
	    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
	    try {
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) { 
		        Uri uri = Uri.fromParts("package", packageName, null);
		        intent.setData(uri);
		    } 
	    } catch ( ActivityNotFoundException e ) {
	    	Log.e("ActivityNotFoundException :" + e.toString());
	    } finally{
	    	activity.startActivityForResult(intent, requestCode);
	    }
	}
	
	
	/**
	 * Gets the notify dialog.
	 *
	 * @param context the context
	 * @param title the title
	 * @param content the content
	 * @param textButton the text button
	 * @param hasTwoButtons the has two buttons
	 * @param listener the listener
	 * @return the notify dialog
	 */
	public static NotifyDialog getNotifyDialog(Context context, String title, String content, String textButton, boolean hasTwoButtons, DialogListener listener){
		NotifyDialog notifyDialog = getNotifyDialog(context, title, content, hasTwoButtons, listener);
		notifyDialog.settextButton(textButton);
		return notifyDialog;
	}
	
	/**
	 * Gets the notify dialog.
	 *
	 * @param context the context
	 * @param title the title
	 * @param content the content
	 * @param hasTwoButtons the has two buttons
	 * @param listener the listener
	 * @return the notify dialog
	 */
	public static NotifyDialog getNotifyDialog(Context context, String title, String content, boolean hasTwoButtons, DialogListener listener){
		NotifyDialog notifyDialog = new NotifyDialog(context, listener);
		notifyDialog.setTitle(title);
		notifyDialog.setContent(content);
		notifyDialog.hasTwoButton(hasTwoButtons);
		return notifyDialog;
	}
	
	/**
	 * Hide soft keyboard.
	 *
	 * @param activity the activity
	 */
	public static void hideSoftKeyboard(Activity activity){
		InputMethodManager inputManager = (InputMethodManager)activity
	                .getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() == null) {
			return;
		} else {
	        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
	                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * Checks for gps device.
	 *
	 * @param context the context
	 * @return true, if successful
	 */
	public static boolean hasGPSDevice(Context context){
	    final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    if ( mgr == null ) {
	    	return false;
	    }
    	final List<String> providers = mgr.getAllProviders();
	    if ( providers == null ) {
	    	return false;
	    }
	    return providers.contains(LocationManager.GPS_PROVIDER);
    }
	
	/**
	 * Checks for gps provider.
	 *
	 * @param context the context
	 * @return true, if successful
	 */
	public static boolean hasGPSProvider(Context context){
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	    if ( manager == null ) {
	    	return false;
	    }
		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	
	/**
	 * Gets the time string2long.
	 *
	 * @param values the values
	 * @return the time string2long
	 */
	public static long getTimeString2long(String values){
		long time = 0;
		 SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        try {
				Date date = dt.parse(values);
				time = date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return time;
	}
	
	
	/**
	 * Gets the timelong2 string.
	 *
	 * @param values the values
	 * @return the timelong2 string
	 */
	public static String getTimelong2String(long values){
		  SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  Date date = new Date(values);
		  date.setTime(values);
		  return outputDateFormat.format(date);
	}

	/**
	 * Enable disable view.
	 *
	 * @param view the view
	 * @param enabled the enabled
	 */
	public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup)view;
            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
	
	/**
	 * Gets the schedule auto backup time.
	 *
	 * @return the schedule auto backup time
	 */
	public static Calendar getScheduleAutoBackupTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 13);//Schedule at 1:00 PM everyday
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	/**
	 * Sets the schedule auto backup.
	 *
	 * @param context the new schedule auto backup
	 */
	public static void setScheduleAutoBackup(Context context){
		// check task is scheduled or not
		boolean isAlreadySchedule = (PendingIntent.getBroadcast(context, 0, new Intent(context, NetworkUtil.class),
				PendingIntent.FLAG_NO_CREATE) != null);
		if (!isAlreadySchedule) {
			Intent intent = new Intent(context, NetworkUtil.class);
			intent.setAction(Config.INTENT_SCHEDULE_AUTO_BACKUP);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			//Repeat every day
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					getScheduleAutoBackupTime().getTimeInMillis(), AlarmManager.INTERVAL_DAY,
					pendingIntent);
		}
	}
}
