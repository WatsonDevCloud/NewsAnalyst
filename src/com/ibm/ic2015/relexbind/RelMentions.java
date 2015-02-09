package com.ibm.ic2015.relexbind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class RelMentions {

	@XmlElement(name="relmention")
	public List<RelMention> getRelMentions() {
		return relMentions;
	}
	public void seReltMentions(List<RelMention> relMentions) {
		this.relMentions = relMentions;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("relmentions:").append("\n");
		for(RelMention rm: relMentions) {
			sb.append(rm.toString()).append("\n");
		}
		
		return sb.toString();
	}
	
	
	private List<RelMention> relMentions = new ArrayList<RelMention>();
	
}
