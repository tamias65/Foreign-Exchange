/*Tree.java
 * 
 * Alvin Chou
 * 9/29/2015
 * 
 * Tree class for the decision tree.
 */
package decision_tree;

//import java.util.ArrayList;

public class Tree {
	public Node root;
	
	public Tree(Node root){
		this.root = root;
	}
	
	//Build tree
	public void buildTree(Node node){
		node.setDividingFeature();
		Table leftTable = new Table();
		Table rightTable = new Table();
		int featureIndx = node.featureIndx.intValue();
		double featureValue = node.featureLessThanEqualsTo;
		node.instances.splitOnFeature(leftTable, rightTable, featureIndx, featureValue);
		
		if(leftTable.dataTable.isEmpty() || rightTable.dataTable.isEmpty()){
			int label = node.getMajorityLabel();
			node.setFinalLabel(label);
		}else{
			Node left = new Node();
			Node right = new Node();
			left.setTable(leftTable);
			right.setTable(rightTable);
			node.setChildren(left, right);
			//System.out.println("Left: "+leftTable.size()+". Right: "+rightTable.size());
			//System.out.println("Extending to the left");
			this.buildTree(node.left);
			//System.out.println("Extending to the right");
			this.buildTree(node.right);
		}
	}
}
