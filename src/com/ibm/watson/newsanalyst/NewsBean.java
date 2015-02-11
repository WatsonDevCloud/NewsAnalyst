package com.ibm.watson.newsanalyst;

import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.newsanalyst.rss.NewsItem;

/**
 * 
 * Java bean to represent the payload provided by the 
 * application API.  The application will convert this 
 * to JSON before serving it up.
 *
 */
public class NewsBean {
	
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
