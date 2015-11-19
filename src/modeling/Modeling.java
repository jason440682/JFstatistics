package modeling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import read.ReadData;


//建模父类
public class Modeling {
	public List<String> attribute=new LinkedList<String>();  //属性链表
	public HashMap<String,String[]> att_val=new HashMap<String,String[]>(); //各属性的取值
	public List<ArrayList<String>> train=new LinkedList<ArrayList<String>>();  //训练集元祖数据
	public List<ArrayList<String>> test=new LinkedList<ArrayList<String>>();
	public int label;
	public String label_s;
	
	public List<ArrayList<String>> readTest(String path) throws Exception{
		ReadData rdata=new ReadData();
		rdata.readArff(path);
		this.test=rdata.getData();
		return this.test;
	}
	
	public void  readTrain(String path) throws IOException{   //数 入为数据文件路径
		ReadData rdata=new ReadData();
		rdata.readArff(path);
		this.attribute=rdata.getArrtibute();
		this.att_val=rdata.getAttVal();
		this.train=rdata.getData();
	}
	
	public void setLabel(int i){
		this.label=i;
	}
	public void setLabel(String label_s){
		this.label_s=label_s;
		this.label=attribute.indexOf(label_s);
	}
}
