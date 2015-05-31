package com.example.thitruong;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class Screen extends ActionBarActivity {

	private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_screen);
		mProgress = (ProgressBar) findViewById(R.id.progress_bar);
		new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	        	Screen.this.finish();
	        }
	    }, 6000);
	}

}
