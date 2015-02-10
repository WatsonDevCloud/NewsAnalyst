package com.ibm.watson.relexbind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Entity {
/*	
	   <entity eid="-E8" type="PEOPLE" generic="0" class="SPC" level="NOM" subtype="OTHER" score="0.788773">
	      <mentref mid="-M0">group</mentref>
	      <mentref mid="-M40">group</mentref>
	      <mentref mid="-M61">group</mentref>
	    </entity>
*/
	
//	@XmlValue
//	public String getValue() {
//		return value;
//	}
//	public void setValue(String value) {
//		this.value = value;
//	}
//	
	@XmlAttribute(name="eid")
	public String getEidAttrib() {
		return eidAttrib;
	}
	public void setEidAttrib(String eidAttrib) {
		this.eidAttrib = eidAttrib;
	}
	
	@XmlAttribute(name="type")
	public String getTypeAttrib() {
		return typeAttrib;
	}
	public void setTypeAttrib(String typeAttrib) {
		this.typeAttrib = typeAttrib;
	}
	
	@XmlAttribute(name="generic")
	public String getGenericAttrib() {
		return genericAttrib;
	}
	public void setGenericAttrib(String genericAttrib) {
		this.genericAttrib = genericAttrib;
	}
	
	@XmlAttribute(name="class")
	public String getClassAttrib() {
		return classAttrib;
	}
	public void setClassAttrib(String classAttrib) {
		this.classAttrib = classAttrib;
	}
	
	@XmlAttribute(name="level")
	public String getLevelAttrib() {
		return levelAttrib;
	}
	public void setLevelAttrib(String levelAttrib) {
		this.levelAttrib = levelAttrib;
	}
	
	@XmlAttribute(name="subtype")
	public String getSubtypeAttrib() {
		return subtypeAttrib;
	}
	public void setSubtypeAttrib(String subtypeAttrib) {
		this.subtypeAttrib = subtypeAttrib;
	}
	
	@XmlAttribute(name="score")
	public String getScoreAttrib() {
		return scoreAttrib;
	}
	public void setScoreAttrib(String scoreAttrib) {
		this.scoreAttrib = scoreAttrib;
	}
	
	@XmlElement(name="mentref")
	public List<MentRef> getMentRef() {
		return mentRef;
	}
	public void setMentRef(List<MentRef> mentRef) {
		this.mentRef = mentRef;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("entity").append(" type=").append(typeAttrib).append(":\n");
		for(MentRef mr: mentRef) {
			sb.append(mr.toString()).append("\n");
		}

		return sb.toString();
	}
	
	private String eidAttrib;
	private String typeAttrib;
	private String genericAttrib;
	private String levelAttrib;
	private String subtypeAttrib;
	private String classAttrib;
	private String scoreAttrib;
	private List<MentRef> mentRef = new ArrayList<MentRef>();
	
}
