package com.ibm.watson.newsanalyst.cache;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.ibm.watson.newsanalyst.News;
import com.ibm.watson.newsanalyst.NewsBean;
import com.ibm.watson.newsanalyst.NewsResource;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

public class NewsCache {

		private static final String NEWS_CACHE_KEY = "NEWS_CACHE_KEY";
		

		/**
		 * Update the cache with the latest news
		 * 
		 */
		public static void updateCache() {
			String timeStamp = new Date().toString();
			try {				
				// Get the latest news
				News news = new News();
				String newsJson = news.readRssAndMakeJson();
				
				// Set updated time stamp and write to cache
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				NewsBean bean = null;
				if(newsJson != null) {
					bean = gson.fromJson(newsJson, NewsBean.class);
					bean.setLastUpdated(new Date().toString());
					if(bean.getStatus().equalsIgnoreCase("OK")) {
						NewsCache.setNews(gson.toJson(bean));
						System.out.println("Updated News Cache: " + timeStamp);
					} else {
						System.out.println("Status is NOT OK. Not updating News Cache: " + timeStamp);
					}
				} else {
					System.out.println("News JSON is null. Not updating News Cache: " + timeStamp);
				}
			} catch (JsonSyntaxException e) {
				System.out.println("Error updating News Cache: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error updating News Cache: " + e.getMessage());
				e.printStackTrace();
			} 
		}
		
		
		
		/**
		 * Close the memcached connection
		 * 
		 * @param mc
		 */
		private static void closeMemcacheClient(MemcachedClient mc) {
			try {
				mc.shutdown(5L, TimeUnit.SECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Create a memcached connection
		 * 
		 * @return
		 * @throws IOException
		 */
		public static MemcachedClient getMemcachedClient() throws IOException {
			MemcachedVCAPProperties props = new MemcachedVCAPProperties();
			
			// Create an AuthDescriptor, this one is for plain SASL, so the 
			//   username and passwords are just Strings
			AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(props.getUsername(), props.getPassword()));

			// Then connect using the ConnectionFactoryBuilder.  Binary is required.
			MemcachedClient mc = null;

			    if (mc == null) {
			        mc = new MemcachedClient(
			                new ConnectionFactoryBuilder()
				                .setProtocol(Protocol.BINARY)
				                .setAuthDescriptor(ad)
				                .build(),
			                AddrUtil.getAddresses(props.getHosts()));
			    }


			return mc;
		}
		
		
		
		/**
		 * Read the news from the cache
		 * 
		 * @return
		 * @throws IOException
		 */
		public static String getNews() throws IOException {
			MemcachedClient mc = getMemcachedClient();
			
			Object myObject = mc.get(NEWS_CACHE_KEY);
			
			closeMemcacheClient(mc);
			
			if(myObject instanceof String) {
				return (String)myObject;
			} else {
				return null;
			}
		}

		
		/**
		 * Write the news to the cache
		 * 
		 * @param newsJson
		 * @throws IOException
		 */
		public static void setNews(String newsJson) throws IOException {
			MemcachedClient mc = getMemcachedClient();
			mc.set(NEWS_CACHE_KEY, 0, newsJson);
			closeMemcacheClient(mc);
		}
		
		

}
