package com.ibm.watson.relexbind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Entities {

	List<Entity> entities = new ArrayList<Entity>();

	@XmlElement(name="entity")
	public List<Entity> getEntities() {
		return entities;
	}
	public void setMentions(List<Entity> Entities) {
		this.entities = Entities;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("entities:").append("\n");
		for(Entity e: entities) {
			sb.append(e.toString()).append("\n");
		}
		
		return sb.toString();
	}
	
}
