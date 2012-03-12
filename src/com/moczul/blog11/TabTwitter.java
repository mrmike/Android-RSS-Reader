package com.moczul.blog11;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TabTwitter extends Activity implements OnItemClickListener {
	
	private static final String TAG = "blog11";
	
	private ListView tweetList;
	private TweetHandler mHandler;
	private ArrayList<String> tweets;
	private DBHelper mDBHelper;
	
	private String urlFromTweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_feed_layout);

		tweetList = (ListView) findViewById(R.id.tweetList);
		tweets = new ArrayList<String>();
		
		mDBHelper = new DBHelper(getApplicationContext());
		Cursor c = mDBHelper.getTweetFeed(mDBHelper.getReadableDatabase());
		
		if(c.moveToFirst()) {
			do {
				tweets.add(c.getString(0));
			} while (c.moveToNext());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tweets);
		adapter.notifyDataSetChanged();
		tweetList.setAdapter(adapter);
		tweetList.setOnItemClickListener(this);
		
		((MyTabHost) this.getParent()).setTabTwitter(this);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View item, int position, long id) {
		String tweet = ((TextView) item).getText().toString();
		Log.d(TAG, "tweet:" + tweet);
		long st = System.currentTimeMillis();
		int start = tweet.indexOf("http://");
		urlFromTweet = null;
		if (start > 0) {
			String temp = tweet.substring(start);
			int spaceChar = temp.indexOf(" ");
			if (spaceChar == -1) {
				urlFromTweet = temp;
			} else {
				urlFromTweet = temp.substring(0, spaceChar);
			}
		}
		
		long t = System.currentTimeMillis() - st;
		Log.d(TAG, "Time of fetching url from tweet " + String.valueOf(t));
		
		if (urlFromTweet != null) {
			Log.d(TAG, "HTTP address from the tweet is: " + urlFromTweet);
//			 open the url here, but first we have to show dialog alert (yes/no)
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("Otworzyć url");
			alertDialog.setMessage("Czy otworzyć podany adres w przeglądarce ? ");
			alertDialog.setPositiveButton("Tak", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// open the url here
					Log.d(TAG, "Opening the url");
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(urlFromTweet));
					startActivity(i);
				}
			});
			alertDialog.setNegativeButton("Nie", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// simply do nothing
				}
			});
			alertDialog.show();
		}
		
	}
	
	public void refreshAdapter() {
		mDBHelper = new DBHelper(getApplicationContext());
		Cursor c = mDBHelper.getTweetFeed(mDBHelper.getReadableDatabase());
		
		if(c.moveToFirst()) {
			do {
				tweets.add(c.getString(0));
			} while (c.moveToNext());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tweets);
		adapter.notifyDataSetChanged();
		tweetList.setAdapter(adapter);
		tweetList.setOnItemClickListener(this);
	}


		
}
