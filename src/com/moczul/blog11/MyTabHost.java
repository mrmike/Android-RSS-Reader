package com.moczul.blog11;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class MyTabHost extends TabActivity {

	private static final String TAG = "blog11";
	private android.widget.TabHost tabHost;

	private TabSpec mainTab;
	private Intent mainIntent;
	private TabBlog tabBlog = null;
	private TabTwitter tabTwitter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);

		tabHost = getTabHost();

		// main feed tab
		mainTab = tabHost.newTabSpec("Blog");
		mainTab.setIndicator("Blog",
				getResources().getDrawable(R.drawable.icon_photos_tab));
		mainIntent = new Intent(this, TabBlog.class);
		mainTab.setContent(mainIntent);

		// mini_feed tab
		TabSpec miniTab = tabHost.newTabSpec("Miniblog");
		miniTab.setIndicator("Miniblog",
				getResources().getDrawable(R.drawable.icon_songs_tab));
		Intent miniIntent = new Intent(this, TabMiniblog.class);
		miniTab.setContent(miniIntent);

		// twitter feed tab
		TabSpec twitterTab = tabHost.newTabSpec("Twitter");
		twitterTab.setIndicator("Twitter",
				getResources().getDrawable(R.drawable.icon_videos_tab));
		Intent twitterIntent = new Intent(this, TabTwitter.class);
		twitterTab.setContent(twitterIntent);

		// adding all TabSpec to TabHost
		tabHost.addTab(mainTab);
		tabHost.addTab(miniTab);
		tabHost.addTab(twitterTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mInflater = getMenuInflater();
		mInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "menu item clicked");

		switch (item.getItemId()) {

		case R.id.syncData:
			// konstruktor pobierze context, i wywola zadanie asynchroniczne
			DataSync sync = new DataSync(getApplicationContext(), this);
			break;

		case R.id.syncTwitter:
			// pobieramy twittera
			TwitterSync tSync = new TwitterSync(getApplicationContext(), this);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setTabBlog(Activity a) {
		this.tabBlog = (TabBlog) a;
	}

	public void setTabTwitter(Activity a) {
		this.tabTwitter = (TabTwitter) a;
	}

	public void afterUpdate(int i) {
		switch(i) {
		case 0:
			if (tabBlog != null) {
				tabBlog.refreshAdapter();
			}
			
		case 2:
			if (tabTwitter != null) {
				tabTwitter.refreshAdapter();
			}
		}
		tabHost.setCurrentTab(i);
		Log.d(TAG, "afterUpdate method");
	}
}
