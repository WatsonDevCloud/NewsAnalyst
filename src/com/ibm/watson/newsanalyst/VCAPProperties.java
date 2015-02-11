package com.ibm.watson.newsanalyst;

import java.io.IOException;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;


/**
 * Reads the Watson service URL and credentials from the Bluemix
 * environment variable VCAP_SERVICES.
 */
public class VCAPProperties {
	
	private String serviceName = "<service name>";
	private String baseURL = "<service url>";
	private String username = "<service username>";
	private String password = "<service password>";
	
	
	public VCAPProperties(String serviceName) {
		this.serviceName = serviceName;
		processVCAP_Services();
	}
	

    /**
     * If exists, process the VCAP_SERVICES environment variable in order to get the 
     * username, password and baseURL
     */
    private void processVCAP_Services() {
    	System.out.println("Processing VCAP_SERVICES");
    	
        JSONObject sysEnv = getVcapServices();
        if (sysEnv == null) {
        	return;
        }
        
        System.out.println("Looking for: " + serviceName );
        
        if (sysEnv.containsKey(serviceName)) {
			JSONArray services = (JSONArray)sysEnv.get(serviceName);
			JSONObject service = (JSONObject)services.get(0);
			JSONObject credentials = (JSONObject)service.get("credentials");
			
			baseURL = (String)credentials.get("url");
			username = (String)credentials.get("username");
			password = (String)credentials.get("password");
			
			System.out.println("baseURL  = " + baseURL);
			System.out.println("username   = " + username);
			System.out.println("password = " + password);
    	} else {
    		System.out.println(serviceName + " is not available in VCAP_SERVICES, "
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
	
	
	public String getBaseURL() {
		return baseURL;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getServiceName() {
		return serviceName;
	} 
    
}
