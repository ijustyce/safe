package com.ijustyce.launcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.widget.Button;

import com.ijustyce.safe.R;

public class unit {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setGprsEnable(String methodName, boolean isEnable , MainActivity activity) {
		Class cmClass = activity.mCM.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(activity.mCM, isEnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean gprsIsOpenMethod(String methodName , MainActivity activity) {
		Class cmClass = activity.mCM.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			isOpen = (Boolean) method.invoke(activity.mCM, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Button gprs = (Button) activity.findViewById(R.id.gprs);
		if (isOpen) {
			gprs.setBackgroundResource(R.drawable.on);
		} else {
			gprs.setBackgroundResource(R.drawable.off);
		}
		return isOpen;
	}
	
	public static void gprs(MainActivity activity){
		
		boolean connect = unit.gprsIsOpenMethod
				("getMobileDataEnabled", activity);
		unit.setGprsEnable("setMobileDataEnabled" , 
				!connect , activity);
		while(String.valueOf(unit.gprsIsOpenMethod
				("getMobileDataEnabled" , activity)).
				equals(String.valueOf(connect)));
	}
	
	private static String getFreeMemory(MainActivity activity){ 
		
		activity.mActivityManager = (ActivityManager)
				activity.getSystemService(Context.ACTIVITY_SERVICE);  
		MemoryInfo memoryInfo = new MemoryInfo() ;    
		activity.mActivityManager.getMemoryInfo(memoryInfo) ;  
        long memSize = memoryInfo.availMem ;   
     
        String mem = formateFileSize(memSize , activity);
        String totalMem = getTotalMemory(activity);
        
        double free = Double.parseDouble(mem.substring(0,mem.length()-2));
        double total = Double.parseDouble
        		(totalMem.substring(0,totalMem.length()-2));
        String result = String.valueOf(100*(total-free)/total);
        return result.substring(0,2);  
    } 
	
	private static String getTotalMemory(MainActivity activity){
		
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try{
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new 
					BufferedReader(localFileReader,8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			initial_memory = Integer.valueOf
					(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();
		}
		catch (IOException e) {
		}
		return Formatter.formatFileSize
				(activity.getBaseContext(), initial_memory);
	}
	
    private static String formateFileSize(long size , MainActivity activity){  
        
    	return Formatter.formatFileSize(activity, size);   
    }  
    
    public static void freeMem(MainActivity activity){
    	ActivityManager activityManger=(ActivityManager) 
    			activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> 
        list=activityManger.getRunningAppProcesses();
        if(list!=null)
        for(int i=0;i<list.size();i++){
            ActivityManager.RunningAppProcessInfo apinfo=list.get(i);
            String[] pkgList=apinfo.pkgList;
            
            if(apinfo.importance>ActivityManager.
            		RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                for(int j=0;j<pkgList.length;j++) {
                    activityManger.killBackgroundProcesses(pkgList[j]);
                } 
            }
        }
        Button free_memory = (Button)activity.findViewById
        		(R.id.launcher_free_memory);
        free_memory.setText(getFreeMemory(activity));
    }
    
    public static void startApp(MainActivity activity){
		Intent intent = new Intent();
		intent.setClassName(activity.clickPkg, activity.clickClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
		activity.anim();
	}
    
    public static void showLeftMenu(){
    	
    	MainActivity.menu.toggle();
    }
	
	public static  void startSettings(MainActivity activity){	
		Intent intent = new Intent();
		intent.setClassName("com.android.settings", 
				"com.android.settings.Settings");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
		activity.anim();	
	}
}
