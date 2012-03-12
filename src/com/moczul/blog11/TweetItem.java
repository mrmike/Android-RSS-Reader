package com.moczul.blog11;

public class TweetItem {
	
	private String created_at;
	private long tweet_id;
	private String text;
	
	
	public String getCreated_at() {
		return created_at;
	}
	
	public void setCreated_at(String c) {
		this.created_at = c;
	}
	
	public long getTweet_id() {
		return tweet_id;
	}
	
	public void setTweet_id(long id) {
		this.tweet_id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

}
