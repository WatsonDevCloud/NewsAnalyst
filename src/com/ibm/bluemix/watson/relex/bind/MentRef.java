package com.ibm.bluemix.watson.relex.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class MentRef {
	
	//   <mentref mid="-M81">seven weeks</mentref>
	
	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute(name="mid")
	public String getMidAttrib() {
		return midAttrib;
	}
	public void setMidAttrib(String midAttrib) {
		this.midAttrib = midAttrib;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("mentref").append(" mid=").append(midAttrib).append(": ").append(value);

		return sb.toString();
	}
	
	
	private String value;
	private String midAttrib;
}
