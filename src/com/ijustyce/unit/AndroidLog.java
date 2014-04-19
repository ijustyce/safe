package com.ijustyce.unit;

import android.util.Log;

public class AndroidLog {
	
	private static boolean showLog = true;
	private static String tag = "AndroidLog";
	
	public static void i(String tag , String msg) {
		
		if (showLog) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String msg) {
		
		if (showLog) {
			Log.i(tag, msg);
		}
	}
	
	public static void setShowLog(boolean value) {
		
		showLog = value;
	}

}
