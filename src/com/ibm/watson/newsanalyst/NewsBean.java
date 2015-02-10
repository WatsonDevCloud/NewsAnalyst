package com.ibm.watson.newsanalyst;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.ibm.watson.newsanalyst.rss.NewsItem;

public class NewsBean {

	public static void main(String[] args) {
		NewsBean bean = new NewsBean();
		
		bean.setNewsDistance(300L);
		List<NewsItem> news = new ArrayList<NewsItem>();
		
		
		NewsItem item = new NewsItem();
		
		item.setDescription("LIMA, Peru (AP) — Negotiators from more than 190 countries get together Monday in Lima for the last main stop of the U.N. climate negotiations on the road to a planned global warming deal in Paris next year.");
		item.setHeadline("Clock ticking, UN climate talks resume in Lima");
		item.setLat("-12.046374");
		item.setLng("-77.04279339999999");
		item.setLocationName("LIMA,Peru");
		item.setUrl("http://hosted2.ap.org/APDEFAULT/cae69a7523db45408eeb2b3a98c0c9c5/Article_2014-12-01-LT--Peru-Climate%20Talks/id-0d20149257e3410aab240a8e00b88c21");
		news.add(item);
		
		bean.setNewsItems(news);
		
		Gson gson = new Gson();
		
		System.out.println("Returning JSON: " + gson.toJson(bean));
	}
	

	
	public Long getNewsDistance() {
		return newsDistance;
	}
	public void setNewsDistance(Long newsDistance) {
		this.newsDistance = newsDistance;
	}
	
	public List<NewsItem> getNewsItems() {
		return newsItems;
	}
	public void setNewsItems(List<NewsItem> newsItems) {
		this.newsItems = newsItems;
	}
	
	
	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpadted) {
		this.lastUpdated = lastUpadted;
	}


	private String status = "OK";
	private String message = "";
	private Long newsDistance;
	private String lastUpdated;
	private List<NewsItem> newsItems = new ArrayList<NewsItem>();
}
