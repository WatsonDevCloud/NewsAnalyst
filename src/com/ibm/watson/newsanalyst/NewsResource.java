package com.ibm.watson.newsanalyst;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.newsanalyst.cache.NewsCache;


@Path("/news")
public class NewsResource {	
	
	
	@GET
	@Produces("application/json")
	public String getNews(@QueryParam("lat") String lat, @QueryParam("lng") String lng) {
		
		// Read news JSON from cache and convert to Java beans
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		NewsBean newsBean = null;
		try {
			String newsJson = NewsCache.getNews();

			if(newsJson != null) {
				newsBean = gson.fromJson(newsJson, NewsBean.class);
			} 	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set the distance of news items from given lat and long
		newsBean = News.setDistance(newsBean, lat, lng);
		
		// Convert the news beans back to JSON
		String json = gson.toJson(newsBean);
		System.out.println("Returning JSON:\n" + json);
		
		return json;
	}

	
}
