package cnrs.lattice.tools.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.google.common.base.Joiner;

import cnrs.lattice.tools.utils.Tools;


public class Corpus {

	
	
	
	/**
	 * Prend un corpus en format un mot par ligne, et melange les phrases
	 * aleatoirement
	 * 
	 * @param path
	 * @return le string melange
	 * @throws IOException
	 */
	public static String melangeurAleatoire(String path) throws IOException {
		File fichier = new File(path);
		BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
		String texte = lecteur.toString();
		String phrase = "";
		List<String> phrases = new ArrayList<String>();

		while ((texte = lecteur.readLine()) != null) {
			if (texte.length() == 0) {
				phrases.add(phrase);
				phrase = "";
				continue;
			}
			phrase += texte + "\n";
		}
		lecteur.close();

		System.out.println("Nombre de phrases/clauses : " + phrases.size());
		String melange = "";
		Tools.shuffleList(phrases);
		for (String ele : phrases)
			melange += ele + "\n";
		return melange;
	}

	/**
	 * Divise un corpus deja melange avec MelangeurAleatoire et le divise en un
	 * corpus d'entrainement et de test.\n La taille du corpus d'entrainement
	 * est a specifier.
	 * 
	 * @param path_melange
	 *            Chemin du corpus deja melange aleatoirement
	 * @param otrain
	 *            Chemin absolu de sortie du corpus d'entrainement
	 * @param otest
	 *            Chemin absolu de sortie du corpus de test
	 * @param nbmotstrain
	 *            Nombre de mots demandes en entrainement
	 * @throws IOException
	 */
	public static void diviseurTrainTest(String path_melange, String otrain,
			String otest, int nbmotstrain) throws IOException {
		String contenu = Tools.readFile(path_melange);
		BufferedReader lecteur = Tools.StringToBufferedReader(contenu);
		String texte = lecteur.toString();
		String train = "";
		String test = "";
		boolean fintrain = false;
		boolean modetest = false;
		int count = 0;
		int motsTrain = 0;

		while ((texte = lecteur.readLine()) != null) {
			if (modetest) {
				if (texte.length() == 0) {
					test += texte + "\n";
					continue;
				}
				test += texte + "\n";
				count++;
				continue;
			}
			if (fintrain) {
				if (texte.length() == 0) {
					modetest = true;
					fintrain = false;
					motsTrain = count;
					System.out.println("Constitution du corpus de test");
					continue;
				}
				train += texte + "\n";
				count++;
				continue;
			}
			if (count > nbmotstrain) {
				fintrain = true;
				train += texte + "\n";
				continue;
			}
			if (texte.length() == 0) {
				train += texte + "\n";
				continue;
			}
			train += texte + "\n";
			count++;
		}

		lecteur.close();
		count++;
		System.out.println("Ecriture de " + otrain + " et " + otest + "\n"
				+ "Nombre de mots en Train : " + (motsTrain) + "\n"
				+ "Nombre de mots en Test : " + (count - (motsTrain)));
		Tools.ecrire(otrain, train);
		Tools.ecrire(otest, test);
	}

	/**
	 * Methode pour parser un Tiger XML en fonction d'un XPath donné. Ne prend
	 * pas de namespace. Initialement fait pour l'ajout de béroul.
	 * 
	 * @date 16.12.2015
	 * @param xmlSource
	 * @return hashmap<string, string> ayant en clef l'id, et en valeur le POS
	 *         tag
	 * @throws Exception
	 */
	public static HashMap<String, String> parseTigerXml(String xmlSource,
			String xpath) throws Exception {
		HashMap<String, String> mapIdPos = new HashMap<String, String>();

		SAXBuilder jdomBuilder = new SAXBuilder();
		Document jdomDocument = jdomBuilder.build(xmlSource);
		XPathFactory xFactory = XPathFactory.instance();

		XPathExpression<Element> expr = xFactory.compile(xpath,
				Filters.element());
		List<Element> links = expr.evaluate(jdomDocument);
		for (Element linkElement : links) {
			mapIdPos.put(linkElement.getAttributeValue("id"),
					linkElement.getAttributeValue("pos"));
		}
		return mapIdPos;
	}

	/**
	 * Methode pour parser un TEI XML en fonction d'un XPath donné. Ne prend
	 * pas de namespace. Initialement fait pour l'ajout de béroul.
	 * 
	 * @date 16.12.2015
	 * @param xmlSource
	 * @return hashmap<string, string> ayant en clef l'id, et en valeur le POS
	 *         tag
	 * @throws Exception
	 */
	public static HashMap<String, String> parseTeiXml(String xmlSource) throws IOException, JDOMException {
		HashMap<String, String> mapIdPos = new HashMap<String, String>();
		org.jdom2.Document doc;
		Element rac;
		SAXBuilder sx = new SAXBuilder();
		doc = sx.build(new File(xmlSource));
		rac = doc.getRootElement();
		@SuppressWarnings("unused")
		Namespace tei = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		Namespace ns = Namespace.XML_NAMESPACE;

		XPathFactory xFac = XPathFactory.instance();
		XPathExpression<Element> exp = xFac.compile("//*[local-name()='w']",
				Filters.element()); 
		List<Element> lListe = exp.evaluate(rac);
		for (Element w : lListe) {
			mapIdPos.put(w.getAttributeValue("id", ns),
						w.getAttributeValue("type"));
		}
		return mapIdPos;
	}

	/**
	 * Methode pour parser un TEI XML en fonction d'un XPath donné. Ne prend
	 * pas de namespace. Initialement fait pour récupérer les données des TEI Lakme.
	 * 
	 * @date 16.12.2015
	 * @param xmlSource
	 * @return hashmap<string, string> ayant en clef l'id, et en valeur le POS
	 *         tag
	 * @throws Exception
	 */
	public static String parseTeiXmlLakme(String xmlSource) throws IOException, JDOMException {
		HashMap<String, String> mapIdPos = new HashMap<String, String>();
		String xmlContent = Tools.readFile(xmlSource);
		xmlContent = xmlContent.replaceAll("<lb/>", "<l><w>EMPTYLINE</w></l>");
		String xmlTemp = Tools.tempFile("xml", ".xml", xmlContent);
		
		StringBuilder sb = new StringBuilder();
		org.jdom2.Document doc;
		Element rac;
		SAXBuilder sx = new SAXBuilder();
		doc = sx.build(new File(xmlTemp));
		rac = doc.getRootElement();
		@SuppressWarnings("unused")
		Namespace tei = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		Namespace ns = Namespace.XML_NAMESPACE;

		
		XPathFactory xFac = XPathFactory.instance();
		XPathExpression<Element> exp = xFac.compile("//*[local-name()='l']", Filters.element());
		List<Element> lListe = exp.evaluate(rac);
		XPathExpression<Element> expW = xFac.compile("//*[local-name()='l']//*[local-name()='w']", Filters.element());
		List<Element> wliste = expW.evaluate(rac);
			for(Element w : wliste){
				if(w.getName().equals("w")){					
					XMLOutputter outp = new XMLOutputter();

//				    outp.setFormat(Format.getCompactFormat());
				    //outp.setFormat(Format.getRawFormat());
				    //outp.setFormat(Format.getPrettyFormat());
				    //outp.getFormat().setTextMode(Format.TextMode.PRESERVE);

				    StringWriter sw = new StringWriter();
				    outp.output(w.getContent(), sw);
				    StringBuffer sbuff = sw.getBuffer();
//				    System.out.println(sbuff.toString());
					sb.append(String.format("%s\t%s\n", w.getAttributeValue("id", ns), 
							sbuff.toString()));
				}

			}

			sb.append("\n");


		String str = sb.toString()
				.replaceAll("(?s)<orig[^>]*>.*?</orig>", "")
				.replaceAll("(?s)<add[^>]*>.*?</add>", "")
				.replaceAll("(?s)<corr[^>]*>.*?</corr>", "")
				.replaceAll("(?s)<del[^>]*>.*?</del>", "")
//				.replaceAll("(?s)<hi[^>]*>.*?</hi>", "")
				.replaceAll("(?s)<note[^>]*>.*?</note>", "")
				.replaceAll("(?s)<sic[^>]*>.*?</sic>", "")
				.replaceAll("(?s)<space[^>]*>.*?</space>", "")
//				.replaceAll("(?s)<unclear[^>]*>.*?</unclear>", "")
				.replaceAll("(?s)<abbr[^>]*>.*?</abbr>", "")
				.replaceAll("<[^>]*>", "")
				.replaceAll("null\tEMPTYLINE", "")
				;
		StringBuilder res = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		List<String> lines = Tools.StringToList(str);
		int id = 1;
		for(String line : lines){
			if(line.length() == 0){
				res.append("\n");
				id = 1;
				continue;
			}
			String[] tsv16 = new String[]{"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"};
			String[] cols = line.split("\t");
//			if((line.length() != 0)&&(cols.length < 2))System.out.println(line);
			tsv16[0] = cols[0];
			tsv16[1] = String.valueOf(id);
			tsv16[2] = cols[1];
			id++;
			res.append(joiner.join(tsv16)+"\n");
		}
		return res.toString();
	}
	
	
	/**
	 * Methode pour parser un TEI XML en fonction d'un XPath donné. Ne prend
	 * pas de namespace. Initialement fait pour récupérer les données des TEI Lakme.
	 * Méthode modifiée pour prendre en compte les nouveaux fichiers de JBC contenant les gold pos (ana) 
	 * et gold lemma (lemma)
	 * 
	 * @date 03.02.2016
	 * @param xmlSource
	 * @return hashmap<string, string> ayant en clef l'id, et en valeur le POS
	 *         tag
	 * @throws Exception
	 */
	public static String parseTeiXmlLakmeGPosGLemma(String xmlSource) throws IOException, JDOMException {
		HashMap<String, String> mapIdPos = new HashMap<String, String>();
		String xmlContent = Tools.readFile(xmlSource);
		xmlContent = xmlContent.replaceAll("<lb/>", "<l><w>EMPTYLINE</w></l>");
		String xmlTemp = Tools.tempFile("xml", ".xml", xmlContent);
		
		StringBuilder sb = new StringBuilder();
		org.jdom2.Document doc;
		Element rac;
		SAXBuilder sx = new SAXBuilder();
		doc = sx.build(new File(xmlTemp));
		rac = doc.getRootElement();
		@SuppressWarnings("unused")
		Namespace tei = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		Namespace ns = Namespace.XML_NAMESPACE;

		
		XPathFactory xFac = XPathFactory.instance();
		XPathExpression<Element> exp = xFac.compile("//*[local-name()='l']", Filters.element());
		List<Element> lListe = exp.evaluate(rac);
		XPathExpression<Element> expW = xFac.compile("//*[local-name()='l']//*[local-name()='w']", Filters.element());
		List<Element> wliste = expW.evaluate(rac);
			for(Element w : wliste){
				if(w.getName().equals("w")){					
					XMLOutputter outp = new XMLOutputter();

//				    outp.setFormat(Format.getCompactFormat());
				    //outp.setFormat(Format.getRawFormat());
				    //outp.setFormat(Format.getPrettyFormat());
				    //outp.getFormat().setTextMode(Format.TextMode.PRESERVE);

				    StringWriter sw = new StringWriter();
				    outp.output(w.getContent(), sw);
				    StringBuffer sbuff = sw.getBuffer();
//				    System.out.println(sbuff.toString());
				    
				    String ana = w.getAttributeValue("ana");
				    int count = StringUtils.countMatches(ana, "#");
				    if(count > 1){
				    	ana = ana.split(" ")[0];
				    }
					sb.append(String.format("%s\t%s\t%s\t%s\n", w.getAttributeValue("id", ns), 
							sbuff.toString(), w.getAttributeValue("lemma"), ana));
				}

			}

			sb.append("\n");


		String str = sb.toString()
				.replaceAll("(?s)<orig[^>]*>.*?</orig>", "")
				.replaceAll("(?s)<add[^>]*>.*?</add>", "")
				.replaceAll("(?s)<corr[^>]*>.*?</corr>", "")
				.replaceAll("(?s)<del[^>]*>.*?</del>", "")
//				.replaceAll("(?s)<hi[^>]*>.*?</hi>", "")
				.replaceAll("(?s)<note[^>]*>.*?</note>", "")
				.replaceAll("(?s)<sic[^>]*>.*?</sic>", "")
				.replaceAll("(?s)<space[^>]*>.*?</space>", "")
//				.replaceAll("(?s)<unclear[^>]*>.*?</unclear>", "")
				.replaceAll("(?s)<abbr[^>]*>.*?</abbr>", "")
				.replaceAll("<[^>]*>", "")
				.replaceAll("null\tEMPTYLINE", "")
				;
		System.out.println(str);
		StringBuilder res = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		List<String> lines = Tools.StringToList(str);
		int id = 1;
		for(String line : lines){
			if((line.length() == 0)||(line.contains("null"))){
				res.append("\n");
				id = 1;
				continue;
			}
			String[] tsv16 = new String[]{"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"};
			String[] cols = line.split("\t");
//			if((line.length() != 0)&&(cols.length < 2))System.out.println(line);
			tsv16[0] = cols[0];
			tsv16[1] = String.valueOf(id);
			tsv16[2] = cols[1];
			tsv16[3] = cols[2];
			tsv16[5] = cols[3].replace("#", "");
			id++;
			res.append(joiner.join(tsv16)+"\n");
		}
		return res.toString();
	}
	
	
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String parseTeiLakmeRegex(String path) throws Exception{
		String re1 = "(?s)<w[ >].*?(.+?)</w>";
		String re = "(?s)<w[^>]*>.*?</w>";
		String re3 = "(?s)<orig[^>]*>.*?</orig>";
		String balises = "<[^>]*>";
		String xml = Tools.readFile(path);
		xml = xml.replaceAll(re3, "")
				.replaceAll("(?s)<orig[^>]*>.*?</orig>", "")
				.replaceAll("(?s)<add[^>]*>.*?</add>", "")
				.replaceAll("(?s)<corr[^>]*>.*?</corr>", "")
				.replaceAll("(?s)<del[^>]*>.*?</del>", "")
//				.replaceAll("(?s)<hi[^>]*>.*?</hi>", "")
				.replaceAll("(?s)<note[^>]*>.*?</note>", "")
				.replaceAll("(?s)<sic[^>]*>.*?</sic>", "")
				.replaceAll("(?s)<space[^>]*>.*?</space>", "")
//				.replaceAll("(?s)<unclear[^>]*>.*?</unclear>", "")
				.replaceAll("(?s)<abbr[^>]*>.*?</abbr>", "")
				.replaceAll("rend=\".+\"", "")
				.replaceAll("<lb/>", "\n\n")
				
				;
//		replaceAll("<[^>]*>", "");
		final Pattern pattern = Pattern.compile(re);
		final Matcher matcher = pattern.matcher(xml);
		matcher.find();
		StringBuilder sb = new StringBuilder();
//		sb.append(matcher.groupCount()+"\n");
//		for(int i = 0; i < matcher.groupCount() ; i++){
//			sb.append(matcher.group() + "\n");
//		}
		while(matcher.find()){
			sb.append(matcher.group() + "\n");
		}
		String data = sb.toString();
		data = data.replaceAll("<w xml:id=\"", "").replaceAll("\">", "\t")
				.replaceAll("\" >", "\t")
				.replaceAll("\" />", "")
//				.replaceAll("\" rend=", "")
				;
		data = data.replaceAll(balises, "");
		
		return data;
	}

	/**
	 * Injecte une liste de valeurs dans une colonne d'un tsv. ex: remplace tous les lemmes, etc.
	 * @param conllContent
	 * @param colToPut La liste ne doit pas contenir de "\t" dans ses éléments !
	 * @param colIndexToRemplace
	 * @return le nouveau tsv avec la colonne modifiée
	 * @throws Exception
	 */
	public static String putValueInConll(String conllContent, List<String> colToPut, int colIndexToRemplace) throws Exception{
		List<String> lines = Tools.StringToList(conllContent);
		StringBuilder sb = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		int index = 0;
		int current = 0;
		int total = lines.size();
		
		for(String line : lines){
			current++;
			Tools.printProgress(total, current);
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			String[] cols = line.split("\t");
			cols[colIndexToRemplace] = colToPut.get(index);
			index++;
			sb.append(joiner.join(cols) + "\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * A partir d'une hash map <String (id), String (pos)> injecte les pos dans un conll en se basant sur les id 
	 * en position 0. Peut également être utilisé pour n'importe quelle valeur à injecter en lui donnant la bonne colonne. 
	 * L'id est supposé être en colonne 0. Changer cela si nécessaire.
	 * Fait initialement pour injecter les pos obtenus par parseTigerXml() pour Béroul
	 * @date 16.12.2015
	 * @param conllContent
	 * @param mapIdValue
	 * @return
	 * @throws Exception
	 */
	public static String putValueInConll(String conllContent,
			HashMap<String, String> mapIdValue, int colnb, boolean incompleteId ) throws Exception {
		System.out.println("\nPut value in conll\t"+Tools.time());
		List<String> listConll = Tools.StringToList(conllContent);
		List<String> listLignesConll = new ArrayList<String>();
		int current = 0;
		int total = listConll.size();
		if(incompleteId){
			for (int i = 0; i < listConll.size(); i++) {
				current ++;
				Tools.printProgress(total, current);
				if (listConll.get(i).length() == 0) {
					listLignesConll.add("\n");
					continue;
				}
				String[] cols = (listConll.get(i)).split("\t");
//				mapIdValue.get(cols[0]);
				String[] cutId = cols[0].split("#");
				cols[colnb] = mapIdValue.get(cutId[1]);
				StringBuilder ligneConll = new StringBuilder();
				for (int j = 0; j < cols.length; j++) {
					if (j == cols.length - 1) {
						ligneConll.append(cols[j]);
						continue;
					}
					ligneConll.append(cols[j] + "\t");
				}
				listLignesConll.add(ligneConll.toString());
			}
		}else{
			for (int i = 0; i < listConll.size(); i++) {
				current ++;
				Tools.printProgress(total, current);
				if (listConll.get(i).length() == 0) {
					listLignesConll.add("\n");
					continue;
				}
				String[] cols = (listConll.get(i)).split("\t");
	//			mapIdValue.get(cols[0]);
				cols[colnb] = mapIdValue.get(cols[0]);
				StringBuilder ligneConll = new StringBuilder();
				for (int j = 0; j < cols.length; j++) {
					if (j == cols.length - 1) {
						ligneConll.append(cols[j]);
						continue;
					}
					ligneConll.append(cols[j] + "\t");
				}
				listLignesConll.add(ligneConll.toString());
			}
		}
		//injection du contenu dans un string
		System.out.println("\nProcessing new String conll");
		total = listLignesConll.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < listLignesConll.size(); i++) {
			Tools.printProgress(total, i);
			String line = listLignesConll.get(i);
			if (line.equals("\n")) {
				sb.append(line);
				continue;
			}
			sb.append(line + "\n");
		}
		return sb.toString();
	}
	
		

	/**
	 * Permet d'obtenir une hashmap à partir d'un conll. Clef = id (colonne 0) et Valeur = Forme (colonne 2)
	 * @param conll content
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	public static HashMap<String, String> getMapIdValue(String conll, int rowId, int rowValue) throws Exception{
		System.out.println("getMapIdValue"+Tools.time());
		List<String> lines = Tools.StringToList(conll);
		HashMap<String, String> map = new HashMap<String, String>();
		int total = lines.size();
		int current = 0;
		for(String line : lines){
			current++;
			Tools.printProgress(total, current);
			if(line.length() == 0)continue;
			String[] cols = line.split("\t");
			map.put(cols[rowId], cols[rowValue]);
		}
		return map;
	}
	
	/**
	 * From a conll or other, return each sentence in a String. Then gives you the list
	 * of these sentences.
	 * @param conllPath
	 * @return List<String> sentences
	 * @throws Exception
	 */
	public static List<String> getSentences(String conllPath) throws Exception{
		File fichier = new File(conllPath);
		BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
		String texte = lecteur.toString();
		StringBuilder phrase = new StringBuilder();
		List<String> phrases = new ArrayList<String>();

		while ((texte = lecteur.readLine()) != null) {
			if (texte.length() == 0) {
				phrases.add(phrase.toString());
				phrase = new StringBuilder();
				continue;
			}
			phrase.append( texte + "\n" );
		}
		lecteur.close();
		if(phrase.length() != 0)
		phrases.add(phrase.toString());
		return phrases;
	}

	/**
	 * A sample usage method to obtain word order from conll.
	 * @param path
	 * @throws Exception
	 */
	public static void countOrders(String path) throws Exception{
		File fichier = new File(path);
		BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
		String texte = lecteur.toString();
		String phrase = "";
		List<String> phrases = new ArrayList<String>();
	
		while ((texte = lecteur.readLine()) != null) {
			if (texte.length() == 0) {
				phrases.add(phrase);
				phrase = "";
				continue;
			}
			phrase += texte + "\n";
		}
		lecteur.close();
		//29 et li baron regardoient les letres
		phrase = phrases.get(50);
		List<String> elements = Tools.StringToList(phrase);
		String order = "";
		for(int i =0; i < elements.size(); i++){
			String[] cols = elements.get(i).split("\t");
			if(cols[11].contains("SjPer"))order += "S";
			if(cols[5].contains("VERcjg"))order+="V";
			if(cols[11].contains("Cmpl"))order += "C";
			if(cols[11].contains("Obj"))order += "C";
		}
		System.out.println(phrase);
		System.out.println(order);
	}
	
	/**
	 * get sub corpus into a combined one by matching a string contained in the [col] column.
	 * Example: a corpus contains Yvain and beroul, you want to retrieve beroul : "beroul" as matcher.
	 * @param path
	 * @param matcher
	 * @param col
	 * @return
	 * @throws Exception
	 */
	public static String getSubCorpus(String path, String matcher, int col)throws Exception{
		List<String> lines = Tools.path2liste(path);
		StringBuilder sb = new StringBuilder();
		boolean mode = false;
		for (String line : lines) {
			if (mode) {
				if (line.length() == 0) {
					sb.append("\n");
					continue;
				}
				String[] cols = line.split("\t");
				if (!(cols[col].contains(matcher))) {
					mode = false;
				}else{
					sb.append(line + "\n");
				}
			}else{
				if (line.length() == 0) {
					continue;
				}
				String[] cols = line.split("\t");
				if (cols[col].contains(matcher)) {
					mode = true;
					sb.append(line + "\n");
				}
			}
		}
		return sb.toString();
	}
}
