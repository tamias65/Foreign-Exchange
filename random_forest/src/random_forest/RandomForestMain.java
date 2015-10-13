package random_forest;

import java.util.ArrayList;

import decision_tree.*;

public class RandomForestMain {

	private static final int numberOfTrees = 1000;
	
	public static void main(String[] args) {
		
		long maxBytes = Runtime.getRuntime().maxMemory();
		System.out.println("Max memory: " + maxBytes / 1024 / 1024 + "M");
		
		int N = numberOfTrees;
		
		Table rootTable;
		rootTable = FileReader.buildRecord("C:/Users/Tamias65/Documents/GitHub/Foreign-Exchange/Assignment2_DecisionTree/decision_tree/data.csv");
		
		ArrayList<Tree> randomForest = new ArrayList<Tree>(N);
		for(int i = 0; i < N; i++){
//			System.out.println("Splitting Table...");
			Table trainTable = new Table();
			Table testTable = new Table();
			int partition = rootTable.dataTable.size()*2/3;
//			System.out.println("In RF: rootTable has "+rootTable.dataTable.get(0).getFeatureSize()+" features");
			rootTable.splitRandomInstances(trainTable, testTable, partition);
//			System.out.println("In RF: rootTable has "+rootTable.dataTable.get(0).getFeatureSize()+" features");

			Node root = new Node();
			if(trainTable.dataTable.isEmpty()){
				System.out.println("No training set found. Exiting...");
				System.exit(0);
			}
			root.instances = trainTable;
			Tree t = new Tree(root);
			t.buildTree(root);
			
			randomForest.add(t);
			int numOfTrees = i+1;
//			System.out.println("Tree added "+numOfTrees);
			
			Test test = new Test();
			test.testForest(randomForest, testTable);
			System.out.println(numOfTrees+": "+test.accuracy);
		}
		
//		Table testSet;
//		testSet = FileReader.buildRecord("C:/Users/Tamias65/Documents/GitHub/Foreign-Exchange/Assignment2_DecisionTree/decision_tree/test.csv");
//		Test test = new Test();
//		test.testTree(root, testSet);
//		System.out.println("Accuracy: "+test.accuracy);
//		System.out.println("Correct: "+test.correct);
//		System.out.println("Incorrect: "+test.incorrect);
	}
	
		
}