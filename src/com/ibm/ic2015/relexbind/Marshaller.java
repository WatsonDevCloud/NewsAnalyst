package com.ibm.ic2015.relexbind;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

public class Marshaller {

	
	public static Rep marshallXml(String xml) {
	    Rep repDoc = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Rep.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputSource input = new InputSource(new StringReader(xml));
			repDoc = (Rep) jaxbUnmarshaller.unmarshal(input);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	     
	    return repDoc;
	}
	
	
}
