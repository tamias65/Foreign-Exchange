/*Tree.java
 * 
 * Alvin Chou
 * 9/29/2015
 * 
 * Tree class for the decision tree.
 */
package decision_tree;

import java.util.ArrayList;

public class Tree {
	public Node root;
	
	public Tree(Node root){
		this.root = root;
	}
	
	//Build tree
	public void buildTree(Node node){
		node.setDividingFeature();
		ArrayList<Record> leftTable = new ArrayList<Record>();
		ArrayList<Record> rightTable = new ArrayList<Record>();
		this.splitTable(node, leftTable, rightTable);
		
		if(leftTable.isEmpty() || rightTable.isEmpty()){
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
	
	//split a data table into left table and right table based on a given median value
	public void splitTable(Node node, ArrayList<Record> leftTable, ArrayList<Record> rightTable){
		int featureIndx = node.featureIndx.intValue();
		double featureValue = node.featureLessThanEqualsTo;
		for(int i = 0; i < node.dataTable.size(); i++){
			if(node.dataTable.get(i).features[featureIndx] <= featureValue){
				leftTable.add(node.dataTable.get(i));
			}else{
				rightTable.add(node.dataTable.get(i));
			}
		}
	}
		
}
