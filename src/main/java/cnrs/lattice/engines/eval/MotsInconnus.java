package cnrs.lattice.engines.eval;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cnrs.lattice.tools.utils.Tools;

public class MotsInconnus {
	private static List<Integer> score = new ArrayList<Integer>();
	private static List<Integer> scoreMot = new ArrayList<Integer>();
	private static List<String> listeTest = new ArrayList<String>();
	private static List<String> listeMotTest = new ArrayList<String>();
	private static List<String> listeMotTrain = new ArrayList<String>();
	private static List<String> listeTrain = new ArrayList<String>();
	private static String compareTrain = "";
	private static String compareTest = "";
	private static  int i = 0;
	private static double global = 0;
	private static double globalMot = 0;
	//private static String saut = System.getProperty("line.separator");
	private static String infos = "";
	 
	 
	public static void main(String args[]) throws IOException{
		
		try{
			

			
			BufferedReader lecteurTrain = new BufferedReader(new FileReader(args[0]));
			BufferedReader lecteurTest = new BufferedReader(new FileReader(args[1]));
			/*BufferedReader lecteurTrain = trans(file1);
			BufferedReader lecteurTest = trans(test);*/
			
			String texteTest = lecteurTest.toString();
			String texteTrain = lecteurTrain.toString();
			
			
			while (( texteTest = lecteurTest.readLine()) != null  ) {
				if (texteTest.length() == 0 ) {
					  continue;
					  }
				//pour garder redondance, enlever le if et compareTest.
				if(compareTest.contains(texteTest)!= true){
				String[] ligneTest = texteTest.split("\t"); 
				listeTest.add(texteTest);
				listeMotTest.add(ligneTest[0]);
				}
				compareTest += texteTest;
			}
			
			while (( texteTrain = lecteurTrain.readLine()) != null  ) {
				if (texteTrain.length() == 0 ) {
					  continue;
					  }
				if(compareTrain.contains(texteTrain)!= true){
				
				
				
				String[] ligneTrain = texteTrain.split("\t"); 
				listeTrain.add(texteTrain);
				listeMotTrain.add(ligneTrain[0]);
				}
				compareTrain += texteTrain;
				
			}
			
			
				for(String eleTest : listeTest){
					i=0;
					for(String eleTrain : listeTrain){
						
						if(eleTrain.equals(eleTest)){
							
							i++;
							//System.out.println(eleTest + " ==========>  " + eleTrain);
							break;
						}
						
					}
					if(i>0){
						score.add(1);
					}else{
					
						score.add(0);
					}
				}
				
				
				
				for(String motTest : listeMotTest){
					i=0;
					for(String motTrain : listeMotTrain){
						
						if(motTrain.equals(motTest)){
							
							i++;
							//System.out.println(motTest + " ==========>  " + motTrain);
							break;
						}
						
					}
					if(i>0){
						scoreMot.add(1);
					}else{
					
						scoreMot.add(0);
					}
				}
			
			
				
				
			
			/////////////////////////////////////////

			for (int k : score){
			System.out.print(k);
			global += k;
			}
			double nb = score.size();
			System.out.println("\n" + "Mots connus avec leur etiquette et lemme : " +  global + " sur un total de " + nb );
			
		
			for (int k : scoreMot){
				System.out.print(k);
				globalMot += k;
				}
				double nbMot = scoreMot.size();
				System.out.println("\n" + "Mots connus : " +  globalMot + " sur un total de " + nbMot );
				
			  double moyenne = global/nb;
			  double moyenneMot = globalMot/nbMot;
			System.out.println("Taux de mots connus avec leurs etiquettes et lemmes : " + moyenne*100 + "% !");
			System.out.println("Taux de mots inconnus avec leurs etiquettes et lemmes : " + (100-(moyenne*100)) + "% !");
			//System.out.println("compareTrain = " + compareTrain);
			System.out.println("Taux de mots connus : " + moyenneMot*100 + "% !");
			System.out.println("Taux de mots inconnus : " + (100-(moyenneMot*100)) + "% !");
			
			infos += "Taux de mots inconnus avec leurs etiquettes et lemmes : " +"\t"+ (100-(moyenne*100)) + "\t" + "Taux de mots inconnus : " +"\t"+ (100-(moyenneMot*100));			
			Tools.ecrire(args[2], infos);
							
							
							
							
							
			lecteurTest.close();				
			lecteurTrain.close();				
		}catch(Exception e){
			
			
			System.out.println(e.toString());
		}
		
		
	}
	
	
	
	
	
	
	

	public static BufferedReader transStringFlux (String arg) throws IOException {
		
		
		//convert String into InputStream
		InputStream Fichier = new ByteArrayInputStream(arg.getBytes());
		// read it with BufferedReader
		  BufferedReader lecteur = new BufferedReader(new InputStreamReader(Fichier));
		//String texte = lecteur.toString();
		
		
		
		return lecteur;
        
	}//fin m�thode
	
	
}
