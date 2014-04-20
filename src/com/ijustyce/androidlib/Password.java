package com.ijustyce.androidlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ijustyce.safe.R;
import com.ijustyce.safe.txApplication;
import com.ijustyce.unit.toast;
import com.txh.Api.md5;

public class Password extends Activity {

	private txApplication tx;
	private int errorTime = 0;
	private String password;
	private TextView info;
	private boolean first = false, affirm = false;
	private boolean setPassword = false , delPassword = true;
	public static String lockPin = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tx = (txApplication) getApplication();

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {

			String lock = bundle.getString("lock");
			if (lock.equals("gesture")) {
				setContentView(R.layout.androidlib_locker);
				password = tx.getPreferences("gesture", "pass");
				if (password.equals("null")) {
					setPassword = true;
					info = (TextView) findViewById(R.id.lock_user_info);
					init();
				}
			}
		}
	}

	private void init() {

		info.setText(tx.getStringValue(R.string.pass_first));
		affirm = false;
		password = "";
		first = true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (setPassword && delPassword) {

				tx.setPreferences("lock", "null", "pass");
			}
			exit("cancel");
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit(String value) {

		Intent intent = new Intent();
		intent.putExtra("result", value);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (!lockPin.equals("")) {
				check(lockPin);
			}
		}
		return true;
	}

	private void check(String lockPin) {

		if (errorTime > 2) {
			exit("cancel");
		}

		if (!first && password.equals(md5.afterMd5(lockPin))) {

			exit("success");
			toast.show(R.string.pw_success , this);
		}

		if (!first && !password.equals(md5.afterMd5(lockPin))) {

			errorTime++;
			if (errorTime == 3) {
				toast.show(R.string.pw_wait , this);
			} else {
				toast.show(R.string.pw_error , this);
			}
		}

		if (first && affirm) {

			if (password.equals(lockPin)) {

				delPassword = false;
				tx.setPreferences("gesture", md5.afterMd5(password), "pass");
				exit("success");
			}

			else{
				init();
				toast.show(R.string.pass_affirm_error , this);
				return ;
			}
		}

		if (first && !affirm) {

			password = lockPin;
			info.setText(tx.getStringValue(R.string.pass_affirm));
			affirm = true;
		}
	}
}
