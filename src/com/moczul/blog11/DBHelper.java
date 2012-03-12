package com.moczul.blog11;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "blog11_reader_database.sqlite3";
	private static final String DATABASE_TABLE = "posts";
	private static final String TWEET_TABLE = "tweets";
	private static final int DB_VERSION = 1;
	private static final String DATABASE_CREATE = "CREATE TABLE posts (id INTEGER PRIMARY KEY AUTOINCREMENT, pubDate TEXT, title TEXT, category TEXT, content TEXT, link TEXT, fetched_at INTEGER);";
	private static final String DATABASE_CREATE2 = "CREATE TABLE tweets (tweet_id INTEGER PRIMARY KEY, created_at TEXT,  text TEXT);";
	
	// posts Table keys
	private static final String KEY_PUBDATE = "pubDate";
	private static final String KEY_TITLE = "title";
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_LINK = "link";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_FETCHED_AT = "fetched_at";
	// tweet table keys
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_TWEET_ID = "tweet_id";
	private static final String KEY_TEXT = "text";
	
	private static final String TAG = "blog11";
	
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE2);
			db.execSQL("CREATE UNIQUE INDEX idx ON "+ DATABASE_TABLE + "("+ KEY_PUBDATE +");");
			db.execSQL("CREATE UNIQUE INDEX tweet_idx ON " + TWEET_TABLE + "(" + KEY_TWEET_ID + ");");
			Log.d(TAG, "New databases has been created.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// upgrading database is simply deleting the old one and creating the new
	// in real app we should think about migration
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS posts");
		onCreate(db);	
	}
	
	// method is adding feed to database, and returns how many items were add
	public int addFeedToDB(ArrayList<RSSItem> mFeed, SQLiteDatabase db) {
		int counter = 0;
		ContentValues initialValues;
		RSSItem tempItem;
		for(int i = mFeed.size()-1; i>=0; i--) {
			tempItem = mFeed.get(i);
			initialValues = new ContentValues();
			initialValues.put(KEY_PUBDATE, tempItem.getPubDate());
			initialValues.put(KEY_TITLE, tempItem.getTitle());
			initialValues.put(KEY_CATEGORY, tempItem.getCategory());
			initialValues.put(KEY_LINK, tempItem.getLink());
			initialValues.put(KEY_CONTENT, tempItem.getContent());
			initialValues.put(KEY_FETCHED_AT, System.currentTimeMillis());
			
			if(!tempItem.getTitle().equals("blog11")) {
				if ((db.insert(DATABASE_TABLE, null, initialValues)) != -1) {
					counter++;
				}
			}
		}
		// return the number of added posts
		return counter;
	}
	
	//method is adding tweets to database, just like method above(in case of posts)
	public int addTweetToDB(ArrayList<TweetItem> tFeed, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		TweetItem tempTweet = null;
		int counter = 0;
		for(int i = 0; i<tFeed.size();i++) {
			tempTweet = tFeed.get(i);
			cv.put(KEY_TWEET_ID, tempTweet.getTweet_id());
			cv.put(KEY_CREATED_AT, tempTweet.getCreated_at().toString());
			cv.put(KEY_TEXT, tempTweet.getText().toString());
			
			Log.d(TAG, "Tweet added to database: " + tempTweet.getText());
			
			db.insert(TWEET_TABLE, null, cv);

			
			tempTweet = null;
			
		}
		
		return counter;
	}
	
	//getting all entries from the main category, exclude miniblog's posts
	public Cursor getDbFeed(SQLiteDatabase db) {
		return db.query(DATABASE_TABLE, new String[] {KEY_TITLE}, "NOT " + KEY_CATEGORY + " like 'miniblog'", null, null, null, KEY_FETCHED_AT + " DESC");
	}
	
	// get all entries from the miniblog category (this is data for the second tab in our application
	public Cursor getMiniFeed(SQLiteDatabase db) {
		return db.query(DATABASE_TABLE, new String[] {KEY_TITLE}, KEY_CATEGORY +" like 'miniblog'", null, null, null, KEY_FETCHED_AT + " DESC");
	}
	
	//return only one post from database, post is specified by title,
	// looking for a post by title maybe isn't the best solutions(especially when you have post with same titles) but it works in this case
	public Cursor getPost(String title, SQLiteDatabase db) {
		return db.query(DATABASE_TABLE, new String[] {KEY_CONTENT}, KEY_TITLE + " like '" + title + "'", null, null, null, null);
	}
	
	//get all tweets saved in database
	// quite simple, we want only the content of each tweet, we don't care here about eg. date, only text
	public Cursor getTweetFeed(SQLiteDatabase db) {
		return db.query(TWEET_TABLE, new String[] {KEY_TEXT}, null, null, null, null, KEY_TWEET_ID + " DESC");
	}

}
