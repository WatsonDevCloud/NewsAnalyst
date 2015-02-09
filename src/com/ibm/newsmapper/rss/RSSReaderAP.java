package com.ibm.newsmapper.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

public class RSSReaderAP {
	
	public static void main(String[] args) {
//		FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
//		FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
//		SyndFeed feed = null;
//		try {
//			feed = feedFetcher.retrieveFeed(new URL("http://hosted.ap.org/lineups/WORLDHEADS.rss?SITE=AP&SECTION=HOME"));
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FeedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FetcherException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		List<SyndEntry> entries = feed.getEntries();
//		
//		for(SyndEntry entry : entries) {
//			System.out.println("TITLE: " + entry.getTitle());
//			SyndContent content = entry.getDescription();
//			System.out.println("DESC: " + content.getValue());
//			System.out.println("LINK: " + entry.getLink());
//			System.out.println();
//		}
//		
//		//System.out.println(feed);
		
		
		
		System.out.println("World news\n===============");
		RSSReaderAP reader = new RSSReaderAP(AP_RSS_URL_WORLD);
		List<SyndEntry> entries = reader.getFeedEntries();
		for(SyndEntry entry : entries) {
			System.out.println("TITLE: " + entry.getTitle());
			SyndContent content = entry.getDescription();
			System.out.println("DESC: " + content.getValue());
			System.out.println("LINK: " + entry.getLink());
			System.out.println("URI: " + entry.getUri());
			System.out.println("UPDATED DATE: " + entry.getUpdatedDate());
			System.out.println();
		}
		
		System.out.println("Top news\n===============");
		reader = new RSSReaderAP(AP_RSS_URL_TOP);
		entries = reader.getFeedEntries();
		for(SyndEntry entry : entries) {
			System.out.println("TITLE: " + entry.getTitle());
			SyndContent content = entry.getDescription();
			System.out.println("DESC: " + content.getValue());
			System.out.println("LINK: " + entry.getLink());
			System.out.println("URI: " + entry.getUri());
			System.out.println("UPDATED DATE: " + entry.getUpdatedDate());
			System.out.println();
		}
		
		System.out.println("US news\n===============");
		reader = new RSSReaderAP(AP_RSS_URL_US);
		entries = reader.getFeedEntries();
		for(SyndEntry entry : entries) {
			System.out.println("TITLE: " + entry.getTitle());
			SyndContent content = entry.getDescription();
			System.out.println("DESC: " + content.getValue());
			System.out.println("LINK: " + entry.getLink());
			System.out.println("URI: " + entry.getUri());
			System.out.println("UPDATED DATE: " + entry.getUpdatedDate());
			System.out.println();
		}		
	}
	
	public static final String AP_RSS_URL_WORLD_OLD = "http://hosted.ap.org/lineups/WORLDHEADS.rss?SITE=AP&SECTION=HOME";
	public static final String AP_RSS_URL_WORLD = "http://hosted2.ap.org/atom/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5";
	public static final String AP_RSS_URL_TOP = "http://hosted2.ap.org/atom/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305";
	public static final String AP_RSS_URL_US = "http://hosted2.ap.org/atom/APDEFAULT/386c25518f464186bf7a2ac026580ce7";
	public static final String REUTERS_RSS_URL_US = "http://feeds.reuters.com/Reuters/domesticNews";
	public static final String REUTERS_RSS_URL_WORLD = "http://feeds.reuters.com/reuters/topNews";
	
	
	private static final FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
	private FeedFetcher feedFetcher;
	private SyndFeed feed;
	
	public RSSReaderAP(String feedUrl) {
		feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
		try {
			feed = feedFetcher.retrieveFeed(new URL(feedUrl));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FetcherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public List<SyndEntry> getFeedEntries() {
		return feed.getEntries();
	}
	
}
