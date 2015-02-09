package com.ibm.bluemix.watson.relex.bind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class RelMention {
	
	@XmlAttribute(name="rmid")
	public String getRmidAttrib() {
		return rmidAttrib;
	}
	public void setRmidAttrib(String rmidAttrib) {
		this.rmidAttrib = rmidAttrib;
	}
	
	@XmlAttribute(name="score")
	public String getScoreAttrib() {
		return scoreAttrib;
	}
	public void setScoreAttrib(String scoreAttrib) {
		this.scoreAttrib = scoreAttrib;
	}
	
	@XmlAttribute(name="class")
	public String getClassAttrib() {
		return classAttrib;
	}
	public void setClassAttrib(String classAttrib) {
		this.classAttrib = classAttrib;
	}
	
	@XmlAttribute(name="modality")
	public String getModalityAttrib() {
		return modalityAttrib;
	}
	public void setModalityAttrib(String modalityAttrib) {
		this.modalityAttrib = modalityAttrib;
	}
	
	@XmlAttribute(name="tense")
	public String getTenseAttrib() {
		return tenseAttrib;
	}
	public void setTenseAttrib(String tenseAttrib) {
		this.tenseAttrib = tenseAttrib;
	}
	
	@XmlElement(name="rel_mention_arg")
	public List<RelMentionArg> getRelMentionArgs() {
		return relMentionArgs;
	}
	public void setRelMentionArgs(List<RelMentionArg> relMentionArgs) {
		this.relMentionArgs = relMentionArgs;
	}
/*	
    <relmentions>
    <relmention rmid="-R9-1" score="0.916322" class="SPECIFIC" modality="ASSERTED" tense="UNSPECIFIED">
      <rel_mention_arg mid="-M35" argnum="1">who</rel_mention_arg>
      <rel_mention_arg mid="-M36" argnum="2">predicted</rel_mention_arg>
    </relmention>
  </relmentions>
*/	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("relmention")
			.append(" rmid=").append(rmidAttrib)
			.append(" score=").append(scoreAttrib)
			.append(" class=").append(classAttrib)
			.append(" modality=").append(modalityAttrib)
			.append(" tense=").append(tenseAttrib).append("\n");
		
		for(RelMentionArg rma: relMentionArgs) {
			sb.append(rma.toString()).append("\n");
		}

		return sb.toString();
	}
	

	private String rmidAttrib;
	private String scoreAttrib;
	private String classAttrib;
	private String modalityAttrib;
	private String tenseAttrib;
	private List<RelMentionArg> relMentionArgs = new ArrayList<RelMentionArg>();


}
