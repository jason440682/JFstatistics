package tree;

public class TreeNode {
    public TreeNode[] child_array;  
    public String attribute;   
    public String value;
    
   public void setAttribute(String attribute){
	   this.attribute=attribute;
   }
   
   public void setValue(String value){
	   this.value=value;
   }
}
