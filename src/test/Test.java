package test;

import java.util.ArrayList;
import java.util.List;

import classify.ID3;
import classify.Rule;

public class Test {

	public static void main(String[] args) throws Exception {
		ID3 id3=new ID3();
		id3.readTrain("data/TrainingData.arff");
		id3.setLabel("BikeBuyer");    
		id3.readTest("data/Test.arff");
		id3.predict();
		

/*		
		Rule rule=new Rule();
		rule.readTrain("data/TrainingData.arff");
		rule.setLabel("BikeBuyer");    
		rule.readTest("data/Test.arff");
		rule.predict();
		
		List<ArrayList<String[]>> r=rule.rule;
		for(int i=0;i<r.size();i++){
			for(int j=0;j<r.get(i).size();j++){
				System.out.print(" att:"+r.get(i).get(j)[0]+" val:"+r.get(i).get(j)[1]);
			}
			System.out.println("");
		}
	*/	
	}

}
