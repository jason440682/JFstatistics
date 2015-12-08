package association;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import utility.SequenceList;

public class AssociationAnalysis {
    //输入项以自然数1，2，3，4，……表示
	private double minSupport=0.0;  //支持度
	private double minConf=0.0;         //置信度
	private LinkedList<SequenceList>  data=new LinkedList<SequenceList>();
	private HashMap<SequenceList,Integer> supportCount=new HashMap<SequenceList,Integer>();
	private LinkedList<AssociationRule> ruleList=new LinkedList<AssociationRule>(); 
	private LinkedList<LinkedList<SequenceList>> kItemList=new LinkedList<LinkedList<SequenceList>>();
	public void setMinSupport(double support){
		this.minSupport=support;
	}
	public void setMinConfidence(double confidence){
		this.minConf=confidence;
	}
	
	public void frequentRule(){
		for(int i=1;i<kItemList.size();i++){
			LinkedList<SequenceList> kItem=kItemList.get(i);   //频繁k项集		
			for(int j=0;j<kItem.size();j++){
				SequenceList one_kItem=kItem.get(j);           //一个频繁k项集
				LinkedList<SequenceList> oneBackItem=getOneBack(one_kItem);   //规则的一项后件
				apGenRules(one_kItem,oneBackItem);
			}
		}
	}
	
	public void apGenRules(SequenceList one_kItem,LinkedList<SequenceList> backItem){
		int k=one_kItem.size();            //评分项集大小
		int m=backItem.get(0).size();   //规则后件大小
		if(k>m){
			int size=backItem.size();
			for(int i=0;i<size;i++){
				SequenceList frontItem=getFront(one_kItem,backItem.get(i));
				int fk=supportCount.get(one_kItem);
				int fk_h=getSupportCount(frontItem);
				double conf=(double) fk/fk_h;//频繁项集支持度计数除以前项支持度计数
			    if(conf>minConf){
			    	AssociationRule rule=new AssociationRule(frontItem,backItem.get(i));
			    	ruleList.add(rule);		    	
			    }else{
			    	backItem.remove(i);
			    	i--;
			    	size--;
			    }
			}
			LinkedList<SequenceList> genBackItem=apriori_gen(backItem);		   	
			if(genBackItem.size()!=0){
				apGenRules(one_kItem,genBackItem);
			}	   
		}
	}
	
	public SequenceList getFront(SequenceList all,SequenceList back){
		SequenceList front=new SequenceList();
		for(int i=0;i<all.size();i++){
			if(!back.contain(all.get(i))){
				front.add(all.get(i));
			}
		}
		return front;
	}
	
	private int getSupportCount(SequenceList front){
		int len=front.size();
		LinkedList<SequenceList> itemList=kItemList.get(len-1);
		for(int i=0;i<itemList.size();i++){
			SequenceList temp=itemList.get(i);
			boolean equal=true;
			for(int j=0;j<len;j++){
				if(!temp.contain(front.get(j))){
					equal=false;
				}
			}
			if(equal){
				return supportCount.get(temp);
			}
		}
		return 0;		
	}
	public LinkedList<SequenceList> getOneBack(SequenceList one_kItem){
		LinkedList<SequenceList> oneBackItem=new LinkedList<SequenceList>();
		for(int i=0;i<one_kItem.size();i++){
			SequenceList item=new SequenceList();
			item.add(one_kItem.get(i));
			oneBackItem.add(item);
		}
		return oneBackItem;
	}
	
	public void getFrequent(){  //得到所有的频繁项集
		LinkedList<SequenceList> oneItem=getItem();
		LinkedList<SequenceList> index=oneItem;
		int k=1;
		while(index.size()!=0){
			kItemList.add(index);
			k=k+1;
			LinkedList<SequenceList> candidate=apriori_gen(index);
			LinkedList<SequenceList> cut=new LinkedList<SequenceList>();
			for(int i=0;i<candidate.size();i++){
				int support=getSupport(candidate.get(i));
				//注意删除的时候链表长度的改变
				if(support>=minSupport*data.size()){
					cut.add(candidate.get(i));
					supportCount.put(candidate.get(i),support);
				}				
			}
			index=cut;
		}
	}
	
	public int getSupport(SequenceList kItem){
		int support=0;
		for(int i=0;i<data.size();i++){
			boolean contain=true;
			for(int j=0;j<kItem.size();j++){
				if(!data.get(i).contain(kItem.get(j))){
					contain=false;
					break;
				}
			}
			if(contain){
				support++;
			}
		}
		return support;
	}
	
	public  LinkedList<SequenceList>  getItem(){   //得到频繁一项集
		LinkedList<SequenceList> oneItem=new LinkedList<SequenceList>();
		SequenceList item=new SequenceList();
		for(int i=0;i<data.size();i++){
 			SequenceList affair=data.get(i);
 			for(int j=0;j<affair.size();j++){
 				//这里的判断太慢了
 				if(!item.contain(affair.get(j))){
 					item.add(affair.get(j));
 					System.out.println("add:"+affair.get(j));
 				}
 			}
 		}
		System.out.println("item:"+item.size());
		
		int[] count=new int[item.size()];   //计算支持度计数
		Arrays.fill(count,0);
		for(int i=0;i<item.size();i++){
			for(int j=0;j<data.size();j++){
				SequenceList affair=data.get(j);
	 			if(affair.contain(item.get(i))){
	 				count[i]++;
	 			}
			}
		}
 		for(int i=0;i<item.size();i++){
			if(count[i]>item.size()*minSupport){
				SequenceList eachItem=new SequenceList();
				eachItem.add(item.get(i));
				supportCount.put(eachItem,count[i]);
				oneItem.add(eachItem);
			}			
		}
		return oneItem;
	}
	
	public LinkedList<SequenceList> apriori_gen(LinkedList<SequenceList> kItem){
		//传入的是k项集，生成k+1项集  k>1   
		LinkedList<SequenceList> increasedItem=new LinkedList<SequenceList>();	
		for(int i=0;i<kItem.size()-1;i++){   //最后一项就不用了
			for(int j=i+1;j<kItem.size();j++){
				if(canMerge(kItem.get(i),kItem.get(j))){
					SequenceList itemSet=new SequenceList();
					itemSet.addAll(kItem.get(i));
					itemSet.add(kItem.get(j).getLast());
					increasedItem.add(itemSet);
				}
			}
		}
		return increasedItem;
	}
	
	public boolean canMerge(SequenceList itemSet1,SequenceList itemSet2){
		if(itemSet1.size()==1){
			return true;
		}else{
			for(int i=0;i<itemSet1.size()-1;i++){   //检查前k-1项是否相同
				if(!itemSet1.get(i).equals(itemSet2.get(i))){
					return false;
				}
			}
		}
		return true;
	}
	
	
	public boolean containItem(SequenceList kItem,SequenceList affair){
		for(int i=0;i<kItem.size();i++){
			if(!affair.contain(kItem.get(i))){
				return false;
			}
		}
		return true;
	}
	
	public void readData(String path) throws Exception{
		File file= new File(path);
		FileReader read=new FileReader(file);
		BufferedReader br=new BufferedReader(read);
		String line="";
		int count=0;
		line=br.readLine();
		while(line!=null&&count<10000){
			String[] dataPoint=line.split(" ");
			SequenceList affairs=new SequenceList();
			for(int i=0;i<dataPoint.length;i++){
				affairs.add(Integer.valueOf(dataPoint[i]));
			}
			data.add(affairs);			
			System.out.println(line);
			line=br.readLine();	
			count++;
		}
		br.close();
	}
	
	public void printRule(){
		for(int i=0;i<this.ruleList.size();i++){
			AssociationRule rule=ruleList.get(i);
			System.out.print("front:");
			SequenceList front=rule.getFront();
			SequenceList back=rule.getBack();
			for(int j=0;j<front.size();j++){
				System.out.print(front.get(j)+" ");
			}
			System.out.print("back:");
			for(int j=0;j<back.size();j++){
				System.out.print(back.get(j)+" ");
			}
			System.out.println(" ");
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		AssociationAnalysis ass=new AssociationAnalysis();
		ass.setMinSupport(0.3);
		ass.setMinConfidence(0.8);
		//ass.readData("data\\association");
		ass.readData("D:\\迅雷下载\\关联规则\\关联规则\\T10I4D100K.txt");
		ass.getFrequent();
		ass.frequentRule();
        ass.printRule();
	}

}
