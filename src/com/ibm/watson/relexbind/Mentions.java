package com.ibm.watson.relexbind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class Mentions {

	List<Mention> mentions = new ArrayList<Mention>();

	@XmlElement(name="mention")
	public List<Mention> getMentions() {
		return mentions;
	}
	public void setMentions(List<Mention> mentions) {
		this.mentions = mentions;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("mentions:").append("\n");
		for(Mention m: mentions) {
			sb.append(m.toString()).append("\n");
		}
		
		return sb.toString();
	}
	
}
