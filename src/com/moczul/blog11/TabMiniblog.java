package com.moczul.blog11;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

public class TabMiniblog extends Activity implements OnItemClickListener {

	private static final String TAG = "blog11";
	
	private DBHelper mDBHelper;
	private ArrayList<String> titles;
	private ArrayAdapter<String> adapter;
	private ListView miniListView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mini_feed_layout);
		
		miniListView = (ListView) findViewById(R.id.miniListView);
		
		titles = new ArrayList<String>();
		
		mDBHelper = new DBHelper(getApplicationContext());
		Cursor c = mDBHelper.getMiniFeed(mDBHelper.getReadableDatabase());
		
		if(c.moveToFirst()) {
			do {
				titles.add(c.getString(0));
			} while (c.moveToNext());
		} 
		
		setAdapter();
		
	}
	
	private void setAdapter() {
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
		adapter.notifyDataSetChanged();
		miniListView.setAdapter(adapter);
		miniListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View parent, int position	, long id) {
		String title = ((TextView) parent).getText().toString();
		Cursor post = mDBHelper.getPost(title, mDBHelper.getReadableDatabase());
		boolean isContentLoad = post.moveToFirst();
		if (!isContentLoad) {
			Log.d(TAG, "We cannot get the post from database;");
			return;
		}
		Intent i = new Intent(getApplicationContext(), Post.class);
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("content", post.getString(0).toString());
		i.putExtras(b);
		
		post.close();
		
		startActivity(i);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// always close database connection !
		mDBHelper.close();
		
	}

}
