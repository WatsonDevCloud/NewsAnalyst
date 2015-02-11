package com.ibm.watson.newsanalyst;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.newsanalyst.GeoUtils.Units;
import com.ibm.watson.newsanalyst.rss.NewsItem;
import com.ibm.watson.newsanalyst.rss.RSSReader;
import com.ibm.watson.relexbind.Entity;
import com.ibm.watson.relexbind.Marshaller;
import com.ibm.watson.relexbind.RelExUtil;
import com.ibm.watson.relexbind.Rep;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;


/**
 * 
 * Provides the majority of the integration for this app. 
 *
 */
public class News {	
	
	/**
	 * Constructor
	 */
	public News() {
		
		// Get connection info and credentials for the Watson Relationship Extraction service
		this.relexConnectionProps = new VCAPProperties("relationship_extraction"); 
		
	}
	
		
	
	/**
	 * Gets the Relationship Extraction data from the RSS news entry.
	 * 
	 * @param entry 
	 * @param cleanDesc Cleans a Rueters news article.  
	 * @return A utility to pull out relevant data 
	 */
	private RelExUtil extract(SyndEntry entry, boolean cleanDesc) {
		System.out.println("TITLE: " + entry.getTitle());
		SyndContent content = entry.getDescription();
		
		// Get the description text from the news item
		String desc = content.getValue();
		
		// Clean up Rueters news description.  They have an HTML
		// images embedded in them.
		int imgIndex = desc.indexOf("<img");
		if(cleanDesc && imgIndex > 1 ) {
			desc = desc.substring(0, imgIndex);
			//System.out.println("Clean desc: " + desc);
			content.setValue(desc);
		}
			
		
		// Process the news description.
		String xml = RelExAPI.performExtraction(entry.getDescription().getValue(), 
						"ie-en-news", 
						relexConnectionProps.getBaseURL(), 
						relexConnectionProps.getUsername(), 
						relexConnectionProps.getPassword());
		
		System.out.println("XML Results:\n" + xml);
		
		// Convert the XML to java objects
		Rep rep = Marshaller.marshallXml(xml);
		
		System.out.println(rep);
		
		return (new RelExUtil(rep));
	}
	
	
	
	/**
	 * 
	 * Gets the news items from RSS, runs the items through Watson Relationship
	 * Extraction service and Google Geolocation service, then converts the 
	 * results to JSON.  
	 * 
	 * @return JSON used by the app API
	 */
	public String readRssAndMakeJson() {
		// Get news from rss feeds
		RSSReader readerWorld = new RSSReader(RSSReader.AP_RSS_URL_WORLD);
		List<SyndEntry> entriesAll = readerWorld.getFeedEntries();
		
		RSSReader readerTop = new RSSReader(RSSReader.AP_RSS_URL_TOP);
		List<SyndEntry> entriesTop = readerTop.getFeedEntries();
		entriesAll.addAll(entriesTop);
		
		RSSReader readerUs = new RSSReader(RSSReader.AP_RSS_URL_US);
		List<SyndEntry> entriesUs = readerUs.getFeedEntries();
		entriesAll.addAll(entriesUs);
	
		RSSReader readerReutersUs = new RSSReader(RSSReader.REUTERS_RSS_URL_US);
		List<SyndEntry> entriesReutersUs = readerReutersUs.getFeedEntries();
		entriesAll.addAll(entriesReutersUs);
			
		// list of news items
		List<NewsItem> news = new ArrayList<NewsItem>();
		
		// Add feed to list
		String message = "";
		String status = "OK";
		for(SyndEntry entry : entriesAll) {
			// Extract metadata
			RelExUtil rxUtil = extract(entry, true);			

			// Find location
			String locName = "";
			try {
				locName = makeLocationName(rxUtil.getLocations());
			} catch (Exception rxe) {
				message = rxe.getMessage();
				status = "REL_EX_FAILED";
				break; 
			}
			
			// only add found locations
			if("NO_LOCATION_FOUND".equalsIgnoreCase(locName) == false) {		
				String itemLat = "NOT_FOUND";
				String itemLng = "NOT_FOUND";
				final Geocoder geocoder = new Geocoder();
				GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(locName).setLanguage("en").getGeocoderRequest();
				try {
					GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
					if(geocoderResponse.getResults() != null && geocoderResponse.getResults().size() > 0) {
						itemLat = "" + geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat();
						itemLng = "" + geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng();	
					}
				} catch (IOException e) {
					message = e.getMessage();
					status = "GEOLOCATION_FAILED";
					break; 
				}
				
				// Create news item
				NewsItem item = new NewsItem();	
				item.setDescription(entry.getDescription().getValue());
				item.setHeadline(entry.getTitle().toString());
				item.setUrl(entry.getLink().toString());
				item.setLat(itemLat);
				item.setLng(itemLng);
				item.setLocationName(locName);				
				item.setPersonNames(rxUtil.getPersonNames());
				item.setOrgNames(rxUtil.getOrgNames(true));
				item.setRels(rxUtil.getRelationsList());
				item.setEnts(rxUtil.getEntitiesList());
				news.add(item);
			}
		}
		
		// Make results bean
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		NewsBean bean = new NewsBean();
		bean.setNewsItems(news);
		bean.setStatus(status);
		bean.setMessage(message);
		
		// Convert results to JSON
		String json = gson.toJson(bean);
		
		return json;
	}
	
	
	
	
	/**
	 * Utility to update a news item with the distance from a given latitude
	 * and longitude.
	 * 
	 * @param nmb
	 * @param lat
	 * @param lng
	 * @return
	 */
	protected static NewsBean setDistance(NewsBean nmb, String lat, String lng) {
		List<NewsItem> newsItems = nmb.getNewsItems();
		double closestNews = Long.MAX_VALUE;
		NewsItem closestItem = null;
		if(newsItems != null && lat != null && lng != null) {
			try {
				double localLat = Double.parseDouble(lat);
				double localLng = Double.parseDouble(lng);
				
				for(NewsItem i: newsItems) {
					try {
						double newsLat = Double.parseDouble(i.getLat());
						double newsLng = Double.parseDouble(i.getLng());
						double dist = GeoUtils.distance(localLat, localLng, newsLat, newsLng, Units.KILOMETERS);
						i.setDistance((long)dist);
						if(dist < closestNews) {
							closestNews = dist;
							closestItem = i;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch(Exception e) {
				closestNews = -1L;
				e.printStackTrace();
			}
		} else {
			closestNews = -1L;
		}
		
		if(closestItem != null) {
			closestItem.setClosest(true);
		}
		nmb.setNewsDistance((long)closestNews);
		
		return nmb;
	}
	
	
	
	/**
	 * Utility to grab the location of the news item from a list of locations mentioned in the article.
	 *   
	 * @param locationEntities
	 * @return
	 */
	private static String makeLocationName(List<Entity> locationEntities) {
		String locName = "NO_LOCATION_FOUND";
		if(locationEntities != null && locationEntities.size()  > 0) {
			locName = RelExUtil.getDisplayName(locationEntities.get(0));
		}		
		System.out.println(locName);
		return locName;
	}
	
	
	
	// Connection info and credentials for the Watson Relationship Extraction service
	private VCAPProperties relexConnectionProps;
	
}
