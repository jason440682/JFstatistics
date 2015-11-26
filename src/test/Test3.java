package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import classify.NaiveBayesian;


public abstract class Test3 {

	public static void main(String[] args) throws Exception {
		NaiveBayesian nb=new NaiveBayesian();
		nb.readTrain("data/TrainingData.arff");
		nb.setLabel("BikeBuyer");    
		nb.readTest("data/Test.arff");
		nb.countCondition();
		LinkedList<HashMap<String,int[]>> t=nb.cond_count;
		LinkedList<String> attribute=nb.attribute;
		HashMap<String,String[]> att_val=nb.att_val;
		
		String[] lab=att_val.get("BikeBuyer");
		for(int i=0;i<t.size();i++){
				String att=attribute.get(i);
				String[] val=att_val.get(att);
				for(int k=0;k<val.length;k++){
					int[] c=t.get(i).get(val[k]);
					for(int j=0;j<c.length;j++){
						String label=lab[j];
						System.out.println("att:"+att+" val:"+val[k]+" label:"+label+" count"+c[j]);		
					}
													
				}
			
		}
		
		LinkedList<HashMap<String,double[]>> p=nb.cond_p;
		for(int i=0;i<p.size();i++){
			String att=attribute.get(i);
			String[] val=att_val.get(att);
			for(int k=0;k<val.length;k++){
				double[] c=p.get(i).get(val[k]);
				for(int j=0;j<c.length;j++){
					String label=lab[j];
					System.out.println("att:"+att+" val:"+val[k]+" label:"+label+" count"+c[j]);		
				}
												
			}
		
	}
	}

}
