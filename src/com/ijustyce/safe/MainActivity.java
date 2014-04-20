package com.ijustyce.safe;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.ijustyce.androidlib.about;
import com.ijustyce.androidlib.baseclass;
import com.ijustyce.androidlib.settings;
import com.ijustyce.unit.AndroidLog;
import com.ijustyce.unit.toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class MainActivity extends baseclass {

	private txApplication tx;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.androidlib_main);

		tx = (txApplication) getApplication();
		init();
		getDefaultSms();
	}

	private void init() {

		String themeString = tx.theme(MainActivity.this);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		int width = metric.widthPixels;
		int height = metric.heightPixels;

		int h = 3 * height / 16;
		int w = 11 * width / 30;

		Button bt9 = (Button) findViewById(R.id.bt9);
		bt9.setBackgroundResource(R.drawable.exit);

		Button bt10 = (Button) findViewById(R.id.bt10);
		bt10.setBackgroundResource(R.drawable.setting);

		int j = R.id.bt1;

		for (int i = j; i < j + 8; i++) {

			Button bt = (Button) findViewById(i);

			 int sysVersion = VERSION.SDK_INT;
			if (themeString.equals("beauty")) {

				if (sysVersion >=11) {
					bt.setAlpha((float) 0.4);
				}
				bt.setBackgroundResource(R.drawable.newmessage);
			}
			bt.setHeight(h);
			bt.setWidth(w);
		}
	}

	public void btClick(View v) {

		switch (v.getId()) {
		case R.id.bt1:
			startActivity(new Intent(this, com.ijustyce.launcher.MainActivity.class));
			anim();
			this.finish();
			break;
		case R.id.bt2:
			// startActivity(new Intent(this, conversation.class));
			anim();
			this.finish();
			break;
		case R.id.bt3:
			// startActivity(new Intent(this,timingList.class));
			anim();
			this.finish();
			break;
		case R.id.bt4:
			// startActivity(new Intent(this,intercept.class));
			anim();
			this.finish();
			break;
		case R.id.bt5:
			// startActivity(new Intent(this,backup.class));
			anim();
			this.finish();
			break;
		case R.id.bt6:
			// startActivity(new Intent(this,advance.class));
			anim();
			this.finish();
			break;
		case R.id.bt7:
			// startActivity(new Intent(this,feedback.class));
			anim();
			this.finish();
			break;
		case R.id.bt8:
			startActivity(new Intent(this, about.class));
			anim();
			this.finish();
			break;
		case R.id.bt9:
			exit();
			break;
		case R.id.bt10:
			startActivity(new Intent(this, settings.class));
			anim();
			this.finish();
			break;
		}
	}

	private void exit() {

		System.gc();
		System.exit(0);
	}
	
	@TargetApi(19) 
	private void getDefaultSms() {

	    int sysVersion = VERSION.SDK_INT;
		if (sysVersion >= 19) {
			String pkgName = Telephony.Sms.getDefaultSmsPackage(this);
			if (!pkgName.equals(this.getPackageName())) {
				
				toast.show(R.string.MainActivity_default_sms , this);
				AndroidLog.i("---defaultSms---", pkgName);
			}
		}
	}
}
