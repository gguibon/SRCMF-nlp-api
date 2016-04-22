package cnrs.lattice.engines.eval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import cnrs.lattice.tools.utils.Tools;
 
 
public class MoyenneMotsInconnus {
 
	
	private static String fichier = "";
	private static List<Double> ligne = new ArrayList<Double>();
	private static List<Double> mot = new ArrayList<Double>();
	private static List<Double> ligneT = new ArrayList<Double>();
	private static List<Double> motT = new ArrayList<Double>();
	private static String moyennes = "";
	private static String moyenne = "";
	private static String TotalM = "";
	private static String TotalL = "";

 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		 JFileChooser chooser = new JFileChooser();
		 chooser.setMultiSelectionEnabled(true);
		 //chooser.setCurrentDirectory(dir);
		 chooser.showOpenDialog(null);
		 File[] files = chooser.getSelectedFiles();
		 if (chooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
		 
			
			 for (File fi : files){
		 BufferedReader lecteurFichier=null;
		 lecteurFichier = new BufferedReader(new FileReader(fi));
		 
		 fichier = lecteurFichier.toString();
		 
		 
			while ((fichier = lecteurFichier.readLine()) != null) {
					
					
					if (fichier.length() == 0) {
						
						
						  continue; 
						         
						  }
						 
					String[] values = fichier.split("\t");
					  
					  ligne.add(Double.parseDouble(values[1]));
					  mot.add(Double.parseDouble(values[3]));
				
					  
					}
			
			// la donnée complète (mot + lemme + morpho)
			 double globalLigne = 0;
		
			  for (Double a : ligne) {
			      globalLigne += a;
			  }
			  
			  double moyenneLigne = globalLigne / ligne.size();
			  //System.out.println();
			 
			  
			  System.out.println("Donnees inconnues : "+ "\t" + moyenneLigne);
			  moyenne += "Donnees inconnues : "+ "\t" + moyenneLigne + "\n";
			  ligneT.add(moyenneLigne);
			  
			  //le mot seul
			  double globalMot = 0;
				
			  for (Double m : mot) {
			      globalMot += m;
			  }
			  
			  double moyenneMot = globalMot / mot.size();
			  //System.out.println();
			 
			  
			  System.out.println("Mots inconnus : "+ "\t" + moyenneMot);
			  moyennes += "Mots inconnus : "+ "\t" + moyenneMot + "\n";
			  motT.add(moyenneMot);
			  
			  
			  lecteurFichier.close();
			  
			 
		 }//for fichiers
			 
			//les moyennes générales
			  double globalMotT = 0;
			  for (Double t : motT) globalMotT += t;
			  double moyenneMotT = globalMotT / motT.size();
			  
			  double globalLigneT = 0;
			  for (Double l : ligneT) globalLigneT += l;
			  double moyenneLigneT = globalLigneT / ligneT.size();
			  
			  TotalM += "Total Mots inconnus : " + moyenneMotT + "\n";
			  TotalL += "Total Donnees inconnues : " + moyenneLigneT + "\n";
			  
			  String total = moyennes + moyenne + TotalL + TotalM;
			  Tools.ecrire(args[0] + "TotalMotsInconnus.txt", total);
			 
			 
		 }
}catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}
}