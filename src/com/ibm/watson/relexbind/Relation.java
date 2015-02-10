package com.ibm.watson.relexbind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Relation {
	

	
	@XmlAttribute(name="rid")
	public String getRidAttrib() {
		return ridAttrib;
	}
	public void setRidAttrib(String ridAttrib) {
		this.ridAttrib = ridAttrib;
	}
	
	@XmlAttribute(name="type")
	public String getTypeAttrib() {
		return typeAttrib;
	}
	public void setTypeAttrib(String typeAttrib) {
		this.typeAttrib = typeAttrib;
	}
	
	@XmlAttribute(name="subtype")
	public String getSubtypeAttrib() {
		return subtypeAttrib;
	}
	public void setSubtypeAttrib(String subtypeAttrib) {
		this.subtypeAttrib = subtypeAttrib;
	}
	
	@XmlElement(name="rel_entity_arg")
	public List<RelEntityArg> getRelEntityArgs() {
		return relEntityArgs;
	}
	public void setRelEntityArgs(List<RelEntityArg> relEntityArgs) {
		this.relEntityArgs = relEntityArgs;
	}
	
	@XmlElement(name="relmentions")
	public RelMentions getRelMentions() {
		return relMentions;
	}
	public void setRelMentions(RelMentions relMentions) {
		this.relMentions = relMentions;
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("relation").append(" rid=").append(ridAttrib).append(" type=").append(typeAttrib).append(" subtype=").append(subtypeAttrib).append(": \n");
		for(RelEntityArg rea: relEntityArgs) {
			sb.append(rea.toString()).append("\n");
		}
		sb.append(relMentions.toString()).append("\n");
		
		return sb.toString();
	}
	
	

	private String ridAttrib;
	private String typeAttrib;
	private String subtypeAttrib;
	private List<RelEntityArg> relEntityArgs = new ArrayList<RelEntityArg>();
	private RelMentions relMentions;

	
}
