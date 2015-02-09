package com.ibm.bluemix.watson.relex.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Path("/rx")
public class RXService {

	@POST
	@Produces("text/plain")
	public String getRelations(@FormParam("text") String text) {
		RelExAPI relEx;
		try {
			System.out.println("Input text:\n" + text);
			
			String xml = RelExAPI.performExtraction(text);
			
			System.out.println("XML Results:\n" + xml);
				
			relEx = new RelExAPI(xml);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			return errors.toString();
		}	
	
		return (relEx.getEntitiesString() + "\n" + relEx.getRelationsString());
	}

	
	@GET
	public String getInformation() {
		return "GET is not supported";
	}
	
	

	
}