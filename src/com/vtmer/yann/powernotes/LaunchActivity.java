package com.vtmer.yann.powernotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LaunchActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread logoTimer = new Thread() {
			public void run() {
				try {
					int logoTimer = 0;
					while(logoTimer < 1500) {
						sleep(100);
						logoTimer = logoTimer + 100;
					};
					startActivity(new Intent("com.vtmer.yann.HOMESCREEN"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					finish();
				}
			}
		};

		logoTimer.start();
	}
}
