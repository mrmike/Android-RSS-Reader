package com.moczul.blog11;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class TabBlog extends Activity implements OnItemClickListener {
	
	private static final String TAG = "blog11";
	private ListView listItems;
	private MyXMLHandler myHandler;
	ArrayAdapter<String> adapter;
	private boolean isCorrect;
	private DBHelper mDBHelper;
	private ArrayList<String> titles;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listItems = (ListView) findViewById(R.id.wpisy);
        /** Create a new textview array to display the results */
        
        mDBHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cFeed = mDBHelper.getDbFeed(db);
        titles = new ArrayList<String>();
        
        // wypelniamy tablice titles, tytułami postów/ dostępnych w bazie
        if (cFeed.moveToFirst()) {
        	do {
        		titles.add(cFeed.getString(0));
        	} while (cFeed.moveToNext());
        }
        
        mDBHelper.close();
        
        setListItemTitles(titles);
        MyTabHost b = (MyTabHost) this.getParent();
        b.setTabBlog(this);
    }
    
    public ArrayList<String> getTitles() {
    	return this.titles;
    }
    
    public void setListItemTitles(ArrayList<String> titleArray) {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleArray);
        listItems.setAdapter(adapter);
        listItems.setOnItemClickListener(this);
    }
    

	@Override
	public void onItemClick(AdapterView<?> arg0, View item, int position, long id) {
		try {
			Log.d(TAG, "Number of clicked item: " + String.valueOf(position));
			TextView row = (TextView) item;
			String title = row.getText().toString();
			
			Cursor c = mDBHelper.getPost(title, mDBHelper.getReadableDatabase());
			boolean isContentLoad = c.moveToFirst();
			if (!isContentLoad) {
				Log.d(TAG, "We cannot get the post from database;");
				return;
			}
			String content = c.getString(0);
			mDBHelper.close();
			
			// checking if we have the appropriate title and content
			Log.d(TAG, "Post title: " + title.toString());
			Log.d(TAG, "Post content: " + content.toString());
			
			//here we have to send intent 
			Intent mIntent = new Intent(getApplicationContext(), Post.class);
			
			Bundle b = new Bundle();
			b.putString("title", title);
			b.putString("content", content);
			Log.d(TAG, "Sending Intent");
			mIntent.putExtras(b);
			
			startActivity(mIntent);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage().toString());
			return;
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		mDBHelper.close();
	}
	
	public void refreshAdapter() {
		mDBHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cFeed = mDBHelper.getDbFeed(db);
        titles = new ArrayList<String>();
        
        // wypelniamy tablice titles, tytułami postów/ dostępnych w bazie
        if (cFeed.moveToFirst()) {
        	do {
        		titles.add(cFeed.getString(0));
        	} while (cFeed.moveToNext());
        }
        
        mDBHelper.close();
        
        setListItemTitles(titles);
        Log.d(TAG, "TabBlog: Inside refreshAdapter method");
	}
	
	

}