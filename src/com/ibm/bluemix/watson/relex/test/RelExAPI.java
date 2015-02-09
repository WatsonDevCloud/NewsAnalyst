package com.ibm.bluemix.watson.relex.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.bluemix.watson.relex.bind.Entity;
import com.ibm.bluemix.watson.relex.bind.Marshaller;
import com.ibm.bluemix.watson.relex.bind.RelEntityArg;
import com.ibm.bluemix.watson.relex.bind.Relation;
import com.ibm.bluemix.watson.relex.bind.Rep;

public class RelExAPI {

	public static void main(String[] args) throws IOException {
		
		
//		Path path = FileSystems.getDefault().getPath("/Users/slawsonb/eclipse/workspaceIBM2/watson-bluemix-api", "test.txt");
//		byte[] bytes = Files.readAllBytes(path);
		
		
		String inputText = "";
		//inputText = "Beat:B1, ALARMS - COMMERCIAL BURGLARY (FALSE) at 18XX BLOCK OF NW BLUE RIDGE DR reported on 11/21/2014 8:33 AM, Call# 14000389374";
		inputText = "Bruce lives in Palmdale. He was born at Holy Cross Hospital.";
		//inputText = "Traffic Injury Pri 4F - 101 - 329 S Capital Of Texas Hwy Nb - ESD09 - 02:07AM";
		
		//		String xml = relEx.test2(new String(bytes));
		
		try {
			String xml = performExtraction(inputText);
			
			System.out.println(xml);
			
			RelExAPI relEx = new RelExAPI(xml);
			
			System.out.println(relEx.getEntitiesString() + "\n" + relEx.getRelationsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		Rep repDoc = Marshaller.unmarshallSireXml2(xml);
		
//		System.out.println(repDoc);
				

		//System.out.println(relEx.getRelationsString());
		
		
//		relEx.printEntities();
//		relEx.printRelations();
		
//		printPerson(repDoc);
//		printPerson(repDoc, "NAM");
//		printPeople(repDoc);
//		printOrgs(repDoc);
//		printPlaces(repDoc);
	}

	private HashMap<String, Entity> eidMap = new HashMap<String, Entity>();
	private Rep repDoc;
	
	public Rep getRep() {
		return repDoc;
	}
	
	public RelExAPI(String xml) {
		this.repDoc = Marshaller.unmarshallSireXml2(xml);
		
		if(repDoc.getDoc() == null) {
			throw new RuntimeException("Relationship Extraction service is unavailable: status=" + repDoc.getStsAttrib());
		}
		
		for(Entity e: repDoc.getDoc().getEntities().getEntities()) {
			eidMap.put(e.getEidAttrib(), e);
		}
	}
	

	public static String getDisplayName(Entity entity) {
		return entity.getMentRef().get(0).getValue();
	}

	private String getDisplayName(String eid) {
		return getDisplayName(getEntityByEid(eid));
	}
	
	private Entity getEntityByEid(String eid) {
		return eidMap.get(eid);
	}
	

	
	public List<String> getRelationsList() {
		StringBuilder sb = new StringBuilder();
		sb.append("Relations:\n");
		
		List<Relation> rels = repDoc.getDoc().getRelations().getRelations();
		List<String> relList = new ArrayList<String>();
		
		for(Relation rel: rels) {
			List<RelEntityArg> rmas = rel.getRelEntityArgs();
			relList.add((getDisplayName(rmas.get(0).getEidAttrib())) + " --- (" + rel.getTypeAttrib() + ") --- " + getDisplayName(rmas.get(1).getEidAttrib()));
		}
		
		return relList;
	}
	
	
	public List<String> getEntitiesList() {
		List<String> entList = new ArrayList<String>();
		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
		
	
		for(Entity entity: entities) {
			StringBuilder sb = new StringBuilder();
			String typeString = entity.getTypeAttrib();
			if(typeString.equalsIgnoreCase("GPE")) {
				typeString = "PLACE";
			}
			sb.append(getDisplayName(entity)).append( " (").append(typeString);
			String levelString = entity.getLevelAttrib();
			if(levelString.equalsIgnoreCase("NAM")) {
				sb.append( ", PROPER NAME");
			}
			sb.append( ")");
			entList.add(sb.toString());
		}
		
		return entList;
	}
	
	public String getRelationsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Relations:\n");
		
		List<Relation> rels = repDoc.getDoc().getRelations().getRelations();
		
		for(Relation rel: rels) {
			//sb.append("RELATION rid=" + rel.getRidAttrib() + ", type=" + rel.getTypeAttrib() + ", subtype=" + rel.getSubtypeAttrib());
			List<RelEntityArg> rmas = rel.getRelEntityArgs();
			sb.append("\t'").append(getDisplayName(rmas.get(0).getEidAttrib())).append("'  <----- (").append(rel.getTypeAttrib()).append(") ---->  '").append(getDisplayName(rmas.get(1).getEidAttrib())).append("'\n");
		}
		
		return sb.toString();
	}
	
	
	public List<String> getPersonNames() {		
		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
		
		List<String> personNames = new ArrayList<String>();
		
		for(Entity entity: entities) {
			String typeString = entity.getTypeAttrib();
			String levelString = entity.getLevelAttrib();
			
			if(typeString.equalsIgnoreCase("PERSON") && levelString.equalsIgnoreCase("NAM")) {
				personNames.add(getDisplayName(entity));
//			} else {
//				System.out.println("Skipping: " + getDisplayName(entity));
			}
		}

		return personNames;
	}	

	public List<String> getOrgNames(boolean skipNewsOrg) {		
		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
		
		List<String> orgNames = new ArrayList<String>();
		
		for(Entity entity: entities) {
			String typeString = entity.getTypeAttrib();
			String levelString = entity.getLevelAttrib();
			
			if(typeString.equalsIgnoreCase("ORGANIZATION") && levelString.equalsIgnoreCase("NAM")) {
				String orgName = getDisplayName(entity);
				//System.out.println("Fount ORG: " + getDisplayName(entity));
				if((skipNewsOrg && ("AP".equals(orgName) || "Reuters".equals(orgName))) == false) {
					orgNames.add(orgName);
				}
			} else {
				//System.out.println("Skipping: " + getDisplayName(entity) + ", typeString= " + typeString + ", levelString=" + levelString);
			}
		}

		return orgNames;
	}
	
	
	
	
	
	public List<Entity> getLocations() {
		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
		List<Entity> locationEntities = new ArrayList<Entity>();
		
		for(Entity entity: entities) {
			String typeString = entity.getTypeAttrib();
			if(typeString.equalsIgnoreCase("GPE")) {
				String levelString = entity.getLevelAttrib();
				if(levelString.equalsIgnoreCase("NAM")) {
					locationEntities.add(entity);
				}
			}
		}
		
		return locationEntities;
	}
	

	public String getEntitiesString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Entities:\n");

		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
		
		for(Entity entity: entities) {
			String typeString = entity.getTypeAttrib();
			if(typeString.equalsIgnoreCase("GPE")) {
				typeString = "PLACE";
			}
			sb.append( "\t'").append(getDisplayName(entity)).append( "' (").append(typeString);
			String levelString = entity.getLevelAttrib();
			if(levelString.equalsIgnoreCase("NAM")) {
				sb.append( ", PROPER NAME");
			}
			sb.append( ")\n");
		
		}
		
		return sb.toString();
	}	
	

	public void printRelations() {
		System.out.println("\nRelations: ");
		List<Relation> rels = repDoc.getDoc().getRelations().getRelations();
		
		for(Relation rel: rels) {
			System.out.println("RELATION rid=" + rel.getRidAttrib() + ", type=" + rel.getTypeAttrib() + ", subtype=" + rel.getSubtypeAttrib());
			List<RelEntityArg> rmas = rel.getRelEntityArgs();
			for(RelEntityArg rma: rmas) {
				System.out.println("\trel_entity_arg eid=" + rma.getEidAttrib() + ", argnum=" + rma.getArgNumAttrib() + ": " + getDisplayName(rma.getEidAttrib()));
			}
		
		}
	}	
	
	
	public void printEntities() {
		System.out.println("\nEntities: ");
		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
		
		for(Entity entity: entities) {
			System.out.println(getDisplayName(entity) + ", eid=" + entity.getEidAttrib() + ", type=" + entity.getTypeAttrib() + ", level=" + entity.getLevelAttrib());
		}
	}	
	
	private static String SID = "ie-en-news";
	private static String USERNAME = "c5d5d63e-65fb-4389-a1fe-958a645562a4";
	private static String PASSWORD = "TJp1ByhdwtxE";
	private static String URL = "https://gateway.watsonplatform.net/laser/service/api/v1/sire/94d94ddd-d414-4dd0-b2a4-d2d03046cc55";


	
	public static String performExtraction(String text) {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("txt", text ));
		qparams.add(new BasicNameValuePair("sid", SID ));
		qparams.add(new BasicNameValuePair("rt", "xml" ));
		
		String response = null;
    	try {
    		Executor executor = Executor.newInstance().auth(USERNAME, PASSWORD);
    		URI serviceURI = new URI(URL).normalize();
    	    byte[] responseBytes = null;
			try {
				responseBytes = executor.execute(Request.Post(serviceURI)
				    .bodyString(URLEncodedUtils.format(qparams, "utf-8"), 
				    		ContentType.APPLICATION_FORM_URLENCODED)
				    ).returnContent().asBytes();
			} catch (Exception e) {
				throw new RuntimeException("Relationship Extraction service is unavailable: status=" + e.getMessage());
			}

    	    response = new String(responseBytes, "UTF-8");    	    
		} catch (Exception e) {
			throw new RuntimeException("Relationship Extraction service is unavailable: status=" + e.getMessage());
		}
    	
    	return response;
	}
	
	
	
	
	
	public String testOLD(String text) {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("txt", text ));
		qparams.add(new BasicNameValuePair("sid", SID ));
		qparams.add(new BasicNameValuePair("rt", "xml" ));
		
		String response = null;
    	try {
    		Executor executor = Executor.newInstance().auth(USERNAME, PASSWORD);
    		URI serviceURI = new URI(URL).normalize();
    	    String auth = USERNAME + ":" + PASSWORD;
    	    byte[] responseB = executor.execute(Request.Post(serviceURI)
			    .addHeader("Authorization", "Basic "+ Base64.encodeBase64String(auth.getBytes()))
			    .bodyString(URLEncodedUtils.format(qparams, "utf-8"), 
			    		ContentType.APPLICATION_FORM_URLENCODED)
			    ).returnContent().asBytes();

    	    response = new String(responseB, "UTF-8");
    	    
    	    System.out.println("Response: " + response);
    	    
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return response;
	}
	
/*	
	{
		  "relationship_extraction": {
		    "name": "Relationship Extraction-os",
		    "label": "relationship_extraction",
		    "plan": "relationship_extraction_free_plan",
		    "credentials": {
		      "url": "https://gateway.watsonplatform.net/laser/service/api/v1/sire/94d94ddd-d414-4dd0-b2a4-d2d03046cc55",
		      "sids": [
		        {
		          "sid": "ie-es-news",
		          "description": "information extraction from Spanish news"
		        },
		        {
		          "sid": "ie-en-news",
		          "description": "information extraction from English news"
		        }
		      ],
		      "username": "c5d5d63e-65fb-4389-a1fe-958a645562a4",
		      "password": "TJp1ByhdwtxE"
		    }
		  }
		}
*/
	
	
	
}
