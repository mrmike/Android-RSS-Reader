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
import android.util.Log;
import android.widget.Toast;

public class DataSync {

	private static final String TAG = "blog11";
	private Context context;
	private MyXMLHandler myHandler;
	private boolean isCorrect;
	private ArrayList<RSSItem> mFeed;
	private MyTabHost mActivty; 

	DataSync(Context ctx, Activity a) {
		this.context = ctx;
		this.mActivty = (MyTabHost) a;
		Task mTask = new Task();
		mTask.execute(null);
	}

	private class Task extends android.os.AsyncTask<URL, Integer, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(context, "Getting new posts.", Toast.LENGTH_SHORT).show();

		}

		@Override
		protected Long doInBackground(URL... params) {
			try {
				Log.d(TAG, "inBackground DataSync");
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				// address of feed source
				URL sourceUrl = new URL(
						"http://feeds.feedburner.com/blog11/icRr");

				myHandler = new MyXMLHandler();
				xr.setContentHandler(myHandler);
				xr.parse(new InputSource(sourceUrl.openStream()));
				isCorrect = true;
				mFeed = myHandler.getFeed();

			} catch (Exception e) {
				isCorrect = false;
				return null;
			}

			DBHelper dbhelper = new DBHelper(context);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// adding downloaded feed to application database
			dbhelper.addFeedToDB(mFeed, db);

			dbhelper.close();

			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			Toast.makeText(context, "Posts have been updated.",
					Toast.LENGTH_SHORT).show();
			NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
			Notification n = new Notification();
			n.vibrate = new long[] {200, 200, 200, 200};
			// we are using vibration signal to notify user about new downloaded posts
			nm.notify(15, n);
			
			// this method will refresh the list of posts and set current tab to the 1st
			mActivty.afterUpdate(0);
			
			
		}

	}

}
