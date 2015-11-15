package tree;

public class BTree {
     public TreeNode root;
     public BTree(TreeNode root){
    	 this.root=root;
    	 
     }
     public BTree(){
    	 this.root=new TreeNode(); 
    	 this.root.attribute="<root>";
    	 this.root.value="none";
     }
}
