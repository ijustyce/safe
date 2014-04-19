/**
 *date:2013-07-04 19:45 
 *deepin12.12 + eclipse  
 */

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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ijustyce.androidlib.baseclass;
import com.ijustyce.safe.R;

public class MainActivity extends baseclass{
	
	private ActivityManager mActivityManager = null;
	private ConnectivityManager mCM;
	private String clickClass,clickPkg;
	private TableLayout tab;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_main);
		
		init();
	
	    tab = new TableLayout(this);
	    tab.setStretchAllColumns(true);
	    loadApp();
	    getFreeMemory();
	}
	
	private void loadApp(){
		int j = 0;
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> list = this.getPackageManager().queryIntentActivities(intent,
			  PackageManager.GET_ACTIVITIES);
		
		String[] pkg = new String[2];
		String[] appName = new String[2];
		String[] className = new String[2];
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;
        int w = 12*width/30;
		
		ScrollView s = (ScrollView)findViewById(R.id.loadApp);
		LayoutParams lp = s.getLayoutParams();
		lp.width = 2*w;
		s.setLayoutParams(lp);
		
		for (int i = 0; i < list.size(); i++){
			if(list.get(i).activityInfo.name.contains(".")){
				className[j] = list.get(i).activityInfo.name;
			}
			else{
				className[j] = list.get(i).activityInfo.packageName+"."+list.get(i).activityInfo.name;
			}
			pkg[j] = list.get(i).activityInfo.packageName;
			appName[j] = list.get(i).activityInfo.loadLabel(getPackageManager()).toString();
			j++;
			if(j==2){
				showApp(pkg,className,appName);
				j = 0;
			}
		}	
		s.addView(tab);
	}
	
	private void showApp(String[] pkg,String[] className,String[] apkName){
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
		int height = metric.heightPixels-24;	        
	    int h = 3*height/16;
	    int width = metric.widthPixels;
        int w = 2*width/5;
        
		TableRow row = new TableRow(this); 
		for(int i = 0; i<2;i++){
			Button b = new Button(this);
			b.setWidth(w);
			b.setHeight(h);
			b.setText(apkName[i]);
			final String temp = pkg[i];
			final String tempClass = className[i];
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0){
						clickPkg = temp;
						clickClass = tempClass;
						startApp();
					}

			});
			b.setBackgroundColor(getResources().getColor(R.color.appBj));
			TextView space = new TextView(this);
			space.setWidth(8);
			row.addView(space);
			row.addView(b);
		}
		TextView t = new TextView(this);
		t.setHeight(8);
		
		tab.addView(row);
		tab.addView(t);
	}
	
	private void startApp(){
		Intent intent = new Intent();
		intent.setClassName(clickPkg, clickClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		anim();
	}
	
	private void init(){
		
		Button bt10 = (Button)findViewById(R.id.exit);
		bt10.setBackgroundResource(R.drawable.exit);	
		
		Button bt6 = (Button)findViewById(R.id.free_memory);
		bt6.setBackgroundResource(R.drawable.mem);
		bt6.setText(getFreeMemory());
		bt6.setTextColor(Color.WHITE);
		
		mCM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);   
        gprsIsOpenMethod("getMobileDataEnabled"); 
	}
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean gprsIsOpenMethod(String methodName){    
		 Class cmClass       = mCM.getClass();    
	     Class[] argClasses  = null;    
	     Object[] argObject  = null;    
	            
	     Boolean isOpen = false;    
	     try{ 
	    	 Method method = cmClass.getMethod(methodName, argClasses);    
	    	 isOpen = (Boolean) method.invoke(mCM, argObject);    
	      } catch (Exception e){    
	            e.printStackTrace();    
	      }    
	      Button bt7 = (Button)findViewById(R.id.bt7);
	      if(isOpen){
	    	  bt7.setBackgroundResource(R.drawable.on);
	      }
	      else{
	    	  bt7.setBackgroundResource(R.drawable.off);
	      }
	      return isOpen;    
	    }    
	          
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		private void setGprsEnable(String methodName, boolean isEnable){    	    	
			Class cmClass       = mCM.getClass();    	      
			Class[] argClasses  = new Class[1];    
	        argClasses[0] = boolean.class;    
	        try{
	        	Method method = cmClass.getMethod(methodName, argClasses);    
	            method.invoke(mCM, isEnable);    
	        } catch (Exception e)
	        {
	        	e.printStackTrace();    
	        }    
	    }    
	
	private void gprs(){
		boolean connect = gprsIsOpenMethod("getMobileDataEnabled");
		setGprsEnable("setMobileDataEnabled",!connect);
		while(String.valueOf(gprsIsOpenMethod("getMobileDataEnabled")).
				equals(String.valueOf(connect)));
	}
	
	private String getFreeMemory(){ 
		mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
		MemoryInfo memoryInfo = new MemoryInfo() ;    
		mActivityManager.getMemoryInfo(memoryInfo) ;  
        long memSize = memoryInfo.availMem ;   
     
        String mem = formateFileSize(memSize);
        String totalMem = getTotalMemory();
        
        double free = Double.parseDouble(mem.substring(0,mem.length()-2));
        double total = Double.parseDouble(totalMem.substring(0,totalMem.length()-2));
        String result = String.valueOf(100*(total-free)/total);
        return result.substring(0,2);  
    } 
	
	private String getTotalMemory(){
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try{
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader,8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();
		}
		catch (IOException e) {
		}
		return Formatter.formatFileSize(getBaseContext(), initial_memory);
	}
	
    private String formateFileSize(long size){  
        return Formatter.formatFileSize(MainActivity.this, size);   
    }  
    
    private void freeMem(){
    	ActivityManager activityManger=(ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list=activityManger.getRunningAppProcesses();
        if(list!=null)
        for(int i=0;i<list.size();i++){
            ActivityManager.RunningAppProcessInfo apinfo=list.get(i);
            String[] pkgList=apinfo.pkgList;
            
            if(apinfo.importance>ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                for(int j=0;j<pkgList.length;j++) {
                    activityManger.killBackgroundProcesses(pkgList[j]);
                } 
            }
        }
        Button bt6 = (Button)findViewById(R.id.free_memory);
	    bt6.setText(getFreeMemory());
    }
	
	public void btClick(View v){
		switch(v.getId()){
		case R.id.exit:
			finish();
			break;
		case R.id.bt7:
			gprs();
			break;
		case R.id.free_memory:
			freeMem();
			break;
		default:
			break;	
		}
	} 
}
