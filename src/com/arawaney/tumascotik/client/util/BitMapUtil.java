package com.arawaney.tumascotik.client.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class BitMapUtil {
	
	public static int getImageId(Context context, String imageName) {
	    return context.getResources().getIdentifier("drawable/ic_" + imageName.toLowerCase(), null, context.getPackageName());
	}
}
