package classify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import modeling.Modeling;


public class Rule extends Modeling{
	public List<ArrayList<String[]>> rule=new LinkedList<ArrayList<String[]>>();
	//规则的每一项由String[]表示    String[0]为属性，String[1]为取值
	
	public void predict(){
		buildRule();
	}
	
	private void buildRule(){
		ArrayList<Integer> all=new ArrayList<Integer>();   //元组索引
		for(int i=0;i<this.train.size();i++){
			all.add(i);
		}
		String[] lab=sortLabel();
		for(int i=0;i<lab.length-1;i++){   //频率最高的类做默认类
			while(havePos(all,lab[i])){    //有正类的时候
				ArrayList<String[]> r=learnRule(lab[i],all);
	            delCover(all,r);
	            rule.add(r);
			}
		}
		
	}
	
	private boolean havePos(ArrayList<Integer> sub,String pos){
		for(int i=0;i<sub.size();i++){
			int ind=sub.get(i);
			if(train.get(ind).get(label).equals(pos)){
				return true;
			}
		}
		return false;
	}
	
	
	
	private void delCover(ArrayList<Integer> sub,ArrayList<String[]> r){
		for(int i=0;i<sub.size();i++){			
			if(matchRule(r,train.get(sub.get(i)))){
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
		for(int i=0;i<sub.size();i++){
			if(train.get(sub.get(i)).get(label).equals(pos)){
				p1++;
			}
		}
		
		int p0=0;   //总正类个数
		for(int i=0;i<this.train.size();i++){
			if(train.get(i).get(label).equals(pos)){
				p0++;
			}
		}
		
		
		//加入一个新的合取项。顺序遍历属性-值
		while(descend(r,pos,p0)){    //规则质量下降，则停止增长
			double max_foil=0.0;
			String[] max_r=new String[2];
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
		//r.remove(r.size()-1);
		
		return r;
	}
	private boolean matchRule(ArrayList<String[]> r,ArrayList<String> ele){
		for(int j=0;j<r.size();j++){
			String att=r.get(j)[0];
			String val=r.get(j)[1];
			int ind=attribute.indexOf(att);
			if(!ele.get(ind).equals(val)){
				//不相等
				return false;
			}
		}
		return true;
	}
	
	private boolean descend(ArrayList<String[]> r,String pos,int p0){
		if(r.size()>1){
			double f1;
			int p1=0;
			int n1=0;
			
			double f2;
			int p2=0;
			int n2=0;
			
			for(int i=0;i<train.size();i++){
				
				if(matchRule(r,train.get(i))){
					if(train.get(i).get(label).equals(pos)){
						p1++;
					}else{
						n1++;
					}
				}
				
				
				ArrayList<String[]> r2=(ArrayList<String[]>) r.clone();
				r2.remove(r2.size()-1);
				if(matchRule(r2,train.get(i))){
					if(train.get(i).get(label).equals(pos)){
						p2++;
					}else{
						n2++;
					}
				}
			}			
			
			f1=p1*(Math.log(p1)/Math.log(p1+n1)-Math.log(p0)/Math.log(train.size()));
			f2=p2*(Math.log(p2)/Math.log(p2+n2)-Math.log(p0)/Math.log(train.size()));
			if(f1>f2){
				return false;
			}else{
				return true;
			}
			
			
		}else{
			return false;
		}
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
		FOIL_gain=p*(Math.log(p/(p+n))/Math.log(2)-Math.log(posNum/sub.size())/Math.log(2));
		return FOIL_gain;
	}
}
