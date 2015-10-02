/*DecisionTreeMain.java
 * 
 *
 * Alvin Chou
 * 9/29/2015
 *
 * Main program for training decision trees and testing
 * using a dataset.  The tree does no prunning.
 */

package decision_tree;

import java.util.*;

public class DecisionTreeMain {

	public static void main(String[] args) {
		// Build decision tree using training data
		ArrayList<Record> rootTable;
		rootTable = FileReader.buildRecord("C:/Users/Tamias65/Desktop/2015/data.csv");
		Node root = new Node();
		root.dataTable = rootTable;
		Tree t = new Tree(root);
		t.buildTree(root);
		
		System.out.println("DONE building tree!");
		
		// Test decision tree using test data
		ArrayList<Record> testSet;
		testSet = FileReader.buildRecord("C:/Users/Tamias65/Desktop/2015/test.csv");
		Test test = new Test();
		test.testTree(root, testSet);
		System.out.println("Accuracy: "+test.accuracy);
		System.out.println("Correct: "+test.correct);
		System.out.println("Incorrect: "+test.incorrect);
	}
}
