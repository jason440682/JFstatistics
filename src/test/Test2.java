package test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import utility.SequenceList;

public class Test2 {

	public static void main(String[] args) {
		SequenceList list=new SequenceList();
		list.add(4);
		list.add(0);
		list.add(3);
		list.add(2);
		list.add(1);
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}
	}

}
