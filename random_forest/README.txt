Big Data Assignment 3

Decision Tree

Included files:

In decision_tree:
- DecisionTreeMain.java - main run file for decision tree.
- FileReader.java - Class reading in files to list from .csv
- InformationGain.java - Class calculating informatin gain, probability,
			and entropy.
- Test.java - Class for testing new test data against built forest/tree.

Data Structure classes:
- Record.java - Class structure for storing data.
- Table.java - Class structure for managing data table of Records.
- Node.java - Class node structure for trees. Include the data table
		at the node, split criteria, and the left and right node ptrs.
- Tree.java - Class for building and creating tree.

In random_forest:
- RandomForestMain.java - main run file for random forest.


To run:
1. Change the filepath for "data.csv" in DecisionTreeMain/RandomForestMain to correct train data path.
2. Change the filepath for "test.csv" in DecisionTreeMain/RandomForestMain to correct test data path.
3. Run DecisionTreeMain.java/RandomForestMain.java