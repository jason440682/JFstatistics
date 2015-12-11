package model;


public class TreeModel {
	public TreeNode root;
    public TreeModel(TreeNode root){
   	 this.root=root;
   	 
    }
    public TreeModel(){
   	 this.root=new TreeNode(); 
   	 this.root.attribute="<root>";
   	 this.root.value="none";
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
