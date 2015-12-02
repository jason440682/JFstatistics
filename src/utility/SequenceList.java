package utility;

import java.util.Collection;
import java.util.LinkedList;


public class SequenceList {
    private LinkedList<Integer> list=new LinkedList<Integer>();
    public int size(){
    	return list.size();
    }
	public void add(Integer toAdd){
		int len=list.size();
		for(int i=0;i<len+1;i++){
			if(i==list.size()){
				list.add(toAdd);
			}else{
				if(toAdd<=list.get(i)){
					list.add(i,toAdd);
					break;
				}
			}
		}		
	}
	
	public void addAll(){
		
	}
	
	public void addAll_sort(){
		
	}
	
	public void sort(){
    	
    }
	public boolean contain(Integer t){
		if(list.contains(t)){
			return true;
		}else{
			return false;
		}
	}
	
	public Integer get(int i){
		return list.get(i);
	}
	
	public void addAll(SequenceList s){
		for(int i=0;i<s.size();i++){
			list.add(s.get(i));
		}
	}
	
	public Integer getLast(){
		return list.getLast();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
