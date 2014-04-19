package com.ijustyce.launcher;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class LoadApp {
	
	private static String[] pkg = new String[2];
	private static String[] apkName = new String[2];
	private static String[] className = new String[2];
	
	public static void getAppList(MainActivity activity){
		
		int times = 0;
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> list = activity.getBaseContext().
				getPackageManager().queryIntentActivities(intent,
			  PackageManager.GET_ACTIVITIES);
		for (ResolveInfo resolveInfo : list) {
			
			if (times == 2) {
				times = 0;
			}
			analyseInfo(resolveInfo, activity , times);
			times++;
		}
	}
	
	private static void analyseInfo(ResolveInfo resolveInfo , MainActivity activity, int times){
		
		if(resolveInfo.activityInfo.name.contains(".")){
			className[times] = resolveInfo.activityInfo.name;
			pkg[times] = resolveInfo.activityInfo.packageName;
			apkName[times] = resolveInfo.activityInfo.loadLabel
					(activity.getBaseContext().getPackageManager()).toString();
		}else {
			className[times] = resolveInfo.activityInfo.packageName + "." 
		                         + resolveInfo.activityInfo.name;
			pkg[times] = resolveInfo.activityInfo.packageName;
			apkName[times] = resolveInfo.activityInfo.loadLabel
					(activity.getBaseContext().getPackageManager()).toString();
		}if (times == 1) {
			activity.showApp(pkg, className, apkName);
		}
	}
}
