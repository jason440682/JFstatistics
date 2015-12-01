package association;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import utility.SequenceList;

public class AssociationAnalysis {
    //输入项以自然数1，2，3，4，……表示
	private double minSupport=0.0;  //支持度
	private LinkedList<SequenceList>  data=new LinkedList<SequenceList>();
	private HashMap<SequenceList,Integer> supportCount=new HashMap<SequenceList,Integer>(); 
	public void setSupport(double support){
		this.minSupport=support;
	}
	
	public void getFrequent(){  //得到所有的频繁项集
		LinkedList<SequenceList> oneItem=getItem();
		HashMap<Integer,LinkedList<SequenceList>> kItemMap=new HashMap<Integer,LinkedList<SequenceList>>();
		kItemMap.put(1,oneItem);
		
		LinkedList<SequenceList> index=oneItem;
		int k=1;
		while(index.size()!=0){
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
			kItemMap.put(k,cut);
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
 				if(!item.contain(affair.get(j))){
 					item.add(affair.get(j));
 				}
 			}
 		}
		
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
	
	
	public boolean containItem(SequenceList item,SequenceList affair){
		for(int i=0;i<item.size();i++){
			if(!affair.contain(item.get(i))){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
