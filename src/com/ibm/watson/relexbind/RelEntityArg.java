package com.ibm.watson.relexbind;

import javax.xml.bind.annotation.XmlAttribute;

public class RelEntityArg {
	// <rel_entity_arg eid="-E7" argnum="1"/>

	

	@XmlAttribute(name="eid")
	public String getEidAttrib() {
		return eidAttrib;
	}
	public void setEidAttrib(String eidAttrib) {
		this.eidAttrib = eidAttrib;
	}
	
	@XmlAttribute(name="argnum")
	public String getArgNumAttrib() {
		return argNumAttrib;
	}
	public void setArgNumAttrib(String argNumAttrib) {
		this.argNumAttrib = argNumAttrib;
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("rel_entity_arg").append(" eid=").append(eidAttrib).append(" argnum=").append(argNumAttrib);

		return sb.toString();
	}
	

	private String eidAttrib;
	private String argNumAttrib;
}
