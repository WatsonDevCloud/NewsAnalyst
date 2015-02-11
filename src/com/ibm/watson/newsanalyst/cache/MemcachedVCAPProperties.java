package com.ibm.watson.newsanalyst.cache;

import java.io.IOException;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;


/**
 * 
 * Reads the MemcachedL  hosts and credentials from the Bluemix
 * environment variable VCAP_SERVICES.
 * 
 */
public class MemcachedVCAPProperties {
	
	private static final String SERVICE_NAME = "memcachedcloud";
	private String hosts = "<service hosts>";
	private String username = "<service username>";
	private String password = "<service password>";
	
	
	public MemcachedVCAPProperties() {
		processVCAP_Services();
	}
	

    /**
     * If exists, process the VCAP_SERVICES environment variable in order to get the 
     * username, password and hosts for memcached service
     * 
     */
    private void processVCAP_Services() {
    	System.out.println("Processing VCAP_SERVICES");
    	
        JSONObject sysEnv = getVcapServices();
        if (sysEnv == null) {
        	return;
        }
        
        if (sysEnv.containsKey(SERVICE_NAME)) {
			JSONArray services = (JSONArray)sysEnv.get(SERVICE_NAME);
			JSONObject service = (JSONObject)services.get(0);
			JSONObject credentials = (JSONObject)service.get("credentials");
			
			hosts = (String)credentials.get("servers");
			username = (String)credentials.get("username");
			password = (String)credentials.get("password");
			
			System.out.println("hosts  = " + hosts);
			System.out.println("username   = " + username);
			System.out.println("password = " + password);
    	} else {
    		System.out.println(SERVICE_NAME + " is not available in VCAP_SERVICES, "
        			+ "please bind the service to your application");
        }
    }

    
    /**
     * Gets the <b>VCAP_SERVICES</b> environment variable and return it
     *  as a JSONObject.
     *
     * @return the VCAP_SERVICES as Json
     */
    private JSONObject getVcapServices() {
        String envServices = System.getenv("VCAP_SERVICES");
        
        if (envServices == null) {
        	return null;
        }
        
        JSONObject sysEnv = null;
        try {
        	 sysEnv = JSONObject.parse(envServices);
        } catch (IOException e) {
        	// Do nothing, fall through to defaults
        	System.out.println("Error parsing VCAP_SERVICES: " + e.getMessage());
        }
        
        return sysEnv;
    }
	
	
	public String getHosts() {
		return hosts;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
 
    
}
