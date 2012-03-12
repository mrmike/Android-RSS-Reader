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

	// This Activity is a container for three tabs, class extends TabActivity
	
	private static final String TAG = "blog11";
	private android.widget.TabHost tabHost;

	private TabSpec mainTab;
	private TabBlog tabBlog = null;
	private TabTwitter tabTwitter = null;
	private Intent mainIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);

		// getting the tabHost object for our tabs
		tabHost = getTabHost();

		//for each tab we specified name, icon, and content
		//intent is fireing when user clicks on tab, and Activity from the intent is loaded
		//rembember about preparing icons in different resolutions
		
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

		// when we have specified all tabs(name, icon, content)
		// we're adding all TabSpec to TabHost
		tabHost.addTab(mainTab);
		tabHost.addTab(miniTab);
		tabHost.addTab(twitterTab);
	}

	// creating standard android menu, activated after pressing menu button 
	// on Android device
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mInflater = getMenuInflater();
		mInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "menu item clicked");

		// depends on user choice we're going to download rss or twitter feed
		switch (item.getItemId()) {

		case R.id.syncData:
			// getting the blog feed, we're using AsyncTask to download feed
			DataSync sync = new DataSync(getApplicationContext(), this);
			break;

		case R.id.syncTwitter:
			// getting twitter feed, same as above using of AsyncTask
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

	// we're running this method from the AsyncTask - onPostExecute
	// method is refresing the post/twitt list, and setting 
	// tab to the latest updated
	public void afterUpdate(int i) {
		switch(i) {
		case 0:
			if (tabBlog != null) {
				// we have the new data, so we should update list of posts
				tabBlog.refreshAdapter();
			}
			
		case 2:
			if (tabTwitter != null) {
				// refreshing tweets
				tabTwitter.refreshAdapter();
			}
		}
		// setting current tab to the latest updated one
		tabHost.setCurrentTab(i);
		Log.d(TAG, "afterUpdate method");
	}
}
