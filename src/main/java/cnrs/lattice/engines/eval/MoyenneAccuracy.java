package cnrs.lattice.engines.eval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import cnrs.lattice.tools.utils.Tools;
 
 
public class MoyenneAccuracy {
 
	
	public static String fichier = "";
	public static double valeursPrec = 0;
	public static double valeursRap = 0;
	public static double valeursFm = 0;
	public static List<Double> Accu = new ArrayList<Double>();
	public static List<Double> rap = new ArrayList<Double>();
	public static List<Double> fm = new ArrayList<Double>();
	public static List<Double> accuracy = new ArrayList<Double>();
	public static List<Double> rappel = new ArrayList<Double>();
	public static List<Double> fmesure = new ArrayList<Double>();
	public static String moyennes = "";
	public static String moyenne = "";
	public static double general = 0;
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		 JFileChooser chooser = new JFileChooser();
		 chooser.setMultiSelectionEnabled(true);
		 chooser.showOpenDialog(null);
		 File[] files = chooser.getSelectedFiles();
		 if (chooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
		 
			
			 for (File fi : files){
		 BufferedReader lecteurFichier=null;
		 lecteurFichier = new BufferedReader(new FileReader(fi));
		 
		 fichier = lecteurFichier.toString();
		 
		 
			while ((fichier = lecteurFichier.readLine()) != null) {
					
					
					if ((fichier.length() == 0) || (fichier.contains("N/A;") == true)) {
						
						
						  continue; 
						         
						  }
						 
					String[] values = fichier.split("\t");
					  
					  accuracy.add(Double.parseDouble(values[1]));
				
					  
					}
			 double globalAcc = 0;
		
			  for (Double a : accuracy) {
			      globalAcc += a;
			  }
			  
			  double moyenneAcc = globalAcc / accuracy.size();
			  //System.out.println();
			 
			  
			  System.out.println("Accuracy: "+ "\t" + moyenneAcc);
			  moyennes += "Accuracy: "+ "\t" + moyenneAcc + "\n";

	
			  Accu.add(moyenneAcc);
			  
			 
			  lecteurFichier.close();
		 }
			 
			 double globalAccuracy = 0;
			  for (Double p : Accu) {
			      globalAccuracy += p;
			  }
			  double AA = globalAccuracy / Accu.size();
			 
			  System.out.println("Accuracy generale: " + AA );
			  moyenne = "Accuracy generale: " + AA + "\n";
			  
			 Tools.ecrire("Score.txt", moyennes);
			  Tools.ajouter("Score.txt", moyenne);
			 
		 }
}catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}
}