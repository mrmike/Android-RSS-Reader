package com.moczul.blog11;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class TweetHandler extends DefaultHandler {
	
	String currentValue;
	TweetList TweetList;
	TweetItem tweetItem;
	private static final String TAG = "blog11";
	private boolean current = false;
	private String text = "";
	private boolean isStatus = true;
	
	public ArrayList<TweetItem> getTweetFeed() {
		return TweetList.getAll();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		currentValue = new String(ch, start, length);
		if(current) {
			text += currentValue;
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		TweetList = new TweetList();
	}
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		ArrayList<TweetItem> temp = TweetList.getAll();
		Log.d(TAG, temp.toString());
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equals("status")) {
			tweetItem = new TweetItem();
		}
		
		if(localName.equals("text")) {
			current = true;
			text = "";
		}
		
		if(localName.equals("user")) {
			isStatus = false;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		if(localName.equals("text")) {
			tweetItem.setText(text);
			Log.d(TAG, "------------TEXT---------------");
			Log.d(TAG, currentValue);
			Log.d(TAG, "-------------------------------------");
			current = false;
		} else if(localName.equals("id")) {
			
			//pobieramy id tylko ze status nie z USER !!
			if(isStatus) {
				tweetItem.setTweet_id(Long.valueOf(currentValue));
				Log.d(TAG, "-----------ID-------------------");
				Log.d(TAG, currentValue);
				Log.d(TAG, "-------------------------------------");
			}
		} else if(localName.equals("created_at")) {
			tweetItem.setCreated_at(currentValue);
		} else if(localName.equals("status")) {
			
			TweetList.add(tweetItem);
			tweetItem = null;
		} else if (localName.equals("user")) {
			isStatus = true;
		}
		
	}



}
