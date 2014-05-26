package com.arawaney.tumascotik.client.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {
public static boolean ConnectedToInternet(Context context){
	
	boolean wifi;
	boolean mobiledata;
	
	ConnectivityManager conma = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	wifi = conma.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting(); 
	if (conma.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
		wifi = conma.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting(); 

	}else 
		wifi = false;
	
	if (conma.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
		mobiledata = conma.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

	}else 
		mobiledata = false;
	
	return (wifi | mobiledata);
	
}
}
