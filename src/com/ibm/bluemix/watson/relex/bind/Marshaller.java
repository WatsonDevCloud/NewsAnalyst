package com.ibm.bluemix.watson.relex.bind;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

public class Marshaller {

	
	public static Rep unmarshallSireXml2(String xml) {
	    Rep repDoc = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Rep.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputSource input = new InputSource(new StringReader(xml));
			repDoc = (Rep) jaxbUnmarshaller.unmarshal(input);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    return repDoc;
	}
	
	public static Doc unmarshallSireXml(String xml) {
	    Doc sireDoc = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Doc.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputSource input = new InputSource(new StringReader(xml));
			sireDoc = (Doc) jaxbUnmarshaller.unmarshal(input);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    return sireDoc;
	}
	
}
