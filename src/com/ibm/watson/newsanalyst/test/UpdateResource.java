package com.ibm.watson.newsanalyst.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ibm.watson.newsanalyst.cache.NewsCache;

@Path("/update")
public class UpdateResource {
	
	@GET
	public String update() {
		
		try {
			NewsCache.updateCache();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAILED!";
		}
		
		return "UPDATED!";
	}

}
