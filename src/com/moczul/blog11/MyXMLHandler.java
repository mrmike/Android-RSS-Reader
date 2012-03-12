package com.moczul.blog11;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.Html;
import android.util.Log;

public class MyXMLHandler extends DefaultHandler {
	
	private static final String TAG = "RSS";
	
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
		
//		Log.d(TAG, "Pocz¹tek tagu: " + localName);
		
		if(localName.equals("channel")) {
			feed = new RSSFeed();
		}
		
		// jezeli napotyka na tag title tworzy now¹ instancje RSSItem()
		if(localName.equals("title")) {
			feed.setItem(item);
			item = new RSSItem();
		} 
		
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
		
//		Log.d(TAG, "Koniec tagu: " + localName);
		
		/** set value */
		
		if(localName.equals("title")) {
			item.setTitle(currentValue);
//			Log.d(TAG, currentValue);
			
		} else if (localName.equals("pubDate")) {
			item.setPubDate(currentValue);
		} else if (localName.equals("category")) {
			item.setCategory(currentValue);
		} else if (localName.equals("link")) {
			Log.d(TAG, currentValue);
			item.setLink(currentValue);
		} else if (localName.equals("description")) {
//			feed.setItem(item);
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
