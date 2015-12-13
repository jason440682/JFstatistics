package classify;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import modeling.Modeling;

public class ID3 extends Modeling {
    //定义：生成多叉树
	Document document = DocumentHelper.createDocument();
    Element root=document.addElement("ID3");
    private double getEntropy(ArrayList<Integer> sub){   //输入的是子集数组的索引
		double entropy=0.0;
		if(sub.size()==0) return entropy;
		String[] label_v=att_val.get(this.label_s);
		int len=label_v.length;
		int[] count=new int[len];
		Arrays.fill(count,0);  //初始化为零
		
		for(int i=0;i<sub.size();i++){
			for(int j=0;j<len;j++){
				if(this.train.get(sub.get(i)).get(label).equals(label_v[j])){   //取label
					count[j]++;
				}
			}
		}
		for(int k=0;k<len;k++){
			double p=(double) count[k]/sub.size();    //在进行整形运算后，需要强制转化为double类型
			if(p!=0.0){			
				entropy-=p*Math.log(p)/Math.log(2);
			}		
		}
		return entropy;
	}
    private void setLeft(Element ele,String predict){
    	ele.addAttribute("child","label");
		Element element=ele.addElement("label");
		element.addText(predict);
		
    }
	
		
	public void buildDT(Element ele,ArrayList<Integer> sub,
			            List<Integer> remainding_att){   //构造决策树
		if(sameLabel(sub)){
			String value=train.get(sub.get(0)).get(this.label);
			setLeft(ele,value);
			return;   //如果类标相同，则返回
		}
		if(remainding_att.size()==0){
			String value=mostLabel(sub);
			setLeft(ele,value);			
			return;  //划分属性用完
		}		
		double minEntropy=1.0; //最小熵
		int min_index=0;   //最少熵对应的属性索引
		HashMap<String,ArrayList<Integer>> min_sub=new HashMap<String,ArrayList<Integer>>();
		//熵最小的划分方式，属性取值对应子数组。
		
		for(int i=0;i<remainding_att.size();i++){     //把属性循环一遍，找出最小熵对应的属性
			int ind=remainding_att.get(i);
			String atr=this.attribute.get(ind);
			String[] values=att_val.get(atr);    //根据属性取得该属性的所以取值
					                                   
			HashMap<String,Integer> count_map=new HashMap<String,Integer>(); 
			//保存取值出现次数
			
			HashMap<String,ArrayList<Integer>> sub_map=new HashMap<String,ArrayList<Integer>>();
			//按属性取值划分的子数组
			
			for(int j=0;j<values.length;j++){
				
				count_map.put(values[j],0);
				sub_map.put(values[j],new ArrayList<Integer>());
			}
			
			for(int k=0;k<sub.size();k++){
				ind=remainding_att.get(i);
				String c_value=this.train.get(sub.get(k)).get(ind);  //当前属性值,ind为对于属性索引
				int count=count_map.get(c_value)+1;
				count_map.put(c_value, count);
				
				ArrayList<Integer> arr=sub_map.get(c_value); //把对应子集取出来，再加进去
				arr.add(sub.get(k));
				sub_map.put(c_value, arr);
				
			}
			//计算熵
			double sum_entropy=0.0;
			for(int kk=0;kk<values.length;kk++){
				String c_value=values[kk];
				int count=count_map.get(c_value);
				ArrayList<Integer> s_sub=sub_map.get(c_value);
				sum_entropy+=(double) count/sub.size()*getEntropy(s_sub);
				
			}
			
			if(sum_entropy<=minEntropy){
				minEntropy=sum_entropy;
				min_index=i;
				min_sub=sub_map;
				
			}						 											
		}
		//这里得到了最佳划分属性对应的索引min_index
		int ind=remainding_att.get(min_index);//从属性索引里面取得索引
		String att=attribute.get(ind);
		ele.addAttribute("child",att);
		for(int i=0;i<min_sub.size();i++){
			
			Element element=ele.addElement(att);
			String v=att_val.get(att)[i];   //属性取值										
			element.addAttribute("value",v);
			ArrayList<Integer> split=min_sub.get(v);
			if(split.size()==0){    //划分的子集为空,则选取未划分前的多数类标
				String value=mostLabel(sub);
				setLeft(element,value);
			}else{
				LinkedList<Integer> copyAtt=new LinkedList<Integer>();    //删除属性
				copyAtt.addAll(remainding_att);
				copyAtt.remove(min_index);
				buildDT(element,split,copyAtt);
			}			
		}	
	}
	
	public void train(){
		ArrayList<Integer> all=new ArrayList<Integer>();   //元组索引
		for(int i=0;i<this.train.size();i++){
			all.add(i);
		}
		
		LinkedList<Integer> att_index=new LinkedList<Integer>();  //属性集索引
		for(int j=0;j<this.attribute.size();j++){
			if(j==label){
				continue;
			}
			att_index.add(j);
		}
		
		buildDT(root,all,att_index);        		
	}
	
	public void writeModel(){
		XMLWriter writer;
		try {
			writer = new XMLWriter(new  FileWriter("D:\\ot.xml"));
			writer.write(document);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	public void predict(){
		int right=0;
		Element p;
		for(int i=0;i<test.size();i++){
			p=root;
			while(!p.attributeValue("child").equals("label")){
				String att=p.attributeValue("child");
				//System.out.println(att);				
				int ind=attribute.indexOf(att);
				String value=test.get(i).get(ind);   //测试集该属性的值
				for(Iterator<Element> it=p.elementIterator();it.hasNext();){  
			        Element element = (Element) it.next();
			        if(element.attributeValue("value").equals(value)){
			        	p=element;
			        	break;
			        }
			    }  
			}
			Element left=p.element("label");
			String pre=left.getText();
			String real=test.get(i).get(label);
			System.out.println("real:"+real+" pre:"+pre);
			if(pre.equals(real)){
				right++;
			}
		}
		double rate=(double)right/test.size();
		System.out.println("right:"+right+" size:"+test.size()+" rate:"+rate);
		
	}
	
	private boolean sameLabel(ArrayList<Integer> sub){  //检查该子集类标是否相同
		String check=train.get(sub.get(0)).get(label);
		for(int i=1;i<sub.size();i++){
			if(!check.equals(train.get(sub.get(i)).get(label))){
				return false;
			}
		}
		return true;
	}
	
	private String mostLabel(ArrayList<Integer> sub){
		String[] label_value=att_val.get(label_s);
		LinkedList<String> label_list=new LinkedList<String>();
        int[] count=new int[label_value.length];
        Arrays.fill(count, 0);
		for(int i=0;i<label_value.length;i++){
        	label_list.add(label_value[i]);
        }
		for(int j=0;j<sub.size();j++){
			String cur_val=this.train.get(sub.get(j)).get(this.label);
			int ind=label_list.indexOf(cur_val);
			count[ind]++;
		}
		int most=0;
		for(int i=0;i<label_value.length;i++){
			if(count[most]<count[i]){
				most=i;
			}
		}		
		return label_value[most];
       
	}
	
	//使用已经存在的决策树模型
	public void readModel(){
		
	}
}
