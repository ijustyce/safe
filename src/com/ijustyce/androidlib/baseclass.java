package com.ijustyce.androidlib;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ijustyce.safe.MainActivity;
import com.ijustyce.safe.R;
import com.ijustyce.safe.txApplication;
import com.txh.Api.md5;

public class baseclass extends Activity {

	public txApplication tx;
	private String className;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		className = this.getLocalClassName().toString();
		tx = (txApplication) getApplication();
		tx.theme(baseclass.this);

		String lock = tx.getPreferences("lock", "pass");
		if (!lock.equals("null")&&!tx.pw) {

			Intent intent = new Intent(this, Password.class);
			intent.putExtra("lock",lock);
			startActivityForResult(intent, 0);
			tx.pw = true;
		}
	}

	public void anim() {

		String anim = tx.getAnim();
		if (anim.equals("fade")) {
			overridePendingTransition(R.anim.fade, R.anim.hold);
		} else if (anim.equals("zoom")) {
			overridePendingTransition(R.anim.my_scale_action,
					R.anim.my_alpha_action);
		} else if (anim.equals("roll")) {
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		} else if (anim.equals("iphone")) {
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} else if (anim.equals("Staggered")) {
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
		} else if (anim.equals("unfold")) {
			overridePendingTransition(R.anim.unfold_enter, R.anim.unfold_exit);
		}
		// default is ubuntu style
		else {
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
	}

	/**
	 * get signInfo by packageName
	 * 
	 * @param pkName
	 * @return
	 */
	public String[] getSignInfo() {

		PackageManager pm = this.getPackageManager();
		List<PackageInfo> apps = pm
				.getInstalledPackages(PackageManager.GET_SIGNATURES);
		Iterator<PackageInfo> iter = apps.iterator();
		String pkName = this.getPackageName().toString();
		while (iter.hasNext()) {
			PackageInfo packageinfo = iter.next();
			String packageName = packageinfo.packageName;
			if (packageName.equals(pkName)) {
				Signature[] sign = packageinfo.signatures;
				String result = sign[0].toString().substring(29);
				result = md5.afterMd5(result);
				String original = "true";
				if (!result.equals("3a380d267a4cfc99b45e49cc18af3829")) {
					Toast.makeText(
							this,
							getResources().getString(R.string.sign_fail)
									.toString(), Toast.LENGTH_LONG).show();
					original = "false";
				}
				String[] signInfo = { original, result };
				return signInfo;
			}
		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (!className.equals("MainActivity")) {
				startActivity(new Intent(this, MainActivity.class));
				anim();
			}
			else if (className.equals("MainActivity")) {
				this.finish();
				System.exit(0);
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onActivityResult(int reqCode, int resultCode, Intent data){  
		 
		 super.onActivityResult(reqCode, resultCode, data);  
		 switch(reqCode){
		 
		 case 0:
			 if(resultCode==RESULT_OK){
				 
				 String text = data.getStringExtra("result");
				 if(text.equals("cancel")){
					 
					 this.finish();
					 System.gc();
					 System.exit(0);
					 
				 }
			 }
		 }
	}
}
