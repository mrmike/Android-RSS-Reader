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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DataSync {

	private Context context;
	private MyXMLHandler myHandler;
	private boolean isCorrect;
	private static final String TAG = "blog11";
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
			Toast.makeText(context, "Pobieranie wpisów...", Toast.LENGTH_SHORT).show();

		}

		@Override
		protected Long doInBackground(URL... params) {
			try {
				Log.d(TAG, "inBackground DataSync");
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

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
			dbhelper.addFeedToDB(mFeed, db);

			dbhelper.close();

			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			Toast.makeText(context, "Wpisy zostały zaktualizowane",
					Toast.LENGTH_SHORT).show();
			NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
			Notification n = new Notification();
			n.vibrate = new long[] {200, 200, 200, 200};
			nm.notify(15, n);
			
			mActivty.afterUpdate(0);
			
			
		}

	}

}
