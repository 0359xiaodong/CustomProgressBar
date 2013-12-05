package com.example.customui;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SeekBar;

public class DemoActivity extends Activity {

	CustomProgressBar mProgressBar;

	int progress;

	int TIME_TO_UPDATE = 100;

	SeekBar mSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgressBar = (CustomProgressBar) findViewById(R.id.myProgressBar);
		mProgressBar.setColor(Color.GREEN);
		mProgressBar.setText("Wait..");
		//new SomeDownloader().execute();

	}

	private class SomeDownloader extends AsyncTask<Integer, Integer, Void> {
		@Override
		public Void doInBackground(Integer... params) {
			while (true) {
				try {
					Thread.sleep(TIME_TO_UPDATE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				publishProgress(progress > 100 ? progress = 0 : progress++);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}
	}
}
