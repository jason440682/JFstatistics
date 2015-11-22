package test;

import java.util.LinkedList;

public class Test2 {

	public static void main(String[] args) {
		LinkedList<Integer> l=new LinkedList<Integer>();
		l.add(1);
		l.add(2);
		l.add(2);
		l.add(4);
        
		LinkedList<Integer> b=(LinkedList<Integer>) l.clone();
		b.remove(1);
		for(int i=0;i<l.size();i++){
			System.out.println(l.get(i));
		}
		
		for(int i=0;i<b.size();i++){
			System.out.println(b.get(i));
		}
	}

}
