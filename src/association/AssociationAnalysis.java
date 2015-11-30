package association;

import java.util.LinkedList;

import utility.SequenceList;

public class AssociationAnalysis {
    //输入项以自然数1，2，3，4，……表示
	private double support=0.0;  //支持度
	private LinkedList<SequenceList>  data=new LinkedList<SequenceList>();
	
	public void setSupport(double support){
		this.support=support;
	}
	
	public  SequenceList  getItem(){   //得到一项集
		SequenceList item=new SequenceList();
		for(int i=0;i<data.size();i++){
 			SequenceList affair=data.get(i);
 			for(int j=0;j<affair.size();j++){
 				if(!item.contain(affair.get(j))){
 					item.add(affair.get(j));
 				}
 			}
 		}
		return item;
	}
	
	public LinkedList<SequenceList> apriori_gen(LinkedList<SequenceList> kItem,int k){
		//传入的是k项集，生成k+1项集
		
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
