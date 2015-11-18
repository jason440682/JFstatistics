package test;

import classify.ID3;

public class Test {

	public static void main(String[] args) throws Exception {
		ID3 id3=new ID3();
		id3.readTrain("data/TrainingData.arff");
		id3.setLabel("BikeBuyer");    
		id3.readTest("data/Test.arff");
		id3.predict();
		
	}

}
