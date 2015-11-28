package classify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import tree.BTree;
import tree.TreeNode;
import modeling.Modeling;

public class NaiveBayesian extends Modeling{
    //Condition String[]   0->att, 1->val 
	HashMap<String,Double> p_label=new HashMap<String,Double>();
	
	public LinkedList<HashMap<String,int[]>> cond_count=new LinkedList<HashMap<String,int[]>>();
	//用来保存每个条件对应各个类的个数
	
	public LinkedList<HashMap<String,double[]>> cond_p=new LinkedList<HashMap<String,double[]>>();
	//用来保存每个条件对应各个类的概率
	
	public BTree model=new BTree();   //保存最终模型  
	
	public void predict(){
		int right=0;
		countCondition();
		buildModel();
		for(int i=0;i<test.size();i++){
			TreeNode p=model.root;			
			for(int j=0;j<attribute.size();j++){
				TreeNode[] child=p.child_array;
				if(!attribute.get(j).equals(label_s)){
					 String val=test.get(i).get(j);
					 for(int k=0;k<child.length;k++){
						 if(val.equals(child[k].value)){
							 p=child[k];
							 break;
						 }
					 }
				}
				if(j==attribute.size()-1){  //最后一层
					String pred=p.child_array[0].value;
					System.out.print(" label:"+test.get(i).get(label));
					System.out.println(" pre:"+pred);
					if(pred.equals(test.get(i).get(label))){
						right++;
					}
				}
			}			
		}
		System.out.println(" right:"+right+" rate:"+(double) right/test.size());
	}
	
	
	
	
	//生成预测模型
	public void buildModel(){
		countLabel();
		TreeNode p;
		Stack<TreeNode> stack=new Stack<TreeNode>();
		stack.push(model.root);
		for(int i=0;i<attribute.size();i++){
			if(!attribute.get(i).equals(label_s)){
				Stack<TreeNode> stack2=new Stack<TreeNode>();
				HashMap<String,double[]> map=cond_p.get(i);
				while(!stack.isEmpty()){
					p=stack.pop();					
					String att=attribute.get(i);
					String[] val=att_val.get(att);
					TreeNode[] child=new TreeNode[val.length];
					for(int j=0;j<val.length;j++){
						TreeNode node=new TreeNode(att,val[j]);
						child[j]=node;
						stack2.push(child[j]);
						if(p.pro==null){
						   node.pro=map.get(val[i]);
						}else{
						   double[] pare=p.pro;
						   double[] curr=map.get(val[j]).clone(); //需要重新克隆一个
						   for(int k=0;k<pare.length;k++){
							   curr[k]=curr[k]*pare[k];
						   }
						   node.pro=curr;
						}						
					}
					p.setChild(child);;
				}
				stack=stack2;				
			}
			if(i==attribute.size()-1){   //在最后一层的时候
				while(!stack.isEmpty()){
					String max_lab="";
					double max_pro=-1.0;					
					p=stack.pop();
					String[] lab_val=att_val.get(label_s);
					double[] probability=p.pro;
					for(int ii=0;ii<lab_val.length;ii++){
						double curr_p=probability[ii]*p_label.get(lab_val[ii]);
						if(curr_p>max_pro){
							max_pro=curr_p;
							max_lab=lab_val[ii];
						}
					}
					TreeNode[] child=new TreeNode[1];
					child[0]=new TreeNode("predict",max_lab);
					p.setChild(child);
				}
			}
		}
	}
	
	public void countCondition(){
		String[] lab_temp=att_val.get(label_s);
		
		
		//初始化,除去类标		
		for(int i=0;i<attribute.size()-1;i++){
			String[] val=att_val.get(attribute.get(i));
			HashMap<String,int[]> val_lab=new  HashMap<String,int[]>();
			for(int j=0;j<val.length;j++){
				int[] c_lab=new int[att_val.get(label_s).length];
				Arrays.fill(c_lab,0);
				val_lab.put(val[j],c_lab);
			}
			cond_count.add(val_lab);	
		}
		
		//计算各条件之下各类出现的次数
		for(int i=0;i<train.size();i++){
			for(int j=0;j<attribute.size()-1;j++){
				String val=train.get(i).get(j);
				HashMap<String,int[]> val_lab=cond_count.get(j);
				String lab=train.get(i).get(label);
				int[] count=val_lab.get(val);
				for(int k=0;k<lab_temp.length;k++){
					if(lab.equals(lab_temp[k])){
						int c=count[k]+1;
						count[k]=c;
					}
				}
				val_lab.put(val,count);				
			}
		}
		
		//计算各条件之下各类的后验概率
		for(int i=0;i<cond_count.size();i++){
			String att=attribute.get(i);
			String[] val=att_val.get(att);
			HashMap<String,double[]> val_lab_p=new  HashMap<String,double[]>();
			for(int j=0;j<val.length;j++){							
				int[] c=cond_count.get(i).get(val[j]);
				double[] pp=new double[c.length];
				int sum=0;
				for(int k=0;k<c.length;k++){
					sum=sum+c[k];
				}
				
				for(int k=0;k<c.length;k++){
					double p=(double)c[k]/sum;
					pp[k]=p;
				}
				val_lab_p.put(val[j],pp);								
			}
		    cond_p.add(val_lab_p);
	   }	
	}
	
	private void countLabel(){
		String[] lab=this.att_val.get(label_s);
        HashMap<String,Integer> countLabel=new HashMap<String,Integer>();
        
        //初始化
        for(int i=0;i<lab.length;i++){
        	countLabel.put(lab[i],0);
        }
        
		for(int i=0;i<train.size();i++){
	          String val=train.get(i).get(label);
	          int c=countLabel.get(val)+1;
	          countLabel.put(val,c);			
		}
		
		//计算后验概率
		for(int i=0;i<lab.length;i++){
        	double d=(double) countLabel.get(lab[i])/train.size();
        	p_label.put(lab[i],d);
        }
	}
}
