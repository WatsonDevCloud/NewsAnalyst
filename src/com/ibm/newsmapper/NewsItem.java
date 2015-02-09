package com.ibm.newsmapper;

import java.util.ArrayList;
import java.util.List;

public class NewsItem {
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	
	public boolean isClosest() {
		return isClosest;
	}
	public void setClosest(boolean isClosest) {
		this.isClosest = isClosest;
	}

	public long getDistance() {
		return distance;
	}
	public void setDistance(long distance) {
		this.distance = distance;
	}


	public List<String> getPersonNames() {
		return personNames;
	}
	public void setPersonNames(List<String> personNames) {
		this.personNames = personNames;
	}


	public List<String> getRels() {
		return rels;
	}
	public void setRels(List<String> rels) {
		this.rels = rels;
	}


	public List<String> getEnts() {
		return ents;
	}
	public void setEnts(List<String> ents) {
		this.ents = ents;
	}


	public List<String> getOrgNames() {
		return orgNames;
	}
	public void setOrgNames(List<String> orgNames) {
		this.orgNames = orgNames;
	}


	private String url;
	private String headline;
	private String description;
	private String locationName;
	private String lat;
	private String lng;
	private boolean isClosest = false;
	private long distance = -1L;
	private List<String> personNames = new ArrayList<String>();
	private List<String> orgNames = new ArrayList<String>();
	private List<String> rels = new ArrayList<String>();
	private List<String> ents = new ArrayList<String>();
	
}
