package com.ibm.watson.newsanalyst.rss;

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

public class RSSReader {
	
	public static final String AP_RSS_URL_WORLD_OLD = "http://hosted.ap.org/lineups/WORLDHEADS.rss?SITE=AP&SECTION=HOME";
	public static final String AP_RSS_URL_WORLD = "http://hosted2.ap.org/atom/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5";
	public static final String AP_RSS_URL_TOP = "http://hosted2.ap.org/atom/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305";
	public static final String AP_RSS_URL_US = "http://hosted2.ap.org/atom/APDEFAULT/386c25518f464186bf7a2ac026580ce7";
	public static final String REUTERS_RSS_URL_US = "http://feeds.reuters.com/Reuters/domesticNews";
	public static final String REUTERS_RSS_URL_WORLD = "http://feeds.reuters.com/reuters/topNews";
	
	
	private static final FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
	private FeedFetcher feedFetcher;
	private SyndFeed feed;
	
	public RSSReader(String feedUrl) {
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
