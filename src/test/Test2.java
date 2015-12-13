package test;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;



public class Test2 {
	
	public static void main(String[] args) throws Exception  {
		 SAXReader reader = new SAXReader();              
	     Document   document = reader.read(new File("D:\\ot.xml"));   
		 Element root=document.getRootElement();
		 for(Iterator<Element> it=root.elementIterator();it.hasNext();){      
		        Element element = (Element) it.next();        
		        System.out.println(element.attributeValue("value"));
		 }  
		
	}

}
