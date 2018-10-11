package com.alexmawashi.nio.utils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlConverter<T> {

    private static XmlConverter instance = null;
    public synchronized static XmlConverter getInstance(){
        if(instance == null){
            instance = new XmlConverter();
        }
        return instance;
    }

    public Object getDataFromBodyRequest(HttpServletRequest request, Class clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        return JAXB.unmarshal(new StringReader(sb.toString()), clazz);
    }

    public String getXmlOf(Object object){
        StringWriter sw = new StringWriter();
        JAXB.marshal(object, sw);
        return sw.toString();
    }


}
