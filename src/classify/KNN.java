package classify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import modeling.Modeling;

//最邻近分类器
public class KNN extends Modeling {
    public int K=0;   //参数K；
    public void setK(int k){
    	this.K=k;
    }
    
    public void predict(){
    	int right=0;
    	int count=0;
    	for(int i=0;i<test.size();i++){
    		LinkedList<Double> sim_list=new LinkedList<Double>();
    		LinkedList<Integer> index_list=new LinkedList<Integer>();   		
    		for(int j=0;j<train.size();j++){
    			ArrayList<String> x1= train.get(j);
    			ArrayList<String> x2= test.get(i);
    			double simlarity=similarity(x1,x2);
    			//System.out.println("sim:"+simlarity);				
    			if(sim_list.size()==0){
    				sim_list.add(simlarity);
    				index_list.add(j);
    			}else{
    				for(int l=0;l<sim_list.size();l++){
    					if(simlarity>=sim_list.get(l)){
    						sim_list.add(l,simlarity);
    						index_list.add(l,j);
    	    				break;
    					}else if(l==sim_list.size()-1){
    						sim_list.add(simlarity);
    	    				index_list.add(j);
    					}
    				}
    			}   			
    		}
    		String pre=mostLabel(index_list);
    		if(pre.equals(test.get(i).get(label))){
    			right++;
    		}
    		System.out.println(" label:"+test.get(i).get(label)+" pre"+pre+" count"+count++); 
    	}
    	System.out.println("right:"+right+" rate:"+(double)right/test.size());    	
    }
    private String mostLabel(LinkedList<Integer> sub){
		String most="";
		HashMap<String,Integer> label_map=new HashMap<String,Integer>();
		String[] label_value=att_val.get(label_s);
		for(int i=0;i<label_value.length;i++){
			label_map.put(label_value[i],0);
		}
		
		//取最近的K个
		for(int j=0;j<this.K;j++){
			String cur_val=this.train.get(sub.get(j)).get(this.label);
			int count=label_map.get(cur_val)+1;
			label_map.put(cur_val,count);
		}
		
		int findmost=0;
		for(int i=0;i<label_value.length;i++){
			int temp=label_map.get(label_value[i]);
			if(findmost<temp){
				findmost=temp;
				most=label_value[i];
			}
		}
		
		
		return most;
	}
	public double similarity(ArrayList<String> x1,ArrayList<String> x2){
		//SMC简单匹配系数，使用前记得先去掉类标
		int match=0;
		for(int i=0;i<x1.size()-1;i++){   //方便起见，这里长度减一，不遍历类标
			if(x1.get(i).equals(x2.get(i))){
				match++;
				
			}
		}
		return (double)match/(x1.size()-1);
	}
	
	
}
