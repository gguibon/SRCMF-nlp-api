package cnrs.lattice.engines.eval;

import java.io.IOException;
import java.util.List;

import cnrs.lattice.tools.utils.Tools;

public class Eval {
	
/**
 * Calcul l'accuracy en fonction de la valeur actual (GOLD) et predicted. La retourne en String.
 * @param actual
 * @param predicted
 * @param filePath
 * @return accuracy in a String
 * @throws IOException
 */
	public static String getAccuracy(int actual, int predicted, String filePath) throws IOException {

			float total = 0;
			float correct = 0;
			
			List<String> lines = Tools.path2liste(filePath);
			for(String line : lines){
				if(line.length() == 0){continue;}
				total += 1;
				String[] cols = line.split("\t");
				if(cols[actual].equals(cols[predicted])){
					correct += 1;
				}
			}
			float accuracy = correct / total;
		    return String.valueOf(accuracy); 
	}
	
	/**
	 * Calcul le labeled accuracy score en focntion des colonnes données, et du chemin spécifié
	 * @param path
	 * @return En String. Le score d'evaluation, sa date, le nom du fichier, le path du fichier.
	 * @throws IOException
	 */
	public static String getLAS(int actualGovernor, int predictedGovernor, 
			int actualSynFunction, int predictedSynFunction, String filePath) throws IOException {
		float total = 0;
		float correct = 0;
		
		List<String> lines = Tools.path2liste(filePath);
		for(String line : lines){
			if(line.length() == 0){continue;}
			total += 1;
			String[] cols = line.split("\t");
			if(cols[actualGovernor].equals(cols[predictedGovernor]) && 
					(cols[actualSynFunction].equals(cols[predictedSynFunction]))){
				correct += 1;
			}
		}
		float accuracy = correct / total;
	    return String.valueOf(accuracy); 
}

	
}
