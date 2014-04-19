/**
 *date:2014-04-19 19:45 
 *elementary 0.3 + eclipse  
 */

package com.ijustyce.launcher;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ijustyce.androidlib.baseclass;
import com.ijustyce.safe.R;

public class MainActivity extends baseclass {
	
	private Handler handler = null;
	ActivityManager mActivityManager = null;
	ConnectivityManager mCM;
	String clickClass;
	String clickPkg;
	private LinearLayout layout;
	private int width , height;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_main);
		
		init();
		if (handler == null) {

			handler = new Handler();
			handler.post(runnable);
		}
	}
	
	Runnable runnable = new Runnable() {

		public void run() {
			LoadApp.getAppList(MainActivity.this);
		}
	};
	
	private void init(){
		
		unit.freeMem(MainActivity.this);
		mCM = (ConnectivityManager)getSystemService
				(Context.CONNECTIVITY_SERVICE);   
        unit.gprsIsOpenMethod("getMobileDataEnabled" , MainActivity.this); 
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        
        height = 3 * (metric.heightPixels-24) / 16;
        width = 2 * metric.widthPixels / 5;
		
        layout = (LinearLayout)findViewById(R.id.loadApp);
		LayoutParams lp = layout.getLayoutParams();
		lp.width = 2 * width ;
		layout.setLayoutParams(lp);
	}
	
	public void showApp(String[] pkg,String[] className,
			String[] apkName){
        
        TableLayout tab;
        tab = new TableLayout(this);
	    tab.setStretchAllColumns(true);    
	    TableRow row = new TableRow(this); 
		for(int i = 0; i<2;i++){
			
			addButton(apkName[i] , pkg[i] , className[i] , row);
		}
		TextView t = new TextView(this);
		t.setHeight(8);
		
		tab.addView(row);
		tab.addView(t);
		layout.addView(tab);
	}
	
	private void addButton(String apkName , String pkg , 
			String className , TableRow row){

		Button b = new Button(this);
		b.setWidth(width);
		b.setHeight(height);
		b.setText(apkName);
		final String temp = pkg;
		final String tempClass = className;
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				clickPkg = temp;
				clickClass = tempClass;
				unit.startApp(MainActivity.this);
			}

		});
		b.setBackgroundColor(getResources().getColor(R.color.appBj));
		TextView space = new TextView(this);
		space.setWidth(8);
		row.addView(space);
		row.addView(b);
	}
	
	public void btClick(View v){
		switch(v.getId()){
		case R.id.launcher_exit:
			finish();
			break;
		case R.id.gprs:
			unit.gprs(MainActivity.this);
			break;
		case R.id.launcher_settings:
			unit.startSettings(MainActivity.this);
		case R.id.launcher_free_memory:
			unit.freeMem(MainActivity.this);
			break;
		default:
			break;	
		}
	}
}
