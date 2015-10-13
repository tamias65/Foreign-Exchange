/*Test.java
 * 
 * Alvin Chou
 * 9/29/2015
 * 
 * Class test used to run a test data on a given tree.
 */

package decision_tree;

import java.util.ArrayList;

//import java.util.ArrayList;

public class Test {
	public double accuracy;
	public int correct;
	public int incorrect;
	public ArrayList<int[]> trial;
	
	public Test(){
		this.accuracy = 0.0000;
		this.correct = 0;
		this.incorrect = 0;
		this.trial = new ArrayList<int[]>();
	}
	
	public void testForest(ArrayList<Tree> forest, Table testSet){
		for(int i = 0; i < forest.size(); i++){
			Test test = new Test();
			test.testTree(forest.get(i).root, testSet);
			this.trial.add(test.trial.get(0));
		}
		int[] predict = this.predictMajority();
		this.evalForest(predict, testSet);
	}
	
	public void testTree(Node root, Table testSet){
		int correct = 0;
		int incorrect = 0;
		int[] predict = new int[testSet.dataTable.size()];
		for(int i = 0; i < testSet.dataTable.size(); i++){
			predict[i] = predictRecord(root, testSet.dataTable.get(i));
			if(predict[i] == testSet.dataTable.get(i).label.intValue()){
				correct += 1;
			}else{
				incorrect += 1;
			}
		}
		
		double accuracy = (double) correct/(correct+incorrect)*100;
		this.accuracy = accuracy;
		this.correct = correct;
		this.incorrect = incorrect;
		this.trial.add(predict);
	}
	
	// Tree traversal and make a prediction for reach record
	private int predictRecord(Node node, Record record){
		int prediction = 0;
		if(node.finalLabel != null){
			prediction = node.finalLabel;
		}else{
			if(record.features[node.featureIndx] <= node.featureLessThanEqualsTo){
				if(node.left == null){
					prediction = predictRecord(node.right, record);
				}else{
					prediction = predictRecord(node.left, record);
				}
			}else if(record.features[node.featureIndx] > node.featureLessThanEqualsTo){
				if(node.right == null){
					prediction = predictRecord(node.left, record);
				}else{
					prediction = predictRecord(node.right, record);
				}
			}else{
				System.out.println("ERROR. Something wrong in traversing. Exiting...");
				System.exit(0);
			}
		}
		return prediction;
	}
	
	// Given a set of prediction of individual trees, find the majority decision for each instance
	private int[] predictMajority(){
		int[] prediction = new int[this.trial.get(0).length];
		for(int i = 0; i < this.trial.get(0).length; i++){
			prediction[i] = 0;
			for(int j = 0; j < this.trial.size(); j++){
				prediction[i] += this.trial.get(j)[i];
			}
			if(prediction[i] >= 0){
				prediction[i] = 1;
			}else{
				prediction[i] = -1;
			}
		}
		return prediction;
	}
	
	private void evalForest(int[] prediction, Table testSet){
		int correct = 0;
		int incorrect = 0;
		for(int i = 0; i < testSet.dataTable.size(); i++){
			if(prediction[i] == testSet.dataTable.get(i).label.intValue()){
				correct += 1;
			}else{
				incorrect += 1;
			}
		}
		
		double accuracy = (double) correct/(correct+incorrect)*100;
		this.accuracy = accuracy;
		this.correct = correct;
		this.incorrect = incorrect;
	}
}
