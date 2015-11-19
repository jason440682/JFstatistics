package classify;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import modeling.Modeling;


public class Rule extends Modeling{
	private List<ArrayList<String[]>> rule=new LinkedList<ArrayList<String[]>>();
	//规则的每一项由String[]表示    String[0]为属性，String[1]为取值
	
	private void buildRule(){
		
	}
	
	private ArrayList<String[]> learnRule(String pos){   //传入类，则正例
		ArrayList<String[]> r=new ArrayList<String[]>();
		int p1=0; //增加规则前正类个数
		for(int i=0;i<this.train.size();i++){
			if(train.get(i).get(label).equals(pos)){
				p1++;
			}
		}
		//加入一个新的合取项。顺序遍历属性-值
		for(int i=0;i<att_val.size();i++){
			String att=this.attribute.get(i);
			for(int j=0;j<att_val.get(att).length;j++){
				String val=att_val.get(att)[j];
				String[] r_all={att,val};
				double foil=FOIL(p1,)
			}
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
