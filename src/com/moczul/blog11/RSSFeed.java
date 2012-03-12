package com.moczul.blog11;

import java.util.ArrayList;

public class RSSFeed {
	
	private ArrayList<RSSItem> items = new ArrayList<RSSItem>();
	
	public ArrayList<RSSItem> getItems() {
		items.remove(0);
		return this.items;
	}
	
	public void setItem(RSSItem item) {
		this.items.add(item);
	}
	
	public RSSItem getItem(int index) {
		return items.get(index);
	}
	
	public int size() {
		return items.size();
	}
	
	
	public ArrayList<String> getTitles() {
		ArrayList<String> temp = new ArrayList<String>();
		
		for(int i=1;i<items.size();i++) {
			temp.add(items.get(i).getTitle());
		}
		
		try {
			temp.remove(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return temp;
	}

}
