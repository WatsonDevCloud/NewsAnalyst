package com.ibm.watson.relexbind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Mention {
	
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
	
	@XmlAttribute(name="mtype")
	public String getMtypeAttrib() {
		return mtypeAttrib;
	}
	public void setMtypeAttrib(String mtypeAttrib) {
		this.mtypeAttrib = mtypeAttrib;
	}
	
	@XmlAttribute(name="begin")
	public String getBeginAttrib() {
		return beginAttrib;
	}
	public void setBeginAttrib(String beginAttrib) {
		this.beginAttrib = beginAttrib;
	}
	
	@XmlAttribute(name="end")
	public String getEndAttrib() {
		return endAttrib;
	}
	public void setEndAttrib(String endAttrib) {
		this.endAttrib = endAttrib;
	}
	
	@XmlAttribute(name="head-begin")
	public String getHeadBeginAttrib() {
		return headBeginAttrib;
	}
	public void setHeadBeginAttrib(String headBeginAttrib) {
		this.headBeginAttrib = headBeginAttrib;
	}
	
	@XmlAttribute(name="head-end")
	public String getHeadEndAttrib() {
		return headEndAttrib;
	}
	public void setHeadEndAttrib(String headEndAttrib) {
		this.headEndAttrib = headEndAttrib;
	}
	
	@XmlAttribute(name="eid")
	public String getEidAttrib() {
		return eidAttrib;
	}
	public void setEidAttrib(String eidAttrib) {
		this.eidAttrib = eidAttrib;
	}
	
	@XmlAttribute(name="etype")
	public String getEtypeAttrib() {
		return etypeAttrib;
	}
	public void setEtypeAttrib(String etypeAttrib) {
		this.etypeAttrib = etypeAttrib;
	}
	
	@XmlAttribute(name="role")
	public String getRoleAttrib() {
		return roleAttrib;
	}
	public void setRoleAttrib(String roleAttrib) {
		this.roleAttrib = roleAttrib;
	}
	
	@XmlAttribute(name="metonymy")
	public String getMetonymyAttrib() {
		return metonymyAttrib;
	}
	public void setMetonymyAttrib(String metonymyAttrib) {
		this.metonymyAttrib = metonymyAttrib;
	}
	
	@XmlAttribute(name="class")
	public String getClassAttrib() {
		return classAttrib;
	}
	public void setClassAttrib(String classAttrib) {
		this.classAttrib = classAttrib;
	}
	
	@XmlAttribute(name="score")
	public String getScoreAttrib() {
		return scoreAttrib;
	}
	public void setScoreAttrib(String scoreAttrib) {
		this.scoreAttrib = scoreAttrib;
	}
	
	@XmlAttribute(name="corefScore")
	public String getCorefScoreAttrib() {
		return corefScoreAttrib;
	}
	public void setCorefScoreAttrib(String corefScoreAttrib) {
		this.corefScoreAttrib = corefScoreAttrib;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("mention").append(" mid=").append(midAttrib).append(" etype=").append(etypeAttrib).append(": ").append(value);

		return sb.toString();
	}
	
	
	private String value;
	private String midAttrib;
	private String mtypeAttrib;
	private String beginAttrib;
	private String endAttrib;
	private String headBeginAttrib;
	private String headEndAttrib;
	private String eidAttrib;
	private String etypeAttrib;
	private String roleAttrib;
	private String metonymyAttrib;
	private String classAttrib;
	private String scoreAttrib;
	private String corefScoreAttrib;

}
