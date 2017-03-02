package cnrs.lattice.tools.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.google.common.base.Joiner;

import cnrs.lattice.tools.utils.Tools;

/**
 * Classe isolant differents types d'erreurs issues des traitements du SRCMF.
 * 
 * @author Gael
 * 
 */
public class Fixer {

	private static String res = "";
	private static String erreurs = "";
	private static List<String> listeID = new ArrayList<String>();

	
	
	
	/**
	 * Correction error parse + decalage des identifiants qui suit dans la même phrase.
	 * @return String contenu du conll corrigé.
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static String CorrectionParseID(String conllContent) throws IOException {
			List<String> lines = Tools.StringToList(conllContent);
			StringBuilder sb = new StringBuilder();
			Joiner joiner = Joiner.on("\t");
			int mode = 0;
			int current = 0;
			int total = lines.size();
			for(String line : lines){
				current++;
				Tools.printProgress(total, current);
				if (mode == 1) {
					String[] values = line.split("\t");
					if (line.length() == 0) {
						sb.append("\n");
						mode = 0;
						continue;
					}

					if (values[0].equals("") && values.length > 1) {
						continue;
					}

					if (values.length > 1) {
						int f = Integer.parseInt(values[1]) - 1;
						int dependency = Integer.parseInt(values[9]);
						if (dependency > 0)
							dependency = dependency - 1;
						values[1] = String.valueOf(f);
						values[9] = String.valueOf(dependency);
						sb.append(joiner.join(values) + "\n");
						continue;
					}
				}

				if (mode == 0) {

					if (line.length() == 0) {
						sb.append("\n");
						continue;
					}
					String[] values = line.split("\t");

					if (line
							.equals("## ERROR: sentence cannot be parsed. Please check annotation!")) {
						mode = 1;
						continue;
					}
					
					sb.append(joiner.join(values) + "\n");
				}
			}
			return sb.toString();
	}
	
	
	//////////////////////////////////////////////////////
	
	///////// anciens codes non refactorés pour le moment
	
	//////////////////////////////////////////////////////
	

	/**
	 * Corrige les erreurs de saut de ligne dans les fichiers de resultat de
	 * wapiti.
	 * 
	 * @param Le
	 *            fichier wapiti du resultat de POS
	 * @return Une variable String avec tab comme separateur. Un fichier conll
	 *         pour Malt.
	 * @throws IOException
	 */

	public static void CorrectPOSresult(String filePath) throws IOException {

			File fichier = new File(filePath);
			BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
			String texte = lecteur.toString();
			String format = "";

			while ((texte = lecteur.readLine()) != null) {

				String[] values = texte.split("\t");

				if (texte.length() == 0
						|| values[0]
								.contains("## ERROR: sentence cannot be parsed. Please check annotation!")
						|| values[0].equals("") /* || values.length<1 */) {
					if (texte.length() == 0)
						format += "\n";
					continue;
				}
				String lemmepropre = values[3].replaceAll("_.*$", "");
				format += texte + "\n";
				// System.out.println(values[0] + "\t" + values[2] + "\t" + "_"
				// + "\t" + "_" + "\t" + "_" + "\t" + "_" + "\t" + "_" + "\t" +
				// "_" + "\t" + "_" + "\t" + "_" + "\t" + "_" + "\t" + "_"+ "\t"
				// + "_"+ "\t" + "_");

			}

			String fic = format.replaceAll("\n\n\n", "\n");
			String pure = fic.replaceAll("\n\n\n\n", "\n");

			Tools.ecrire("Malt" + fichier.getName(), res);
			lecteur.close();

		}
	

	/**
	 * Prend un fichier de resultat WApiti POS et met les erreurs dans un
	 * fichier.
	 * 
	 * @param CONLL
	 *            le fichier Conll dont on veut extraire les formes (avec son
	 *            chemin si necessaire)
	 * @return Une variable String avec tab comme separateur. Un fichier conll
	 *         pour mate seulement rempli avec les id et formes.
	 * @throws IOException
	 */
	public static void ErreursPOS(String filePath) throws IOException {

			File fichier = new File(filePath);
			BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
			String texte = lecteur.toString();
			String formes = "";

			while ((texte = lecteur.readLine()) != null) {

				String[] values = texte.split("\t");
				int t = values.length;

				if (texte.length() == 0 || values[0].equals("")) {
					continue;
				}

				if (values[t - 1].equals(values[t - 2]) != true) {
					formes += texte + "\n";
				}

			}

			String nomFichier = "ERROR" + fichier.getName();
			Tools.ecrire(nomFichier, formes);
			lecteur.close();
		}

	

	/**
	 * Ecrit dans un fichier les erreurs de parsing trouvées et dans quel
	 * fichier elles se trouvent.
	 * 
	 * @throws IOException
	 */
	public static void ErreurParsing(String filePath) throws IOException {

			File dir = new File(filePath);

			File[] liste = dir.listFiles();
			Arrays.sort(liste);

			for (int j = 0; j < liste.length; j++) {
				String conll = liste[j].getPath();
				String formes = Erreur1(conll);

				System.out.println(liste[j].getName());
				System.out.println(formes);
				res += "\n\n" + liste[j].getName() + " :\n\n";
				res += formes;

			}
			Tools.ecrire("erreurs.txt", res);

	}

	/**
	 * Prend un fichier conll du SRCMF et extrait les formes et leur
	 * identifiant.
	 * 
	 * @param CONLL
	 *            le fichier Conll dont on veut extraire les formes (avec son
	 *            chemin si necessaire)
	 * @return Une variable String avec tab comme separateur.
	 * @throws IOException
	 */
	private static String Erreur1(String CONLL) throws IOException {

		BufferedReader lecteur = new BufferedReader(new FileReader(CONLL));
		String texte = lecteur.toString();
		String formes = "";

		while ((texte = lecteur.readLine()) != null) {

			String[] values = texte.split("\t");

			if (texte.length() == 0
					|| values[0]
							.contains("## ERROR: sentence cannot be parsed. Please check annotation!") /*
																										 * ||
																										 * values
																										 * .
																										 * length
																										 * <
																										 * 1
																										 */) {
				continue;
			}
			if (values[0].contains("http") != true
					&& ((values[0].equals("")) && (values[1].equals("1") != true))) {
				formes += texte + "\n";
			}

			if (values.length <= 1)
				formes += texte + "\n";
			if ((values.length == 2)/* &&(values[2].contains("\n")) */) {

				formes += texte + "\n";

			}
		}

		lecteur.close();
		return formes;
	}

	/**
	 * Si erreur de syntaxe, ajoute étiquette correspondant au type d'erreur.
	 * 
	 * @throws IOException
	 */
	private static void ErreurSyntaxeMate(String filePath) throws IOException {

			File fichier = new File(filePath);

			BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
			String texte = lecteur.toString();

			float total = 0;
			float correctLAS = 0;
			float correctUAS = 0;

			while ((texte = lecteur.readLine()) != null) {
				total += 1;
				String[] ligne = texte.split("\t");
				if (texte.length() == 0)
					continue;
				if ((ligne[9].equals(ligne[8]) && ligne[11].equals(ligne[10])) != true) {
					erreurs += texte + "\t" + "ERREUR_LAS" + "\n";
					correctLAS += 1;
					continue;
				}
				if ((ligne[9].equals(ligne[8])) != true) {
					erreurs += texte + "\t" + "ERREUR_UAS" + "\n";
					correctUAS += 1;
					continue;
				}
				erreurs += texte + "\t" + "" + "\n";
			}

			float UAS = correctUAS / total;
			float LAS = correctLAS / total;

			Tools.ecrire("ERROR" + fichier.getName(), erreurs);
			lecteur.close();
			JOptionPane.showMessageDialog(null, "UAS = " + UAS + "\n"
					+ "LAS = " + LAS);
			System.out.println("UAS = " + UAS + "\n" + "LAS = " + LAS);
	}

	public static void ModifyXMLID(String filePath) throws JDOMException, IOException {
		NumberFormat formatter = new DecimalFormat("00000");
			File fichier = new File(filePath);
			String fic = fichier.getAbsolutePath();
			// BufferedReader lecteur = new BufferedReader(new
			// FileReader(fichier));
			// String texte = lecteur.toString();

			org.jdom2.Document document;
			Element racine;

			// On crée une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();

			// On crée un nouveau document JDOM avec en argument le fichier XML
			document = sxb.build(new File(fic));

			// On initialise un nouvel élément racine avec l'élément racine du
			// document.
			racine = document.getRootElement();
			// déclaration des namespaces utiles
			@SuppressWarnings("unused")
			Namespace tei = Namespace
					.getNamespace("http://www.tei-c.org/ns/1.0");
			Namespace ns = Namespace.XML_NAMESPACE;

			// ////////Parcours XML XPATH

			// use the default implementation
			XPathFactory xF = XPathFactory.instance();
			// System.out.println(xFactory.getClass());

			XPathExpression<Element> exp = xF.compile("//*[local-name()='w']",
					Filters.element()); // .compile("//w", Filters.element("w",
										// ns));
			List<Element> wList = exp.evaluate(racine);
			for (Element w : wList) {
				// System.out.println( linkElement.getAttributeValue("type"));

				if ((w.getAttribute("id", ns) != null)) {
					String yo = w.getAttribute("id", ns).getValue();
					String[] lol = yo.split("_");
					int idey = Integer.parseInt(lol[1]) + 1;
					String ideyplus = formatter.format(idey);
					w.getAttribute("id", ns).setValue(lol[0] + "_" + ideyplus);

					/*
					 * listeID.add(ideyplus); System.out.println(ideyplus);
					 */
				}
			}

			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(document, new FileWriter("file.xml"));

			xmlOutput.output(document, System.out);

			System.out.println("File updated!");
	}

	public static void CorrectionIdTEI(String resMatePath) throws JDOMException, IOException {
		NumberFormat formatter = new DecimalFormat("00000");
			File fichier = new File(resMatePath);
			String fic = fichier.getAbsolutePath();
			BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
			String texte = lecteur.toString();

			org.jdom2.Document document;
			Element racine;

			// On crée une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();

			// On crée un nouveau document JDOM avec en argument le fichier XML
			document = sxb.build(new File(fic));

			// On initialise un nouvel élément racine avec l'élément racine du
			// document.
			racine = document.getRootElement();

			// déclaration des namespaces utiles
			@SuppressWarnings("unused")
			Namespace tei = Namespace
					.getNamespace("http://www.tei-c.org/ns/1.0");
			Namespace ns = Namespace.XML_NAMESPACE;

			/*
			 * //////////Parcours XML XPATH
			 * 
			 * //use the default implementation XPathFactory xFactory =
			 * XPathFactory.instance();
			 * //System.out.println(xFactory.getClass());
			 * 
			 * XPathExpression<Element> expr =
			 * xFactory.compile("//*[local-name()='w']", Filters.element());
			 * //.compile("//w", Filters.element("w", ns)); List<Element> wListe
			 * = expr.evaluate(racine); for (Element w : wListe) {
			 * //System.out.println( linkElement.getAttributeValue("type")); if
			 * ((w.getAttribute("id", ns) != null)) { String yo =
			 * w.getAttribute("id", ns).getValue(); String[] lol =
			 * yo.split("_"); int idey = Integer.parseInt(lol[1]); String
			 * ideyplus = formatter.format(idey);
			 * 
			 * listeID.add(ideyplus); System.out.println(ideyplus); } }
			 */

			// ////////Parcours XML XPATH

			// use the default implementation
			XPathFactory xF = XPathFactory.instance();
			// System.out.println(xFactory.getClass());

			XPathExpression<Element> exp = xF.compile("//*[local-name()='w']",
					Filters.element()); // .compile("//w", Filters.element("w",
										// ns));
			List<Element> wList = exp.evaluate(racine);
			for (Element w : wList) {
				// System.out.println( linkElement.getAttributeValue("type"));

				if ((w.getAttribute("id", ns) != null)) {
					String yo = w.getAttribute("id", ns).getValue();
					String[] lol = yo.split("_");
					int idey = Integer.parseInt(lol[1]);
					String ideyplus = formatter.format(idey);
					w.getAttribute("id", ns).setValue(lol[0] + ideyplus);

					/*
					 * listeID.add(ideyplus); System.out.println(ideyplus);
					 */
				}
			}

			System.out.println();
			String contenu = texte;
			for (Element y : wList) {
				contenu += y.toString() + "\n";
			}
			Tools.ecrire("YOOOOOOYOYOYO", contenu);
	}

	

	/**
	 * correction decalage id
	 * 
	 * @throws JDOMException
	 * @throws IOException
	 */
	private static void CorrectionDecalID(String conllPath) throws IOException {
			File fichier = new File(conllPath);
			BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
			String texte = lecteur.toString();
			String r = "";

			while ((texte = lecteur.readLine()) != null) {

				if (texte.length() == 0) {
					r += "\n";
					continue;
				}

				String[] values = texte.split("\t");
				String[] id = values[0].split("_");
				int finid = Integer.parseInt(id[1]) - 1;
				r += id[0] + "_" + finid + "\t" + values[1] + "\t" + values[2]
						+ "\t" + values[3] + "\t" + values[4] + "\t"
						+ values[5] + "\t" + values[6] + "\t" + values[7]
						+ "\t" + values[8] + "\t" + values[9] + "\t"
						+ values[10] + "\t" + values[11] + "\t" + values[12]
						+ "\t" + values[13] + "\t" + values[14] + "\t"
						+ values[15] + "\t" + values[16] + "\n";
			}
			Tools.ecrireSelonNomFichier(fichier.getAbsolutePath(),
					"FIXED.conll", r);
			lecteur.close();
	}

	/**
	 * correction dependency issue
	 * 
	 * @throws JDOMException
	 * @throws IOException
	 */
	private static void CorrectionDependance(String conll, String mateRef) throws IOException {
			File fichier = new File(conll);
			BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
			String texte = lecteur.toString();

				File fichier2 = new File(mateRef);
				BufferedReader lecteur2 = new BufferedReader(new FileReader(
						fichier2));
				String texte2 = lecteur2.toString();

				String r = "";

				while (((texte = lecteur.readLine()) != null)
						&& ((texte2 = lecteur2.readLine()) != null)) {

					if (texte.length() == 0) {
						r += "\n";
						continue;
					}
					String[] correct = texte2.split("\t");
					String[] values = texte.split("\t");
					r += values[0] + "\t" + values[1] + "\t" + values[2] + "\t"
							+ values[3] + "\t" + values[4] + "\t" + values[5]
							+ "\t" + values[6] + "\t" + values[7] + "\t"
							+ values[8] + "\t" + correct[8] + "\t" + values[10]
							+ "\t" + values[11] + "\t" + values[12] + "\t"
							+ values[13] + "\t" + values[14] + "\t"
							+ values[15] + "\t" + values[16] + "\n";
				}
				Tools.ecrireSelonNomFichier(fichier.getAbsolutePath(),
						"DF.conll", r);
				lecteur.close();
				lecteur2.close();
			}
	
	
	/**
	 * correction dependency issue
	 * @throws Exception 
	 * 
	 * @throws JDOMException
	 */
	public static void CheckData(String conll) throws Exception {		
		for(String sent : Corpus.getSentences(Tools.tempFile("check", ".temp", conll))){
			List<String> lines = Tools.StringToList(sent);
			int valueMax = lines.size() ;
			for(String line : lines){
				String[] cols = line.split("\t");
				if(Integer.parseInt(cols[9]) > valueMax){
//					System.out.println(line);
					System.exit(0);
				}
			}
		}
	}
	
	/**
	 * Corrige les redondance de combinaison d'étiquettes syntaxiques.
	 * Ex: "RelNC_RelNC_Obj_RelNC" devient "RelNC_Obj"
	 * @param content
	 * @param colNb
	 * @return le même fichier avec les tags de la colonne spécifée corrigés
	 * @throws Exception
	 */
	public static String fixSyntaxDuplicataAndAnnotatorTag (String content, int colNb)throws Exception{
		List<String> lines = Tools.StringToList(content);
		StringBuilder sb = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		Joiner joinerSyn = Joiner.on("_");
		for(String line : lines){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			String[] cols = line.split("\t");
			String syn = cols[colNb];
			syn = syn.replaceAll("\\[.+\\]", "").replaceAll("\\[.+\\]", "");
			String[] tags = syn.split("_");
			List<String> tagsList = Arrays.asList(tags);
			List<String> tagsListFixed = Tools.ListeNonRedondante(tagsList, 0);
			String synFixed = joinerSyn.join(tagsListFixed);
			cols[colNb] = synFixed;
			sb.append(joiner.join(cols) + "\n");
		}
		return sb.toString();
	}
	

	/**
	 * Fix lines who have "Ignorer" as syntactic function. Thus, it suppress them and reajust
	 * the sentences.
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String fixIgnorerLine(String path)throws Exception{
		// prend le chemin du fichier et le transforme en une liste de phrases/clauses
		List<String> sentences = Corpus.getSentences(path);
		//liste qui sera remplie par les phrases corrigées
		List<String> sentencesFixed = new ArrayList<String>();
		Joiner joiner = Joiner.on("\t");
		Joiner joinerSent = Joiner.on("\n");
		
		// pour chaque phrase fait ...
		for(String sentence : sentences){
			int depMax = 0;
			// récupère les lignes de chaque phrase
			List<String> lines = Tools.StringToList(sentence);
			int index = 0;
			//initialisation du trigger qui permettra de changer de comportement une fois le cas rencontré
			boolean mode = false;
			StringBuilder sent = new StringBuilder();
			//pour chaque ligne fait ...
			int trigger = 0;
			
			for(String line : lines){	
				//sépare les valeurs d'une ligne et les mets en tableau
				String[] cols = line.split("\t");
				
				//si Ignorer a été trouvé faire ...
				if(mode){
					int id = Integer.parseInt(cols[1]) - 1;
					int dep = Integer.parseInt(cols[9]);
					cols[1] = String.valueOf(id);
					//réduit la dépendance si elle est supérieure à l'ID du déclencheur ou si elle n'est pas égale à zero (root)
					if((dep >= trigger)&&(dep>1)){
						dep--;
						cols[9] = String.valueOf(dep);
					}
					sent.append(joiner.join(cols) + "\n");
					index++;
					continue;
				}
				
				//Si Ignorer n'est pas trouvé faire...
				//mieux vaut choisir "contains" car il existe coord1-Ignorer par exemple (roland)
				if(cols[11].contains("Ignorer")){
					// mise en mémoire du déclencheur pour voir les lignes précédentes
					depMax = Integer.parseInt(cols[1]);	
					StringBuilder reverse = new StringBuilder();
					for(int i = 0; i<index; i++){
						String[] values = lines.get(i).split("\t");
						int dep = Integer.parseInt( values[9] );
						//si la dépendance est supérieure à celle du déclencheur alors il faut ajuster la dépendance
						if(dep >= depMax){
							dep = dep - 1;
								values[9] = String.valueOf(dep);
						}
						reverse.append(joiner.join(values) + "\n");
						sent = reverse;
					}
					
					// garde en mémoire l'ID du déclencheur
					trigger = Integer.parseInt(cols[1]);	
					//active le mode pour changer le comportement = dit que Ignorer est trouvé
					mode = true;
					String[] precedent = lines.get(index-1).split("\t");
					String[] suivant = lines.get(index+1).split("\t");
					
					if((precedent[5].equals("_"))||(precedent[5].length() == 0)){
						precedent[5] = cols[5];
						lines.set(index-1,joiner.join(precedent));
					}
					if((suivant[5].equals("_"))||(suivant[5].length() == 0)){
						suivant[5] = cols[5];
						lines.set(index+1,joiner.join(suivant));
					}
					index++;
					continue;
				}
				sent.append(joiner.join(cols) + "\n");
				index++;
				
			}
			sentencesFixed.add(sent.toString());
		}
		return joinerSent.join(sentencesFixed);
	}
	
	
	/**
	 * Fix lines who have "Ignorer" as syntactic function. Thus, it suppress them and reajust
	 * the sentences.
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String fixIgnorerLine(String path, int idSent, int pos, int head, int phead)throws Exception{
		// prend le chemin du fichier et le transforme en une liste de phrases/clauses
		List<String> sentences = Corpus.getSentences(path);
		//liste qui sera remplie par les phrases corrigées
		List<String> sentencesFixed = new ArrayList<String>();
		Joiner joiner = Joiner.on("\t");
		Joiner joinerSent = Joiner.on("\n");
		
		// pour chaque phrase fait ...
		for(String sentence : sentences){
			int depMax = 0;
			// récupère les lignes de chaque phrase
			List<String> lines = Tools.StringToList(sentence);
			int index = 0;
			//initialisation du trigger qui permettra de changer de comportement une fois le cas rencontré
			boolean mode = false;
			StringBuilder sent = new StringBuilder();
			//pour chaque ligne fait ...
			int trigger = 0;
			
			for(String line : lines){	
				//sépare les valeurs d'une ligne et les mets en tableau
				String[] cols = line.split("\t");
				
				//si Ignorer a été trouvé faire ...
				if(mode){
					int id = Integer.parseInt(cols[idSent]) - 1;
					int dep = Integer.parseInt(cols[head]);
					cols[idSent] = String.valueOf(id);
					//réduit la dépendance si elle est supérieure à l'ID du déclencheur ou si elle n'est pas égale à zero (root)
					if((dep >= trigger)&&(dep>1)){
						dep--;
						cols[head] = String.valueOf(dep);
					}
					sent.append(joiner.join(cols) + "\n");
					index++;
					continue;
				}
				
				//Si Ignorer n'est pas trouvé faire...
				//mieux vaut choisir "contains" car il existe coord1-Ignorer par exemple (roland)
				if(cols[phead].contains("Ignorer")){
					// mise en mémoire du déclencheur pour voir les lignes précédentes
					depMax = Integer.parseInt(cols[idSent]);	
					StringBuilder reverse = new StringBuilder();
					for(int i = 0; i<index; i++){
						String[] values = lines.get(i).split("\t");
						int dep = Integer.parseInt( values[head] );
						//si la dépendance est supérieure à celle du déclencheur alors il faut ajuster la dépendance
						if(dep >= depMax){
							dep = dep - 1;
								values[head] = String.valueOf(dep);
						}
						reverse.append(joiner.join(values) + "\n");
						sent = reverse;
					}
					
					// garde en mémoire l'ID du déclencheur
					trigger = Integer.parseInt(cols[idSent]);	
					//active le mode pour changer le comportement = dit que Ignorer est trouvé
					mode = true;
					String[] precedent = lines.get(index-1).split("\t");
					String[] suivant = lines.get(index+1).split("\t");
					
					if((precedent[pos].equals("_"))||(precedent[pos].length() == 0)){
						precedent[pos] = cols[pos];
						lines.set(index-1,joiner.join(precedent));
					}
					if((suivant[pos].equals("_"))||(suivant[pos].length() == 0)){
						suivant[pos] = cols[pos];
						lines.set(index+1,joiner.join(suivant));
					}
					index++;
					continue;
				}
				sent.append(joiner.join(cols) + "\n");
				index++;
				
			}
			sentencesFixed.add(sent.toString());
		}
		return joinerSent.join(sentencesFixed);
	}
	
}