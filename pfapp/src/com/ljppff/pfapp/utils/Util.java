package com.ljppff.pfapp.utils;

import android.text.TextUtils;
import android.util.Log;

public class Util {
	public static final boolean DBG_H = true;
	private static final String TAG = "Utils";
	public static void logh(String tag, String msg) {
		if(DBG_H) 
			if(!TextUtils.isEmpty(msg))
			Log.d(tag, msg);
	}
	
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0||str.equals("null"))
            return true;
        else
            return false;
    }


}
