package classify;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import read.ReadData;
import tree.BTree;
import tree.TreeNode;

public class ID3 {
    //定义：生成多叉树
	private List<String> attribute=new LinkedList<String>();  //属性链表
	private HashMap<String,String[]> att_val=new HashMap<String,String[]>(); //各属性的取值
	private List<ArrayList<String>> data=new LinkedList<ArrayList<String>>();  //训练集元祖数据
	private List<ArrayList<String>> test=new LinkedList<ArrayList<String>>();
	private int label;
	private String label_s;
	
	public BTree tree=new BTree();
	
	//读取测试集
	public List<ArrayList<String>> readTest(String path) throws Exception{
		ReadData rdata=new ReadData();
		rdata.readArff(path);
		this.test=rdata.getData();
		return this.test;
	}
	
	//读取测试集
	public void  readTrain(String path) throws IOException{   //数 入为数据文件路径
		ReadData rdata=new ReadData();
		rdata.readArff(path);
		this.attribute=rdata.getArrtibute();
		this.att_val=rdata.getAttVal();
		this.data=rdata.getData();
	}
	
	public void setLabel(int i){
		this.label=i;
	}
	public void setLabel(String label_s){
		this.label_s=label_s;
		for(int i=0;i<this.attribute.size();i++){
			if(label_s.equals(attribute.get(i))){
				this.label=i;
				break;
			}
		}
	}
	
	
	
	private double getEntropy(ArrayList<Integer> sub){   //输入的是子集数组的索引
		double entropy=0.0;
		if(sub.size()==0) return entropy;
		String[] label_v=this.att_val.get(this.label_s);
		int len=label_v.length;
		int[] count=new int[len];
		Arrays.fill(count,0);  //初始化为零
		
		for(int i=0;i<sub.size();i++){
			for(int j=0;j<len;j++){
				if(this.data.get(sub.get(i)).get(label).equals(label_v[j])){   //取label
					count[j]++;
				}
			}
		}
		for(int k=0;k<len;k++){
			double p=(double) count[k]/sub.size();    //在进行整形运算后，需要强制转化为double类型
			if(p!=0.0){			
				entropy-=p*Math.log(p)/Math.log(2);
			}		
		}
		return entropy;
				
	}
	
	public void buildDT(TreeNode node,ArrayList<Integer> sub,List<Integer> att_ind){   //构造决策树
		if(sameLabel(sub)){
			TreeNode[] c_a=new TreeNode[1];
			TreeNode left=new TreeNode();
			left.attribute="label";
			left.value=this.data.get(0).get(this.label);;
			c_a[0]=left;
			node.child_array=c_a;	
			return;   //如果类标相同，则返回
		}
		if(att_ind.size()==1){
			TreeNode[] c_a=new TreeNode[1];
			TreeNode left=new TreeNode();
			left.attribute="label";
			left.value=mostLabel(sub);
			c_a[0]=left;
			node.child_array=c_a;	
			return;  //划分属性用完
		}
		double minEntropy=1.0; //最小熵
		int min_index=0;   //最少熵对应的属性索引
		HashMap<String,ArrayList<Integer>> min_sub=new HashMap<String,ArrayList<Integer>>();
		//熵最小的划分方式，属性取值对应子数组。
		
		for(int i=0;i<att_ind.size();i++){     //把属性循环一遍，找出最小熵对应的属性
			if(att_ind.get(i)==this.label) continue;
			int ind=att_ind.get(i);
			String atr=this.attribute.get(ind);
			String[] values=att_val.get(atr);    //根据属性取得该属性的所以取值
					                                   
			HashMap<String,Integer> count_map=new HashMap<String,Integer>(); 
			//保存取值出现次数
			
			HashMap<String,ArrayList<Integer>> sub_map=new HashMap<String,ArrayList<Integer>>();
			//按属性取值划分的子数组
			
			for(int j=0;j<values.length;j++){
				
				count_map.put(values[j],0);
				sub_map.put(values[j],new ArrayList<Integer>());
			}
			
			for(int k=0;k<sub.size();k++){
				ind=att_ind.get(i);
				String c_value=this.data.get(sub.get(k)).get(ind);  //当前属性值,ind为对于属性索引
				int count=count_map.get(c_value)+1;
				count_map.put(c_value, count);
				
				ArrayList<Integer> arr=sub_map.get(c_value); //把对应子集取出来，再加进去
				arr.add(sub.get(k));
				sub_map.put(c_value, arr);
				
			}
			//计算熵
			double sum_entropy=0.0;
			for(int kk=0;kk<values.length;kk++){
				String c_value=values[kk];
				int count=count_map.get(c_value);
				ArrayList<Integer> s_sub=sub_map.get(c_value);
				sum_entropy+=(double) count/sub.size()*getEntropy(s_sub);
				
			}
			//System.out.println("en:"+sum_entropy);
			if(sum_entropy<=minEntropy){
				minEntropy=sum_entropy;
				min_index=i;
				min_sub=sub_map;
				
			}						 											
		}
		//这里得到了最佳划分属性对应的索引min_index
		
		TreeNode[] child=new TreeNode[min_sub.size()];
		for(int ii=0;ii<min_sub.size();ii++){
			child[ii]=new TreeNode();
			
			int ind=att_ind.get(min_index);//从属性索引里面取得索引
			
			String a=this.attribute.get(ind);
			child[ii].attribute=a;
			String v=att_val.get(a)[ii];   //属性取值										
			child[ii].setValue(v);
			ArrayList<Integer> s=min_sub.get(v);
			
			LinkedList<Integer> b=new LinkedList<Integer>();    //删除属性
			b.addAll(att_ind);
			b.remove(min_index);
			buildDT(child[ii],s,b);
		}
		node.child_array=child;
		
		
		
	}
	
	public List<ArrayList<String>> predict(){
		ArrayList<Integer> all=new ArrayList<Integer>();   //元组索引
		for(int i=0;i<this.data.size();i++){
			all.add(i);
		}
		
		LinkedList<Integer> att_index=new LinkedList<Integer>();  //属性集索引
		for(int j=0;j<this.attribute.size();j++){
			att_index.add(j);
		}
		
		buildDT(this.tree.root,all,att_index);
		//printDT(this.tree.root,0);
		
		int right=0;
        
        
        List<String> pre=new LinkedList<String>();
		for(int i=0;i<test.size();i++){
			TreeNode p=this.tree.root;
			while(p.attribute!="label"){
				String c_att=p.child_array[0].attribute;
				if(c_att=="label"){
					p=p.child_array[0];
				}else{
					int ind=this.attribute.indexOf(c_att);
					String val=test.get(i).get(ind);		
					for(int j=0;j<p.child_array.length;j++){				
						if(val.equals(p.child_array[j].value)){
							p=p.child_array[j];
							break;
						}
					}
				}									
			}
			pre.add(p.value);
			test.get(i).add(p.value);
			if(p.value.equals(test.get(i).get(label))){
				right++;
			}
		}
		
		for(int j=0;j<pre.size();j++){
			System.out.println("real:"+test.get(j).get(8)+" pre:"+pre.get(j));
		}
		System.out.println("正确:"+right+" 总共:"+test.size());
		return this.test;
		
	}
	
	private void printDT(TreeNode node,int plies){
		
		for(int i=0;i<plies;i++){
			System.out.print("--");
		}
		System.out.print("attribute:"+node.attribute+"---value:"+node.value);
		System.out.println(" ");
		if(node.child_array!=null){
			TreeNode[] child=node.child_array;
			for(int j=0;j<child.length;j++){
				printDT(child[j],plies+1);
			}
		}
		
		
	}
	
	private boolean sameLabel(ArrayList<Integer> sub){  //检查该子集类标是否相同
		String check=this.data.get(0).get(this.label);
		for(int i=1;i<sub.size();i++){
			if(!check.equals(this.data.get(sub.get(i)).get(this.label))){
				return false;
			}
		}
		return true;
	}
	
	private String mostLabel(ArrayList<Integer> sub){
		String most="";
		HashMap<String,Integer> label_map=new HashMap<String,Integer>();
		String lab=this.attribute.get(this.label);
		String[] label_value=att_val.get(lab);
		for(int i=0;i<label_value.length;i++){
			label_map.put(label_value[i],0);
		}
		
		for(int j=0;j<sub.size();j++){
			String cur_val=this.data.get(sub.get(j)).get(this.label);
			int count=label_map.get(cur_val)+1;
			label_map.put(cur_val,count);
		}
		
		int findmost=0;
		for(int i=0;i<label_value.length;i++){
			int temp=label_map.get(label_value[i]);
			if(findmost<temp){
				findmost=temp;
				most=label_value[i];
			}
		}
		
		
		return most;
	}
	
	//使用已经存在的决策树模型
	public void readModel(){
		
	}
	
	
	

}
