package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import tree.BTree;
import tree.TreeNode;
import classify.ID3;

public class Test {

	public static void main(String[] args) throws Exception {
		int right=0;
		
		ID3 id3=new ID3();
		id3.readArff("data/TrainingData.arff");
		id3.setLabel("BikeBuyer");    
		id3.doWork();
		BTree DTtree=id3.tree;
		
		String[] att={"MaritalStatus","Gender" ,"Children","Occupation","HomeOwner","Cars","CommuteDistance","Age","BikeBuyer"};
		
		List<String[]> test=id3.readTest("data/Test.arff");
		
		List<String> pre=new LinkedList<String>();
		
		
		HashMap<String,Integer> att_ind=new HashMap<String,Integer>();
		for(int i=0;i<att.length;i++){
			att_ind.put(att[i],i);
		}
		
		for(int i=0;i<test.size();i++){
			TreeNode p=DTtree.root;
			while(p.attribute!="label"){
				String c_att=p.child_array[0].attribute;
				if(c_att=="label"){
					p=p.child_array[0];
				}else{
					int ind=att_ind.get(c_att);
					String val=test.get(i)[ind];		
					for(int j=0;j<p.child_array.length;j++){				
						if(val.equals(p.child_array[j].value)){
							p=p.child_array[j];
							break;
						}
					}
				}									
			}
			pre.add(p.value);
			if(p.value.equals(test.get(i)[8])){
				right++;
			}
		}
		
		for(int j=0;j<pre.size();j++){
			System.out.println("real:"+test.get(j)[8]+" pre:"+pre.get(j));
		}
		System.out.println("正确:"+right+" 总共:"+test.size());
	}

}
