package classify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import modeling.Modeling;


public class Rule extends Modeling{
	private List<ArrayList<String[]>> rule=new LinkedList<ArrayList<String[]>>();
	//规则的每一项由String[]表示    String[0]为属性，String[1]为取值
	
	private void buildRule(){
		ArrayList<Integer> all=new ArrayList<Integer>();   //元组索引
		for(int i=0;i<this.train.size();i++){
			all.add(i);
		}
		String[] lab=sortLabel();
		for(int i=0;i<lab.length-2;i++){   //频率最高的类做默认类
			while(){
				ArrayList<String[]> r=learnRule(lab[i],all);
	            delCover(all,r);
	            rule.add(r);
			}
		}
		
	}
	
	private void delCover(ArrayList<Integer> sub,ArrayList<String[]> r){
		for(int i=0;i<sub.size();i++){
			boolean match=true;
			for(int j=0;j<r.size();j++){
				String att=r.get(j)[0];
				String val=r.get(j)[1];
				int ind=attribute.indexOf(att);
				if(!this.train.get(sub.get(i)).get(ind).equals(val)){
					//不相等
					match=false;
				}
			}
			if(match){
				sub.remove(i);
				i--;
			}
		}
	}
	
	
	private String[] sortLabel(){
		HashMap<String,Integer> countMap=new HashMap<String,Integer>();
		int labelNum=att_val.get(label_s).length;
        for(int i=0;i<labelNum;i++){
		     countMap.put(att_val.get(label_s)[i],0);	
		}
        for(int i=0;i<train.size();i++){
        	String val=train.get(i).get(label);
        	int count=countMap.get(val);
        	count++;
        	countMap.put(val,count);
        }
        for(int i=0;i<labelNum;i++){
		    int min=Integer.MAX_VALUE;
        	for(int j=i;j<labelNum;j++){
		    	String val=att_val.get(label_s)[j];
        		if(countMap.get(val)<min){
        		   min=countMap.get(val);
        		   String temp=att_val.get(label_s)[i];
        		   att_val.get(label_s)[i]=val;
        		   att_val.get(label_s)[j]=temp;
        		}
		    }
		}
        return att_val.get(label_s);
	}
	
	private ArrayList<String[]> learnRule(String pos,ArrayList<Integer> sub){   //传入类，则正例
		ArrayList<String[]> r=new ArrayList<String[]>();
		int p1=0; //增加规则前正类个数
		for(int i=0;i<this.train.size();i++){
			if(train.get(i).get(label).equals(pos)){
				p1++;
			}
		}
		//加入一个新的合取项。顺序遍历属性-值
		while(什么时候停止合并规则,覆盖负类？){
			double max_foil=0.0;
			String[] max_r;
			for(int i=0;i<att_val.size();i++){
				String att=this.attribute.get(i);
				for(int j=0;j<att_val.get(att).length;j++){
					String val=att_val.get(att)[j];
					String[] r_add={att,val};
					double foil=FOIL(p1,sub,r_add,pos);
					if(foil>max_foil){
						max_foil=foil;;
						max_r=r_add;
					}
				}
			}
			r.add(max_r);
		}
		
		return r;
	}
	
	
	private boolean pruning(){  //检查是否需要剪枝。
		
		return false;
	}
	
	//pos为正例  
	private double FOIL(int posNum,ArrayList<Integer> sub,String[] r_add,String pos){
		double FOIL_gain=0.0;    //FOIL信息增益
		int ind=this.attribute.indexOf(r_add[0]);
		int p=0; //正例个数
		int n=0; //负例个数
		for(int i=0;i<sub.size();i++){
			if(this.train.get(sub.get(i)).get(ind).equals(r_add[1])){
			     if(this.train.get(sub.get(i)).get(label).equals(pos)){
			    	 p++;
			     }else{
			    	 n++;
			     }
			}			
		}
		FOIL_gain=posNum*(Math.log(posNum/(sub.size()))/Math.log(2)-Math.log(p/(p+n))/Math.log(2));
		return FOIL_gain;
	}
}
