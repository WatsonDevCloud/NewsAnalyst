package com.ibm.watson.relexbind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RelExUtil {

	
	private HashMap<String, Entity> eidMap = new HashMap<String, Entity>();
	private Rep repDoc;
	
	
	public RelExUtil(Rep repDoc) {
		this.repDoc = repDoc;
		
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
	
	
	public List<String> getPersonNames() {		
		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
		
		List<String> personNames = new ArrayList<String>();
		
		for(Entity entity: entities) {
			String typeString = entity.getTypeAttrib();
			String levelString = entity.getLevelAttrib();
			
			if(typeString.equalsIgnoreCase("PERSON") && levelString.equalsIgnoreCase("NAM")) {
				personNames.add(getDisplayName(entity));
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
				orgNames.add(orgName);

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
	
	
	
	/*
	 * Helper to print lists of strings.
	 */
	public static String stringFromList(List<String> stringList) {
		StringBuilder sb = new StringBuilder();
		for(String str: stringList) {
			sb.append(str).append("\n");
		}
			
		return sb.toString();
	}
	
	
	
	
	
	
//	public String getRelationsString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Relations:\n");
//		
//		List<Relation> rels = repDoc.getDoc().getRelations().getRelations();
//		
//		for(Relation rel: rels) {
//			List<RelEntityArg> rmas = rel.getRelEntityArgs();
//			sb.append("\t'").append(getDisplayName(rmas.get(0).getEidAttrib())).append("'  <----- (").append(rel.getTypeAttrib()).append(") ---->  '").append(getDisplayName(rmas.get(1).getEidAttrib())).append("'\n");
//		}
//		
//		return sb.toString();
//	}
//
//
//	
//
//	public String getEntitiesString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Entities:\n");
//
//		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
//		
//		for(Entity entity: entities) {
//			String typeString = entity.getTypeAttrib();
//			if(typeString.equalsIgnoreCase("GPE")) {
//				typeString = "PLACE";
//			}
//			sb.append( "\t'").append(getDisplayName(entity)).append( "' (").append(typeString);
//			String levelString = entity.getLevelAttrib();
//			if(levelString.equalsIgnoreCase("NAM")) {
//				sb.append( ", PROPER NAME");
//			}
//			sb.append( ")\n");
//		
//		}
//		
//		return sb.toString();
//	}	
//	
//
//	public void printRelations() {
//		System.out.println("\nRelations: ");
//		List<Relation> rels = repDoc.getDoc().getRelations().getRelations();
//		
//		for(Relation rel: rels) {
//			System.out.println("RELATION rid=" + rel.getRidAttrib() + ", type=" + rel.getTypeAttrib() + ", subtype=" + rel.getSubtypeAttrib());
//			List<RelEntityArg> rmas = rel.getRelEntityArgs();
//			for(RelEntityArg rma: rmas) {
//				System.out.println("\trel_entity_arg eid=" + rma.getEidAttrib() + ", argnum=" + rma.getArgNumAttrib() + ": " + getDisplayName(rma.getEidAttrib()));
//			}
//		
//		}
//	}	
//	
//	
//	public void printEntities() {
//		System.out.println("\nEntities: ");
//		List<Entity> entities = repDoc.getDoc().getEntities().getEntities();
//		
//		for(Entity entity: entities) {
//			System.out.println(getDisplayName(entity) + ", eid=" + entity.getEidAttrib() + ", type=" + entity.getTypeAttrib() + ", level=" + entity.getLevelAttrib());
//		}
//	}	

	
	
	
}
