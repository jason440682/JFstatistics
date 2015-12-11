package test;

import java.io.FileWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;



public class Test2 {
	
	public static void main(String[] args) throws Exception  {
		 Document document = DocumentHelper.createDocument();
         Element root=document.addElement("jason");
         Element ele=root.addElement("性别");
         System.out.println(ele.getName());
         Element ele2=root.addElement("身高");
         XMLWriter writer = new XMLWriter(new  FileWriter("D:\\ot.xml"));
         root.elementIterator()
         writer.write(document);
         writer.close();
     	
	}

}
