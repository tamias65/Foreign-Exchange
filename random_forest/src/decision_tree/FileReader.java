/*FileReader.java
 * 
 * Alvin Chou
 * 9/29/2015
 * 
 * 
 */

package decision_tree;

import java.io.*;

public class FileReader {
	private static final String csvSplitBy = ",";
	// Defines the features to be used
	private static final String[] features = {"Timestamp","Label (t+1)","GBP/USD Change",
			"EUR/USD Change","AUD/NZD Change","EUR/JPY Change"};
	
	// Read in the CSV file then store all data to a list called recordTable
	public static Table buildRecord(String path){
		BufferedReader reader = null;
		Table recordTable = new Table();
		boolean isParamSet = false;
		
		try{
			File f = new File(path);
			FileInputStream fh = new FileInputStream(f);
			reader = new BufferedReader(new InputStreamReader(fh));
			String row;
			int[] stringMapping = new int[features.length];
			int numDeletedRows = 1;
			while ((row = reader.readLine()) != null){
				// Check whether mapping of features between read in rows and those defined above
				// has been determined.
				if(!isParamSet){
					mapParam(row, stringMapping);
					isParamSet = true;
					continue;
				}
				Record newRecord = new Record();
				String[] rowSplit = row.split(csvSplitBy);
				String[] filteredRow = new String[stringMapping.length];
				boolean error = sortAndFilterRow(rowSplit, stringMapping, filteredRow);
				if(error){
					int errLine = recordTable.dataTable.size()+numDeletedRows;
					numDeletedRows += 1;
//					System.out.println("Empty data at "+errLine+".  Ignoring...");
				}else{
					newRecord.createRecord(filteredRow);	
					recordTable.dataTable.add(newRecord);
					//System.out.println(recordTable.size());
				}
			}
			
			if(!isParamSet){
				System.out.println("File is blank. Exiting...");
				System.exit(0);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Done building "+recordTable.dataTable.size()+" records!");
		return recordTable;
	}
	
	// Map the positions of the features in Records with the positions of features read in
	public static void mapParam(String row, int[] stringMapping){
		String[] rowSplit = row.split(csvSplitBy);
		int featureCount = 0;
		for(int i = 0; i < rowSplit.length; i++){
			for(int j = 0; j < features.length; j++){
				if(rowSplit[i].equals(features[j])){
					stringMapping[j] = i;
					featureCount ++;
					break;
				}
			}
		}
		
		// Check that all features in Records are properly match with features read in
		if(featureCount != features.length){
			int miscount = features.length-featureCount;
			System.out.println("Features mismatch by "+miscount+". Exiting...");
			System.exit(0);
		}
	}
	
	// Given a split row, sort the string array by the mapping created and filtered out 
	// not used features.
	public static boolean sortAndFilterRow(String[] rowSplit, int[] stringMapping, String[] filteredRow){
		boolean error = false;
		for(int i = 0; i < stringMapping.length; i++){
			if(!rowSplit[stringMapping[i]].isEmpty()){
				filteredRow[i] = rowSplit[stringMapping[i]];
			}else{
				error = true;
				break;
			}
		}
		return error;
	}
	
}
