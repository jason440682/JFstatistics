package model;

public class TreeNode {
    public TreeNode[] child_array;  
    public String attribute;   
    public String value;
    public double[] pro;    //概率，主要在贝叶斯概率模型时使用
    public TreeNode(String att,String val){
	   this.attribute=att;
	   this.value=val;
    }
    public TreeNode(){
	   
    }
    public void setAttribute(String attribute){
	   this.attribute=attribute;
    }
   
    public void setValue(String value){
	   this.value=value;
    }
   
    public void setChild(TreeNode[] child){
	   this.child_array=child;
    }
}
