package com.moczul.blog11;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.Html;
import android.util.Log;

public class MyXMLHandler extends DefaultHandler {
	
	private static final String TAG = "blog11";
	
	boolean currentElement = false;
	String currentValue = null;
	private RSSFeed feed;
	private RSSItem item = new RSSItem();
	private boolean isEncode = false;
	private String encode = "";
	
	public ArrayList<RSSItem> getFeed() {
		return feed.getItems();
	}
	
	public ArrayList<String> getTitles() {
		return feed.getTitles();
	}
	
	public RSSItem getItem(int index) {
		return feed.getItem(index);
	}
	
	// called when tag starts
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		currentElement = true;
		
		
		if(localName.equals("channel")) {
			feed = new RSSFeed();
		}
		
		// if we encounter title element we statars new instance of RSSItem
		if(localName.equals("title")) {
			feed.setItem(item);
			item = new RSSItem();
		} 
		
		// tip: if you want to parse content:encoded element, always use encoded, 
		// the parser will not work with content:encoded and I don't really now why
		if (localName.equals("encoded")) {
			Log.d(TAG, "ENCODED");
			isEncode = true;
			encode = "";
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		currentElement = false;
		
		
		/** set value */
		
		if(localName.equals("title")) {
			item.setTitle(currentValue);
			
		} else if (localName.equals("pubDate")) {
			item.setPubDate(currentValue);
		} else if (localName.equals("category")) {
			item.setCategory(currentValue);
		} else if (localName.equals("link")) {
			Log.d(TAG, currentValue);
			item.setLink(currentValue);
		} else if (localName.equals("description")) {
			item.setDescription(currentValue);
		} else if (localName.equals("encoded")) {
			item.setContent(encode);
			Log.d(TAG, "ENCODED_2");
			isEncode = false;
		}
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		
		currentValue = new String(ch, start, length);
		if(isEncode) {
			encode = encode + currentValue;
		}
		currentElement = false;
	}
}
