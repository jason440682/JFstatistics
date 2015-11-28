package test;

import classify.KNN;


public class Test4 {

	public static void main(String[] args) throws Exception {
		KNN knn=new KNN();
		knn.readTrain("data/TrainingData.arff");
		knn.setLabel("BikeBuyer");    
		knn.readTest("data/Test.arff");
		knn.setK(10);
		knn.predict();
	}

}
