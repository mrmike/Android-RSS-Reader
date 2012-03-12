package com.moczul.blog11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

public class Post extends Activity {
	
	// activity is displaying one post
	// the data(title, content) are retriving from the intent
	
	private TextView titleView;
	private TextView contentView;
	private DBHelper mHandler;
	private String title = "post title";
	private String content = "post content";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_layout);
		
		titleView = (TextView) findViewById(R.id.title);
		contentView = (TextView) findViewById(R.id.content);
		
		setPostContent();
	}
	
	public void setPostContent() {

		Intent mIntent = getIntent();
		
		//getting data from the intent
		Bundle b = mIntent.getExtras();
		title = b.getString("title");
		content = b.getString("content");
		
		titleView.setText(title);
		contentView.setText(content);
		
	}

}
