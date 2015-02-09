package com.ibm.newsmapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.bluemix.watson.relex.bind.Entity;
import com.ibm.bluemix.watson.relex.test.RelExAPI;
import com.ibm.newsmapper.GeoUtils.Units;
import com.ibm.newsmapper.cache.NewsCache;
import com.ibm.newsmapper.rss.RSSReaderAP;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;


@Path("/newsmapper2")
public class NewsMapperResource2 {

	public static void main(String[] args) {
		NewsMapperResource2 nmr = new NewsMapperResource2();
	
		
		//nmr.makeNewsJson();
		System.out.println(nmr.makeNewsJson());
		
		
//		nmr.getNews("34.5794343", "-118.1164613");
//		nmr.getNews(null, null);
	}
	
	

	
	private String makeLocationName(List<Entity> locationEntities) {
		String locName = "NO_LOCATION_FOUND";
//		if(locationEntities.size() > 1) {
//			locName = RelExTest.getDisplayName(locationEntities.get(0)) + "," + RelExTest.getDisplayName(locationEntities.get(1));
//		} else if(locationEntities.size() == 1) {
//			locName = RelExTest.getDisplayName(locationEntities.get(0));
//		}
		if(locationEntities.size()  > 0) {
			locName = RelExAPI.getDisplayName(locationEntities.get(0));
		}		
		System.out.println(locName);
		return locName;
	}
	
	
	private RelExAPI extract(SyndEntry entry, boolean cleanDesc) {
		System.out.println("TITLE: " + entry.getTitle());
		SyndContent content = entry.getDescription();
		
		String desc = content.getValue();
		//System.out.println("Unclean desc: " + desc);
		
		int imgIndex = desc.indexOf("<img");
		if(cleanDesc && imgIndex > 1 ) {
			desc = desc.substring(0, imgIndex);
			//System.out.println("Clean desc: " + desc);
			content.setValue(desc);
		}
		
		
		System.out.println("DESC: " + desc);
		System.out.println("LINK: " + entry.getLink());
		System.out.println("URI: " + entry.getUri());
		System.out.println("UPDATED DATE: " + entry.getUpdatedDate());
		System.out.println();

		String xml = RelExAPI.performExtraction(entry.getDescription().getValue());
		RelExAPI relEx = new RelExAPI(xml);
		
		return relEx;
	}
	


	
	
	@GET
	@Produces("application/json")
	public String getNews(@QueryParam("lat") String lat, @QueryParam("lng") String lng) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		NewsMapperBean bean = null;
		try {
			String newsJson = NewsCache.getNews();

			if(newsJson != null) {
				bean = gson.fromJson(newsJson, NewsMapperBean.class);
			} 	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(bean == null) {
			bean = gson.fromJson(FAILOVER_STATIC_NEWS, NewsMapperBean.class);
		}
		
		bean = setDistance(bean, lat, lng);
		
		// Convert results to JSON
		String json = gson.toJson(bean);
//		System.out.println("Returning JSON:\n" + json);
		
		return json;
	}
	
	
	public String makeNewsJson() {
		// Get news from rss feeds
		RSSReaderAP readerWorld = new RSSReaderAP(RSSReaderAP.AP_RSS_URL_WORLD);
		List<SyndEntry> entriesAll = readerWorld.getFeedEntries();
		
		RSSReaderAP readerTop = new RSSReaderAP(RSSReaderAP.AP_RSS_URL_TOP);
		List<SyndEntry> entriesTop = readerTop.getFeedEntries();
		entriesAll.addAll(entriesTop);
		
		RSSReaderAP readerUs = new RSSReaderAP(RSSReaderAP.AP_RSS_URL_US);
		List<SyndEntry> entriesUs = readerUs.getFeedEntries();
		entriesAll.addAll(entriesUs);
	
		RSSReaderAP readerReutersUs = new RSSReaderAP(RSSReaderAP.REUTERS_RSS_URL_US);
		List<SyndEntry> entriesReutersUs = readerReutersUs.getFeedEntries();
		entriesAll.addAll(entriesReutersUs);
		
//		RSSReaderAP readerReutersWorld = new RSSReaderAP(RSSReaderAP.REUTERS_RSS_URL_WORLD);
//		List<SyndEntry> entriesReutersWorld = readerReutersWorld.getFeedEntries();
//		entriesAll.addAll(entriesReutersWorld);
//	
 
//		RSSReaderAP readerReutersUs = new RSSReaderAP(RSSReaderAP.REUTERS_RSS_URL_US);
//		List<SyndEntry> entriesAll = readerReutersUs.getFeedEntries();		
		
		// list of news items
		List<NewsItem> news = new ArrayList<NewsItem>();
		
		// Add feed to list
		String message = "";
		String status = "OK";
		
//		int numEntries = 0;
		for(SyndEntry entry : entriesAll) {
//			numEntries++;
//			if(numEntries > 2) {
//				break;
//			}
			
			// Extract metadata
			RelExAPI ex = extract(entry, true);			

			// Find location
			String locName = "";
			try {
				locName = makeLocationName(ex.getLocations());
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
				item.setPersonNames(ex.getPersonNames());
				item.setOrgNames(ex.getOrgNames(true));
				item.setRels(ex.getRelationsList());
				item.setEnts(ex.getEntitiesList());
				news.add(item);
			}
		}
		
		// Make results bean
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		NewsMapperBean bean = new NewsMapperBean();
		bean.setNewsItems(news);
		bean.setStatus(status);
		bean.setMessage(message);
		
		// Convert results to JSON
		String json = gson.toJson(bean);
		//System.out.println("Returning JSON:\n" + json);
		
		return json;
	}
	
	
	
	
	
	private static NewsMapperBean setDistance(NewsMapperBean nmb, String lat, String lng) {
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
	
	
	
	private static final boolean USE_FAILOVER_NEWS = true;
	private static final String FAILOVER_STATIC_NEWS = "{ \"status\": \"OK\", \"message\": \"Error. Using failover news.\", \"newsDistance\": -1, \"lastUpadted\": \"OLD\", \"newsItems\": [ { \"url\": \"http://hosted2.ap.org/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5/Article_2014-12-04-ML--Israel-Election-Analysis/id-fadca42f057445c699c9b830e2132c9e\", \"headline\": \"Israel\u0027s election a referendum on Netanyahu\", \"description\": \"JERUSALEM (AP) â€” The coming Israeli election amounts to a referendum on Benjamin Netanyahu â€” hard-headed defender of Israel to some, the man who buried dreams of peace to others.\", \"locationName\": \"JERUSALEM,Israeli\", \"lat\": \"31.768319\", \"lng\": \"35.21371\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5/Article_2014-12-04-EU--Russia-Restive%20Caucasus/id-e9768feb9d09449bbfcdc6d80ac39219\", \"headline\": \"Gun battles in Chechen capital leave 19 dead\", \"description\": \"GROZNY, Russia (AP) â€” Security forces in the capital of Russia\u0027s North Caucasus republic of Chechnya stormed two buildings, including a school, in fierce gun battles with militants early Thursday that left at least 19 dead, authorities said.\", \"locationName\": \"GROZNY,Russia\", \"lat\": \"43.3168796\", \"lng\": \"45.68148559999999\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5/Article_2014-12-04-ML--Abu%20Dhabi-US/id-b953b66556df4102bbc2c26c3c87e2e4\", \"headline\": \"Emirates police make arrest in American\u0027s stabbing\", \"description\": \"ABU DHABI, United Arab Emirates (AP) â€” Police in the United Arab Emirates have arrested a woman they say is behind the stabbing death of an American teacher and a separate plot to bomb another American\u0027s house, a top official said Thursday as authorities moved swiftly to calm fears of instability in the normally peaceful Gulf nation.\", \"locationName\": \"American\", \"lat\": \"35.5195369\", \"lng\": \"35.7734075\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5/Article_2014-12-04-ML--Syria-Inside%20Kobani/id-5cffe7fbdcee4a07986de3713e98f9b1\", \"headline\": \"INSIDE KOBANI: Kurds doggedly defend town from IS\", \"description\": \"KOBANI, Syria (AP) â€” The men and women of Kobani call one another 'heval' â€” Kurdish for \u0027comrade\u0027 â€” and fight with revolutionary conviction, vowing to liberate what they regard as Kurdish land from Islamic State group militants.\", \"locationName\": \"KOBANI,Syria\", \"lat\": \"36.6959872\", \"lng\": \"38.4008357\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5/Article_2014-12-04-EU-Britain-Obit-Thorpe/id-441594595bc9442f985cf6573be2fe34\", \"headline\": \"Ex-Liberal Party leader Jeremy Thorpe dies at 85\", \"description\": \"LONDON (AP) â€” Jeremy Thorpe, an influential British politician who helped revive the Liberal Party before his career was cut short by scandal, died on Thursday. He was 85.\", \"locationName\": \"LONDON,British\", \"lat\": \"51.49111389999999\", \"lng\": \"-0.2453842\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305/Article_2014-12-04-US-Police-Chokehold-Death/id-34569ea81ee7408697eb1e4c08299e4f\", \"headline\": \"Civil rights leaders decry decision in chokehold case\", \"description\": \"NEW YORK (AP) â€” Civil rights leaders Thursday decried the grand jury decision not to charge a white police officer in the chokehold death of a black man and announced plans for a march and a summit on racial justice in Washington later this month.\", \"locationName\": \"NEW YORK,Washington\", \"lat\": \"43.2519472\", \"lng\": \"-73.37086959999999\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305/Article_2014-12-04-US--Immigration/id-0710c0523f5b4bc7b4a327a5cf2c9964\", \"headline\": \"House rebukes Obama on immigration\", \"description\": \"WASHINGTON (AP) â€” House Republicans have passed a bill denouncing President Barack Obama\u0027s executive actions on immigration and declaring them 'null and void.'\", \"locationName\": \"WASHINGTON\", \"lat\": \"38.9071923\", \"lng\": \"-77.03687069999999\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305/Article_2014-12-04-US-Health-Overhaul-Premiums/id-ac1b375d1f4d45f29e09d4d0c98f5674\", \"headline\": \"HealthCare.gov average premiums going up in 2015\", \"description\": \"WASHINGTON (AP) â€” Many HealthCare.gov customers will face higher costs next year, the Obama administration acknowledged Thursday in a report that shows average premiums rising modestly.\", \"locationName\": \"WASHINGTON\", \"lat\": \"38.9071923\", \"lng\": \"-77.03687069999999\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305/Article_2014-12-04-US-United-States-Yemen/id-75e62d14028b49788d0d67e3999bc4da\", \"headline\": \"Pentagon confirms failed effort to rescue Somers\", \"description\": \"WASHINGTON (AP) â€” The Pentagon says a hostage rescue mission last month in Yemen failed to liberate American Luke Somers because he was not present at the targeted location.\", \"locationName\": \"WASHINGTON,Yemen\", \"lat\": \"38.9172325\", \"lng\": \"-77.0512866\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305/Article_2014-12-04-US-SCI--NASA-Orion%20Test%20Flight/id-ef1d66aecbc8463d9a0419e120deed32\", \"headline\": \"NASA scrubs Orion launch; will try again Friday\", \"description\": \"CAPE CANAVERAL, Fla. (AP) â€” Wind gusts and sluggish fuel valves conspired to keep NASA\u0027s new Orion spacecraft on the launch pad Thursday, delaying a crucial test flight meant to revitalize human exploration.\", \"locationName\": \"CAPE CANAVERAL,Fla.\", \"lat\": \"28.388333\", \"lng\": \"-80.603611\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/386c25518f464186bf7a2ac026580ce7/Article_2014-12-04-US-Police-Chokehold-Death/id-34569ea81ee7408697eb1e4c08299e4f\", \"headline\": \"Civil rights leaders decry decision in chokehold case\", \"description\": \"NEW YORK (AP) â€” Civil rights leaders Thursday decried the grand jury decision not to charge a white police officer in the chokehold death of a black man and announced plans for a march and a summit on racial justice in Washington later this month.\", \"locationName\": \"NEW YORK,Washington\", \"lat\": \"43.2519472\", \"lng\": \"-73.37086959999999\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/386c25518f464186bf7a2ac026580ce7/Article_2014-12-04-US--Cleveland%20Police-Justice%20Probe/id-e40a69c0217b48f7b672349e10c8aa39\", \"headline\": \"US: Cleveland police poorly trained, reckless\", \"description\": \"CLEVELAND (AP) â€” The U.S. Justice Department and Cleveland reached an agreement Thursday to overhaul the city\u0027s police department after federal investigators concluded that officers use excessive and unnecessary force far too often and have endangered the public and their fellow officers with their recklessness.\", \"locationName\": \"CLEVELAND,U.S.\", \"lat\": \"41.49932\", \"lng\": \"-81.6943605\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/386c25518f464186bf7a2ac026580ce7/Article_2014-12-04-US--Police%20Chief-Murder%20Charge/id-0eecf5f5ae3149919a56a1ad4bfede3c\", \"headline\": \"White SC cop who shot unarmed black man charged\", \"description\": \"ORANGEBURG, S.C. (AP) â€” A white police chief who fatally shot an unarmed black man in South Carolina in 2011 was charged with murder, and his lawyer accused prosecutors of taking advantage of national outrage toward police to get the indictment.\", \"locationName\": \"ORANGEBURG,S.C.\", \"lat\": \"33.4918203\", \"lng\": \"-80.8556476\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/386c25518f464186bf7a2ac026580ce7/Article_2014-12-04-US--The%20Jungle%20Cleanout/id-91a90288419f452196ee79841b0caadf\", \"headline\": \"Crews break up homeless camp in Silicon Valley\", \"description\": \"SAN JOSE, Calif. (AP) â€” Police and social-service workers on Thursday began clearing away one of the nation\u0027s largest homeless encampments, a cluster of flimsy tents and plywood shelters that once housed more than 200 people in the heart of wealthy Silicon Valley.\", \"locationName\": \"SAN JOSE,Calif.\", \"lat\": \"37.3393857\", \"lng\": \"-121.8949555\", \"isClosest\": false, \"distance\": -1 }, { \"url\": \"http://hosted2.ap.org/APDEFAULT/386c25518f464186bf7a2ac026580ce7/Article_2014-12-04-US-MED-Flu-Vaccine/id-c7d917644f3e401e97277cc6bd15950a\", \"headline\": \"Flu vaccine may be less effective this winter\", \"description\": \"NEW YORK (AP) â€” The flu vaccine may not be very effective this winter, according to U.S. health officials who worry this may lead to more serious illnesses and deaths.\", \"locationName\": \"NEW YORK,U.S.\", \"lat\": \"40.7127837\", \"lng\": \"-74.0059413\", \"isClosest\": false, \"distance\": -1 } ] }";

}
