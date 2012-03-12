package com.moczul.blog11;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TwitterSync {
	
	private Context context;
	private TweetHandler tHandler;
	private ArrayList<TweetItem> tFeed;
	private static final String TAG = "blog11";
	private MyTabHost mActivity;
	
	TwitterSync(Context ctx, Activity act) {
		this.context = ctx;
		this.mActivity = (MyTabHost) act;
		Task t = new Task();
		t.execute(null);
	}
	
	
	private class Task extends AsyncTask<URL, Integer, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(context, "Pobieranie tweetow...", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Long doInBackground(URL... params) {
			try {
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xmlr = sp.getXMLReader();
				
				tHandler = new TweetHandler();
				xmlr.setContentHandler(tHandler);
				URL source = new URL("http://api.twitter.com/1/statuses/user_timeline.xml?screen_name=pawel07");
				
				xmlr.parse(new InputSource(source.openStream()));
				tFeed = tHandler.getTweetFeed();
				Log.d(TAG, tFeed.toString());
				
				DBHelper dbhelper = new DBHelper(context);
				SQLiteDatabase db = dbhelper.getWritableDatabase();
				
				Log.d(TAG, String.valueOf(tFeed.get(0).getTweet_id()));
				//here's the problem
				dbhelper.addTweetToDB(tFeed, db);
	//
				dbhelper.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);

			Toast.makeText(context, "Pobrano tweety", Toast.LENGTH_SHORT).show();
			NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
			Notification n = new Notification();
			n.vibrate = new long[] {200, 200, 200, 200};
			nm.notify(15, n);
			
			mActivity.afterUpdate(2);
			
		}
		
		
		
	}

}
