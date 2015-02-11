package com.ibm.watson.newsanalyst;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;


/**
 * 
 * The app interface to the Watson Relationship service.
 *
 */
public class RelExAPI {

	
	/*
	 * Using Apache HTTPClient library to call the Relationship Extraction REST service.  
	 * Results are an XML document.
	 */
	public static String performExtraction(String text, String sid, String url, String username, String password) {
	
		System.out.println("text: " + text);
		System.out.println("sid: " + sid);
		System.out.println("url: " + url);
		System.out.println("username: " + username);
		System.out.println("password: " + password);
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("txt", text ));
		queryParams.add(new BasicNameValuePair("sid", sid ));
		queryParams.add(new BasicNameValuePair("rt", "xml" ));
		
		String response = "ERROR";
    	try {
    		Executor executor = Executor.newInstance().auth(username, password);
    		URI serviceURI = new URI(url).normalize();
    	    byte[] responseBytes = null;
			
			responseBytes = executor.execute(Request.Post(serviceURI)
			    .bodyString(URLEncodedUtils.format(queryParams, "utf-8"), 
			    		ContentType.APPLICATION_FORM_URLENCODED)
			    ).returnContent().asBytes();

    	    response = new String(responseBytes, "UTF-8");    	    

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return response;
	}
	
}
