package com.ibm.ic2015.relexbind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class RelMentionArg {	

	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute(name="mid")
	public void setMidAttrib(String midAttrib) {
		this.midAttrib = midAttrib;
	}
	public void setArgNumAttrib(String argNumAttrib) {
		this.argNumAttrib = argNumAttrib;
	}
	
	@XmlAttribute(name="argnum")
	public String getArgNumAttrib() {
		return argNumAttrib;
	}
	public String getMidAttrib() {
		return midAttrib;
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("rel_mention_arg").append(" mid=").append(midAttrib).append(" argnum=").append(argNumAttrib).append(": ").append(value);

		return sb.toString();
	}
	

	private String value;
	private String midAttrib;
	private String argNumAttrib;
	
	
}
