package com.ibm.watson.relexbind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="doc")
public class Doc {
	
	@XmlElement(name="text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlElement(name="mentions")
	public Mentions getMentions() {
		return mentions;
	}
	public void setMentions(Mentions mentions) {
		this.mentions = mentions;
	}
	
	@XmlElement(name="entities")
	public Entities getEntities() {
		return entities;
	}
	public void setEntities(Entities entities) {
		this.entities = entities;
	}
	
	@XmlElement(name="relations")
	public Relations getRelations() {
		return relations;
	}
	public void setRelations(Relations relations) {
		this.relations = relations;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("doc id=").append(id).append(":\n")
			.append("text: ").append(text)
			.append(mentions)
			.append(entities)
			.append(relations);
		
		return sb.toString();
	}

	
	private String text;
	private String id;
	private Mentions mentions;
	private Entities entities;
	private Relations relations;
	
}
