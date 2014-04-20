package com.ijustyce.safe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.ijustyce.sqlite.createTable;
import com.ijustyce.unit.AndroidLog;
import com.ijustyce.unit.toast;

@SuppressWarnings("deprecation")
public class txApplication extends Application {

	private static String tag = "txTag";
	private String dbFile; 
	public String sharedName = "shared";
	public boolean pw = false;	
	@Override
	public void onCreate() {
		
		ActiveAndroid.initialize(this);
		firstUse();
		Log.i(tag, "Application onCreate , pid = " + Process.myPid());
	}
	
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}
	
	/**
	 * set theme and return theme name
	 * @param context
	 */
	public String theme(Context context){
		
		String themeString = getPreferences("theme", getSharedPreferencesName());
		
		if(themeString.equals("")){
			return "";
		}
		Log.i(tag, themeString);
		
		if (themeString.equals("sky")) {
			
			context.setTheme(R.style.txTheme3);
		} else if(themeString.equals("beauty")){
			
			context.setTheme(R.style.txTheme);
		}else if(themeString.equals("pure")){
			context.setTheme(R.style.txTheme2);
		}
		
		return themeString;
	}
	
	/**
	 * read preferences file and return value if possible or return null
	 * @param value 
	 * @param fileName
	 * @return
	 */
	public String getPreferences(String key ,String fileName){
		
		SharedPreferences shared = getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		
		return shared.getString(key, "null");
	}
	
	/**
	 *  set preferences file value
	 * @param key key of preference file 
	 * @param value value of key
	 * @param fileName preference file name
	 */
	public void setPreferences(String key , String value , String fileName){
		
		SharedPreferences shared = getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		
		shared.edit().putString(key, value).commit();
	}
	
	
	/**
	 * return PreferenceActivity's sharedPreferences
	 * @return sharedPreferences
	 */
	public SharedPreferences getSharedPreferences(){
		
		SharedPreferences shared = getSharedPreferences(getSharedPreferencesName(),
				Context.MODE_PRIVATE);
		return shared;
	}
	
	/**
	 * return PreferenceActivity's sharedPreferences
	 * @return pkgName_preferences
	 */
	public String getSharedPreferencesName(){
		
		return this.getPackageName() + "_preferences";
	}
	
	/**
	 * check if is first time , return true if is first time
	 * @return
	 */
	public boolean firstUse(){
		
		File dbFile = this.getDatabasePath("safe.db");
		AndroidLog.i(dbFile.getAbsolutePath());
		if(!dbFile.exists()){
			new createTable();
			Log.i(tag, "this is first time to use");
			return true;
		}
		
		Log.i(tag, "this is not the first time to use");
		return false;
	}
	
	/**
	 * return sqlite file path ,if parent directory not exist
	 * it will create !
	 * @return
	 */
	public String getDbFile(){
	
		String file = this.getFilesDir().getPath();
		File f = new File(file);
		if(!f.exists()){
			f.mkdir();
		}
		dbFile = file + "/contacts.db";
		Log.i(tag, dbFile);
		return dbFile;
	}
	
	
	/**
	 * return windows animation string
	 * @return
	 */
	
	public String getAnim(){
		
		SharedPreferences myshared = getSharedPreferences();
		String anim = myshared.getString("anim", "ubuntu");
		Log.i(tag, "anim: "+anim);
		return anim;
	}
	
	/**
	 * return pkgName+" "+versionName , if fail return ""
	 * @return
	 */
	public String getVersion() {
	    
		try {
	        PackageManager manager = this.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	        String version = info.versionName;
	        String pkgName = info.applicationInfo.loadLabel
	        		(getPackageManager()).toString();
	        return pkgName+" "+version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    }
	}
	
	/**
	 * return date by your own formatter , like yyyy/MM/dd/HH/mm
	 * @return
	 */
	public String getDate(String formatter){
		
		SimpleDateFormat ft = new SimpleDateFormat(formatter,Locale.CHINA);
		 Date dd = new Date();
		 return ft.format(dd);
	}
	
	/**
	 * return date formated like yyyy/MM/dd/HH/mm
	 * @return
	 */
	public static String getDate(){
		
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" ,Locale.CHINA);
		 Date dd = new Date();
		 return ft.format(dd);
	}
	
	/**
	 * @return phone number if success or return null 
	 */
	
	public String getNumber(){
	
		TelephonyManager mngr = (TelephonyManager)this.
				getSystemService(Context.TELEPHONY_SERVICE); 
		String number = mngr.getLine1Number();
	    
	    Log.i("---justyce---", "phone number :"+number);
	    return number;
	}
	
	/**
	 * get string of strings.xml
	 * @param id string id like R.string.hello
	 * @return string value
	 */
	public String getStringValue(int id){
		
		return getResources().getString(id).toString();
	}
	
	/**
	 * set text to clipboard 
	 * @param text the text set to clipboard 
	 */
	public void setClipboard(String text){
		
		ClipboardManager clipboard = (ClipboardManager)getSystemService
				(Context.CLIPBOARD_SERVICE);
		
		clipboard.setText(text);
		toast.show(R.string.copy_success , this);
	}
	
	/**
	 * get clipboard text
	 * @return clipboard text
	 */
	public String getClipboard(){
		
		ClipboardManager clipboard = (ClipboardManager)getSystemService
				(Context.CLIPBOARD_SERVICE);
		
		return clipboard.getText().toString();
	}
}