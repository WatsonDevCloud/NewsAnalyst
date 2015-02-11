package com.ibm.watson.newsanalyst.test;


import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ibm.watson.newsanalyst.RelExAPI;
import com.ibm.watson.newsanalyst.VCAPProperties;
import com.ibm.watson.relexbind.Marshaller;
import com.ibm.watson.relexbind.RelExUtil;
import com.ibm.watson.relexbind.Rep;


@Path("/tester")
public class TesterService {

	@POST
	@Produces("text/plain")
	public String getRelations(@FormParam("text") String text) {
		RelExUtil util;
		
		try {
			
			VCAPProperties vcp = new VCAPProperties("relationship_extraction"); 
			
			System.out.println("Input text:\n" + text);
			
			String xml = RelExAPI.performExtraction(text, 
					"ie-en-news", 
					vcp.getBaseURL(), 
					vcp.getUsername(), 
					vcp.getPassword());
			
			System.out.println("XML Results:\n" + xml);
				
			
			Rep rep = Marshaller.marshallXml(xml);
			
			System.out.println(rep);
			
			util = new RelExUtil(rep);
			
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			return errors.toString();
		}	
	
		return (RelExUtil.stringFromList(util.getEntitiesList()) + "\n" + RelExUtil.stringFromList(util.getRelationsList()));
	}

	
	@GET
	public String getInformation() {
		return "GET is not supported";
	}
	
	

	
}