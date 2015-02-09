package com.ibm.newsmapper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.newsmapper.GeoUtils.Units;
import com.ibm.newsmapper.rss.RSSReaderAP;
import com.rometools.rome.feed.synd.SyndEntry;


@Path("/newsmapper")
public class NewsMapperResource {

	public static void main(String[] args) {
		NewsMapperResource nmr = new NewsMapperResource();
		
		nmr.getNews("34.5794343", "-118.1164613");
	}
	
	
	
	@GET
	@Produces("application/json")
	public String getNews(@QueryParam("lat") String lat, @QueryParam("lng") String lng) {

		// Get news from rss feeds
		System.out.println("World news\n===============");
		RSSReaderAP reader = new RSSReaderAP(RSSReaderAP.AP_RSS_URL_WORLD);
		List<SyndEntry> entries = reader.getFeedEntries();
		
		// list of news items
		List<NewsItem> news = new ArrayList<NewsItem>();
		
		// Create news items and add to list
		NewsItem item = new NewsItem();	
		item.setDescription("LIMA, Peru (AP) — Negotiators from more than 190 countries get together Monday in Lima for the last main stop of the U.N. climate negotiations on the road to a planned global warming deal in Paris next year.");
		item.setHeadline("Clock ticking, UN climate talks resume in Lima");
		item.setLat("-12.046374");
		item.setLng("-77.04279339999999");
		item.setLocationName("LIMA,Peru");
		item.setUrl("http://hosted2.ap.org/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5/Article_2014-12-01-LT--Peru-Climate%20Talks/id-0d20149257e3410aab240a8e00b88c21");
		news.add(item);

		item = new NewsItem();	
		item.setDescription("SAN FRANCISCO (AP) — After nearly a decade in legal wrangling, a billion-dollar class-action lawsuit over Apple's iPod music players heads to trial on Tuesday in a California federal court. A key witness will be none other than the company's legendary late founder Steve Jobs, who will be heard in a videotaped deposition.");
		item.setHeadline("Jurors to hear Steve Jobs testimony at Apple trial");
		item.setLat("37.7749295");
		item.setLng("-122.4194155");
		item.setLocationName("SAN FRANCISCO");
		item.setUrl("http://hosted2.ap.org/APDEFAULT/386c25518f464186bf7a2ac026580ce7/Article_2014-12-01-US-TEC--Apple%20iPod%20Trial/id-21a125b74e8e41f6b8d00145c1b10e14");
		news.add(item);
		

		// Calculate the closest news item
		double closestNews = Long.MAX_VALUE;
		try {
			if(lat != null && lng != null) {
				double localLat = Double.parseDouble(lat);
				double localLng = Double.parseDouble(lng);
				
				for(NewsItem i: news) {
					double newsLat = Double.parseDouble(i.getLat());
					double newsLng = Double.parseDouble(i.getLng());
					
					double dist = GeoUtils.distance(localLat, localLng, newsLat, newsLng, Units.KILOMETERS);
					if(dist < closestNews) {
						closestNews = dist;
					}
				}
			} else {
				closestNews = -1L;
			}
		} catch(Exception e) {
			closestNews = -1L;
			e.printStackTrace();
		}
		
		// Make json bean
		NewsMapperBean bean = new NewsMapperBean();
		bean.setNewsItems(news);
		bean.setNewsDistance((long)closestNews);
		//Gson gson = new Gson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		System.out.println("Returning JSON:\n" + gson.toJson(bean));		
		
		return gson.toJson(bean);
	}

	
	

}
