package com.ibm.watson.newsanalyst;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.newsanalyst.cache.NewsCache;



/**
 * 
 * The NewsAnalyst app API.  This is used by the browser based
 * portion of our app. 
 * 
 * Reads the news from the cache, updates the distance info based on 
 * provided lat and lng, and then serves it up a JSON.
 *
 */
@Path("/news")
public class NewsResource {	
	
	
	@GET
	@Produces("application/json")
	public String getNews(@QueryParam("lat") String lat, @QueryParam("lng") String lng) {
		
		// Use Google Gson for Marshalling JSON to/from Java
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		// Read news JSON from cache and convert to Java beans for processing
		NewsBean newsBean = new NewsBean();
		try {
			// Get news JSON from Memcached
			String newsJson = NewsCache.getNews();

			if(newsJson != null) {
				// Marshal JSON to Java object
				newsBean = gson.fromJson(newsJson, NewsBean.class);
				
				// Set the distance of news items from given lat and long
				newsBean = News.setDistance(newsBean, lat, lng);
			} 	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Convert the news beans back to JSON for consumption by web app
		String json = gson.toJson(newsBean);
		System.out.println("Returning JSON:\n" + json);
		
		return json;
	}

	
}
