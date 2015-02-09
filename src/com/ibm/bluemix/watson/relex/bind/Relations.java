package com.ibm.bluemix.watson.relex.bind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class Relations {



	@XmlElement(name="relation")
	public List<Relation> getRelations() {
		return relations;
	}
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	
	@XmlAttribute(name="version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("relations version=" + version + ":").append("\n");
		for(Relation r: relations) {
			sb.append(r.toString()).append("\n");
		}
		
		return sb.toString();
	}
	
	
	private List<Relation> relations = new ArrayList<Relation>();
	private String version;
	
}
