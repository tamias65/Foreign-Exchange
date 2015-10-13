/*InformationGain.java
 * 
 * Alvin Chou
 * 9/29/2015
 * 
 * Class for calculating information gain, including probability, conditional probability, and entropy.
 */

package decision_tree;

//import java.util.ArrayList;

public class InformationGain {
	
	private static final int LESS_THAN_EQUALS = -1;
	private static final int GREATER_THAN = 1;
	private static final int NO_CONDITIONAL = 0;
	
	// Calculate information gain where IG = H(Y)-H(Y|X)
	public static double getInformationGain(Table instances, int featureIndx, double median){
		double labelEntropy = getEntropy(instances, 0, 0.0, NO_CONDITIONAL);
		double conditionalEntropy = getConditionalEntropy(instances, featureIndx, median);
		double featureGain = labelEntropy - conditionalEntropy;
		return featureGain;
	}
	
	// Includes conditional and normal entropy
	private static double getEntropy(Table instances, int givenIndx, double givenMedian, int compare){
		double entropy;
		if(compare == NO_CONDITIONAL){
			double[] probability = prob(instances, 0);
			// E(Y) = -P(Y=0)*log2(P(Y=0))-P(Y=1)*log2(P(Y=1))
			entropy = -probability[0]*(log2(probability[0]))
					  -probability[1]*(log2(probability[1]));
		}else{
			double[] probability = prob(instances, givenIndx, givenMedian, compare);
			entropy = -probability[0]*(log2(probability[0]))
					  -probability[1]*(log2(probability[1]));
		}
		return entropy;
	}
	
	private static double getConditionalEntropy(Table instances, int featureIndx, double featureMedian){
		double conditionalEntropy;
		double[] featureProb = prob(instances, featureIndx, featureMedian, 0);
		// Conditional probability = P(X=0)*H(Y|X=0)+P(X=1)*H(Y|X=1)
		conditionalEntropy = featureProb[0]*getEntropy(instances, featureIndx, featureMedian, LESS_THAN_EQUALS)
							+featureProb[1]*getEntropy(instances, featureIndx, featureMedian, GREATER_THAN);
		return conditionalEntropy;
	}
	
	private static double[] prob(Table instances, int compare){
		return prob(instances, -1, -1, compare);
	}
	
	// Calculating P(X), P(Y), and P(Y|X)
	private static double[] prob(Table instances, int indx, 
			double median, int compare){
		
		double[] probability = {0,0};
		int size = 0;
		
		if(compare == NO_CONDITIONAL && indx != -1){
			for(int i = 0; i < instances.dataTable.size(); i++){
				if(instances.dataTable.get(i).features[indx] > median){
					probability[0] += 1;
				}else{
					probability[1] += 1;
				}
				size += 1;
			}
		}else if(compare == NO_CONDITIONAL && indx == -1){
			for(int i = 0; i < instances.dataTable.size(); i++){
				if(instances.dataTable.get(i).label > 0){
					probability[0] += 1;
				}else{
					probability[1] += 1;
				}
				size += 1;
			}
		}else if(compare == LESS_THAN_EQUALS){
			
			for(int i = 0; i < instances.dataTable.size(); i++){
				if(instances.dataTable.get(i).features[indx] <= median){
					if(instances.dataTable.get(i).label > 0){
						probability[0] += 1;
					}else{
						probability[1] += 1;
					}
					size += 1;
				}
			}
		}else{
			for(int i = 0; i < instances.dataTable.size(); i++){
				if(instances.dataTable.get(i).features[indx] > median){
					if(instances.dataTable.get(i).label > 0){
						probability[0] += 1;
					}else{
						probability[1] += 1;
					}
					size += 1;
				}
			}
		}
		
		if(probability[0] != 0){
			probability[0] /= size;
		}
		
		if(probability[1] != 0){
			probability[1] /= size;
		}
		
		return probability;
	}
	
	private static double log2(double x){
		double y;
		if(x == 0){
			y = 0;
		}else{
			y = Math.log(x)/Math.log(2);
		}
		return y;
	}
}
