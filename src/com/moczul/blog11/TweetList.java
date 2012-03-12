package com.moczul.blog11;

import java.util.ArrayList;

public class TweetList {
	
	ArrayList<TweetItem> tweet_feed = new ArrayList<TweetItem>();
	
	public void add(TweetItem item) {
		tweet_feed.add(item);
	}
	
	public ArrayList<TweetItem> getAll() {
		return tweet_feed;
	}



}
