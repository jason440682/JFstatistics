package test;

import java.util.LinkedList;

public class Test2 {

	public static void main(String[] args) {
		LinkedList<Integer> l=new LinkedList<Integer>();
		l.add(1);
		l.add(2);
		l.add(2);
		l.add(4);
        for(int i=0;i<l.size();i++){
        	if(l.get(i)==2){
        		l.remove(i);
        		i--;
        	}
        }	
        
        for(int i=0;i<l.size();i++){
        	
        	System.out.println(l.get(i)+" size:"+l.size()+" i:"+i);
        }
	}

}
