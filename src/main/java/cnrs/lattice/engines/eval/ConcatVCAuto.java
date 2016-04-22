package cnrs.lattice.engines.eval;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;




public class ConcatVCAuto {
	

		public static int k = 0;
		private static String saut;
		public static String fichierN2 = ""; 
		public static String concat = "";
		public static String fichierN1 = "";
		public static String fichier = "";
		public static File[] liste ;
		public static int n = 0;
		public static int i = 0;


	public static void main(String[] args) throws IOException{
	
		
		
	 try{
		 
		 System.out.println("Creation des fichiers tests...");
		 saut = System.getProperty("line.separator");
		// Fenetre fen = new Fenetre();
		// Panneau pa = new Panneau();
		 
		
			 
		 
			 File dir = new File(args[1]);
			 
		 
		 File[] listeFiles = dir.listFiles();
		 Arrays.sort(listeFiles);
		// String Name = listeFiles[0].getName().replaceAll("\\d+","");
		 
		
		 /**/
		 while( n < listeFiles.length){
			 
		
			 
		 for (File fic : listeFiles){
		
			 
			if (fic == listeFiles[n]){
				continue;
			}
			 System.out.println(fic.getName());
		 BufferedReader lecteurFichier=null;
		 lecteurFichier = new BufferedReader(new FileReader(fic));
		 fichier = lecteurFichier.toString(); 
		 
		 while (( fichier = lecteurFichier.readLine()) != null) {
				
				
				
				if (fichier.length() == 0) {
					
					concat += saut;
					  //System.out.print(saut);
					  continue; 
					         
					  }

			 concat += fichier + "\n";
			
			}
		 lecteurFichier.close();
		}//for
		
		 

	 
//////////////////////écriture dans fichier	
			//String loul = files[0].getName().replaceAll("\\d+","");;
			
		 
	      FileWriter fw = new FileWriter (args[0]+(n));
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw);
			fichierSortie.println (concat);
			fichierSortie.close();
//////////////////////////////////////////

				//System.out.println(concat);
			
			

 
			//}
n++;
concat = "";		 
		 
		 
		 }
		 
		
			
			}catch (Exception e){
		  System.out.println(e.toString());
 	  }finally {

      }
}
}
	
