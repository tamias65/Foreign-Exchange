/*Record.java
 * 
 * Alvin Chou
 * 9/29/2015
 * 
 * Class Record for maintaining each row of data in the table.
 */

package decision_tree;

import java.util.*;

public class Record {
	String timestamp;
	Integer label;
	double[] features;
	
	public Record(){
		this.timestamp = null;
		this.label = null;
		this.features = null;
	}
	
	public void createRecord(String[] rowSplit){
		this.timestamp = rowSplit[0];
		this.label = Integer.parseInt(rowSplit[1]);
		this.features = this.stringArray2DoubleArray(Arrays.copyOfRange(rowSplit, 2, rowSplit.length)); 
	}
	
	//Separate data into arrays instead of lists for easier access.
	public double[] stringArray2DoubleArray(String[] stringArray){
		double[] doubleArray = new double[stringArray.length];
		for(int i = 0; i < stringArray.length; i++){
			doubleArray[i] = Double.parseDouble(stringArray[i]);
		}
		return doubleArray;
	}
}
