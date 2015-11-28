package test;

import classify.NaiveBayesian;


public abstract class Test3 {

	public static void main(String[] args) throws Exception {
		NaiveBayesian nb=new NaiveBayesian();
		nb.readTrain("data/TrainingData.arff");
		nb.setLabel("BikeBuyer");    
		nb.readTest("data/Test.arff");
		nb.predict();
/*		
		LinkedList<HashMap<String,int[]>> l=nb.cond_count;
		for(int i=0;i<l.size();i++){
			HashMap<String,int[]> map=l.get(i);
			String att=nb.attribute.get(i);
			String[] val=nb.att_val.get(att);
			for(int j=0;j<val.length;j++){
				int[] count=map.get(val[j]);
				System.out.println(" att:"+att+" val:"+val[j]+" count1:"+count[0]+" count2:"+count[1]);
			}
		}
		
		LinkedList<HashMap<String,double[]>> d=nb.cond_p;
		for(int i=0;i<d.size();i++){
			HashMap<String,double[]> map=d.get(i);
			String att=nb.attribute.get(i);
			String[] val=nb.att_val.get(att);
			for(int j=0;j<val.length;j++){
				double[] p=map.get(val[j]);
				System.out.println(" att:"+att+" val:"+val[j]+" p1:"+p[0]+" p2:"+p[1]);
			}
		}
		
*/		
	}

}
