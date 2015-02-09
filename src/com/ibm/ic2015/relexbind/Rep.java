package com.ibm.ic2015.relexbind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="rep")
public class Rep {
	
	@XmlAttribute(name="sts")
	public String getStsAttrib() {
		return stsAttrib;
	}
	public void setStsAttrib(String stsAttrib) {
		this.stsAttrib = stsAttrib;
	}
	
	@XmlElement(name="doc")
	public Doc getDoc() {
		return doc;
	}
	public void setDoc(Doc doc) {
		this.doc = doc;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("rep").append(" sts=").append(stsAttrib).append(":\n").append(doc);
		
		return sb.toString();
	}
	
	
	private Doc doc;
	private String stsAttrib;
}
