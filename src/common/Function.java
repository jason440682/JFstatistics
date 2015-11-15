package common;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Function {
    //统计学上常用函数
	
    //整形最大值
	public static int getMax(int[] data){
		int max=Integer.MIN_VALUE;
        for(int i=0;i<data.length;i++){
        	if(data[i]>max){
        		max=data[i];
        	}
        }
        return max;
	}
	
	//双精度
	public static double getMax(double[] data){
		double max=Double.MIN_VALUE;
        for(int i=0;i<data.length;i++){
        	if(data[i]>max){
        		max=data[i];
        	}
        }
        return max;
	}
	
	public static int getMin(int[] data){  //	求最少值
		int min=Integer.MAX_VALUE;
		for(int i=0;i<data.length;i++){
			if(data[i]<min){
				min=data[i];
			}
		}
		return min;
	}
	
	public static double getMin(double[] data){
		double min=Integer.MAX_VALUE;
		for(int i=0;i<data.length;i++){
			if(data[i]<min){
				min=data[i];
			}
		}
		return min;
	}
	
	
	
	public static int getSum(int[] data){
		int sum=0;
		for(int i=0;i<data.length;i++){
			sum=sum+data[i];
		}
		return sum;
	}
	
	public static double getAverage(int[] data){
		int len=data.length;
		return Function.getSum(data)/len;
	}
	
	public static int getMode(int[] data){  //求众数
		int mode=data[0];
		int count=1;
		int maxCount=1;
		Arrays.sort(data);
		for(int i=1;i<data.length;i++){
			if(data[i]==data[i-1]){
				count++;
				if(count>maxCount){
					mode=data[i-1];
					maxCount=count;
				}
			}else{
			    count=1;
			}
		}
		return mode;
	}
	
	public static double getMedian(int[] data){   //求中位数
		int mid;
		int len=data.length;
		Arrays.sort(data);
		if(len%2==0){
			mid=(data[len/2-1]+data[len/2])/2;
		}else{
			mid=data[len/2];
		}
		return mid;
	}
	
	public static int getCount(int[] data,int aim){  //得到目标的个数
	   int count=0;
	   for(int i=0;i<data.length;i++){
		   if(aim==data[i]){
			   count++;
		   }
	   }
	   return count;
	}
	
	public static double getSd(int[] data){   //求多个变量的标准差
		return Math.sqrt(getVariance(data));		
	}
	
	public static double getVariance(int[] data){
		double variance=0;
		double mean=getAverage(data);
		for(int i=0;i<data.length;i++){
			variance+=(data[i]-mean)*(data[i]-mean);
		}
		return variance;
	}
	
	public static void main(String[] args) {
	    int[] data={9,6,1,6,1,4,9,9,6,8,9,9};
	    System.out.println(Function.getMode(data));
	    
	}

}
