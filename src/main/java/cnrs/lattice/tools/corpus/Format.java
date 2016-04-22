package cnrs.lattice.tools.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import cnrs.lattice.models.Word;
import cnrs.lattice.tools.utils.Tools;

import com.google.common.base.Joiner;

public class Format {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * String path =
		 * "/home/gael/Documents/TLT/MATE/S/TLT/Yvainx2/MateGoldCorpusYvainLVdiv.test.conll"
		 * ; String output =
		 * "/home/gael/Documents/TLT/MATE/S/TLT/Yvainx2/MateGoldCorpusYvaindiv_sans_lemme.test.conll"
		 * ; String mate_sans_lemme = Format_sans_lemme(path);
		 * TLT14.Outils.ecrire(output, mate_sans_lemme);
		 */

		/*
		 * String prose_enrichi = TLT14.TLTFormat.FormatMate("predicted",
		 * "/home/gael/Documents/Wapiti/S/TALN/Prose/CorpusProse.conll",
		 * "/home/gael/Documents/Wapiti/R/TALN/vers_sur_prose"); String
		 * vers_enrichi = TLT14.TLTFormat.FormatMate("predicted",
		 * "/home/gael/Documents/Wapiti/S/TALN/Vers/CorpusVers.conll",
		 * "/home/gael/Documents/Wapiti/R/TALN/prose_sur_vers");
		 * TLT14.Outils.ecrire
		 * ("/home/gael/Documents/TLT/MATE/S/TALN/mate_prose_ppos.conll",
		 * prose_enrichi); TLT14.Outils.ecrire(
		 * "/home/gael/Documents/TLT/MATE/S/TALN/mate_vers_ppos.conll",
		 * vers_enrichi);
		 */

		// FormatWapiti("/home/gael/Documents/TLT/TALN/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/CorpusSRCMF.conll",
		// "/home/gael/Documents/Wapiti/S/TALN/corpusCONLL.wapiti");

		
//		String[] values = { "yo", "yop", "yeah" };
//		Joiner joiner = Joiner.on("\t").skipNulls();
//		String str = joiner.join(values) + "\n";
//		System.out.println(str);
		
		String path = "/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/CorpusCoinci.conll";
		System.out.println(conll2Wapiti(path));

	}

	
	/**
	 * Prend un fichier conll du SRCMF et retourne la version pour Wapiti
	 * @param conllPath
	 * @return Format Wapiti du fichier Conll
	 * @throws Exception
	 */
	public static String conll2Wapiti(String conllPath) throws Exception{
		List<String> lines = Tools.path2liste(conllPath);
		String formatWapiti = "";
		for(String line : lines){
			String[] values = line.split("\t");
			Joiner joiner = Joiner.on("\t");
			
			if (line.length() == 0
					|| values[0]
							.contains("## ERROR: sentence cannot be parsed. Please check annotation!")
					|| values[0].equals("")) {
				if (line.length() == 0)
					formatWapiti += "\n";
					continue;
			}
			
			String[] valuesWapiti = ArrayUtils.toArray(values[2], values[3], values[4], values[8], values[10], 
					values[6], values[12], values[5]);
			formatWapiti += joiner.join(valuesWapiti) + "\n";
		}
		return formatWapiti;
	}
	
	/**
	 * Prend un fichier conll du SRCMF et retourne la version pour Marmot
	 * @param conllPath
	 * @return Format Marmot du fichier Conll
	 * @throws Exception
	 */
	public static String conll2Marmot(String conllPath, boolean feats) throws Exception{
		List<String> lines = Tools.path2liste(conllPath);
		String formatMarmot = "";
		for(String line : lines){
			String[] values = line.split("\t");
			Joiner joiner = Joiner.on("\t");
			
			if (line.length() == 0
					|| values[0]
							.contains("## ERROR: sentence cannot be parsed. Please check annotation!")
					|| values[0].equals("")) {
				if (line.length() == 0)
					formatMarmot += "\n";
					continue;
			}
			if(feats){
				String[] valuesMarmot = ArrayUtils.toArray(values[1], values[2], values[7], values[5]);
				formatMarmot += joiner.join(valuesMarmot) + "\n";
			}else{
				String[] valuesMarmot = ArrayUtils.toArray(values[1], values[2], values[5]);
				formatMarmot += joiner.join(valuesMarmot) + "\n";
			}
		}
		return formatMarmot;
	}
	
	
	/**
	 * Prend un fichier Mate et retourne son String en format Malt
	 * @param conllPath
	 * @return Format Malt du fichier Mate.
	 * @throws Exception
	 */
	public static String mate2Malt(String conllPath) throws Exception{
		List<String> lines = Tools.path2liste(conllPath);
		String formatMalt = "";
		for(String line : lines){
			String[] values = line.split("\t");
			Joiner joiner = Joiner.on("\t");
			
			if (line.length() == 0
					|| values[0]
							.contains("## ERROR: sentence cannot be parsed. Please check annotation!")
					|| values[0].equals("")) {
				if (line.length() == 0)
					formatMalt += "\n";
					continue;
			}
			
			String lemmepropre = values[3].replaceAll("_.*$", "");
			String[] valuesWapiti = ArrayUtils.toArray(values[0], values[1], lemmepropre, "_", values[5],
					"_", values[8], values[10], values[9], values[11]);
			formatMalt += joiner.join(valuesWapiti) + "\n";
		}
		return formatMalt;
	}
	
	
	/**
	 * Prend un fichier Conll et retourne son String en format Malt.
	 * @param conllPath
	 * @return Format Malt du fichier Conll.
	 * @throws Exception
	 */
	public static String conll2MaltPredicted(String conllPath) throws Exception{
		List<String> lines = Tools.path2liste(conllPath);
		String formatMalt = "";
		for(String line : lines){
			String[] values = line.split("\t");
			Joiner joiner = Joiner.on("\t");
			
			if (line.length() == 0
					|| values[0]
							.contains("## ERROR: sentence cannot be parsed. Please check annotation!")
					|| values[0].equals("")) {
				if (line.length() == 0)
					formatMalt += "\n";
					continue;
			}
			String[] valuesMalt = ArrayUtils.toArray(values[1], values[2], values[4], "_", values[6],
					"_", values[10], values[12], values[10], values[12]);
			formatMalt += joiner.join(valuesMalt) + "\n";
		}
		return formatMalt;
	}
	
	
	/**
	 * Permet de transformer un résultat de mate en conll complet.
	 * Concrètement, se contente d'ajouter les identifier en première colonne.
	 * @param materesPath
	 * @param conllPath
	 * @param out
	 * @throws Exception
	 */
	public static void materes2conllComplet(String materesPath, String conllPath, String out) throws Exception{
		String content = Tools.readFile(materesPath);
		String content1 = Tools.remplaceColValue(content, "_", 8);
		List<String> lines = Tools.path2liste(conllPath);
		List<String> ids = Tools.ListeRedondante(lines, 0);
		String content2 = Tools.addColValue(content1);
		String content3 = Tools.remplaceColValue(content2, ids, 0);
		Tools.ecrire(out, content3);
		System.out.println(content3);
	}
	//////////////////////////////////////////////////
	
	// conll2Mate ; conll2Malt ; malt2Mate ; ... 
	
	//////////////////////////////////////////////////////
	
	
	public static void insertDataInWords(List<Word> words, List<Word> wordsToInsert) throws Exception{
		for(Word word : words){
			
		}
	}
	
	/**
	 * Ajoute au Mate les POS de Wapiti dans la case PPos.
	 * 
	 * @param conllMorpho
	 *            Le CONLL contenant les morpho.
	 * @return Le fichier wapiti contenant les morpho et les lemmes.
	 * @throws IOException
	 */
	public static String putPosWapitiToMate(String mate, String wapitiResults) throws IOException {
		String resultat = "";
		List<String> linesWapiti = Tools.StringToList(wapitiResults);
		List<String> linesMate = Tools.StringToList(mate);
		Iterator<String> itWapiti = linesWapiti.iterator();
		Iterator<String> itMate = linesMate.iterator();
		int total = linesMate.size();
		int current = 0;
		
		while (itMate.hasNext() && itWapiti.hasNext()) {
		   current++;
		   Tools.printProgress(total, current);
		   String lineWapiti = itMate.next();
		   String lineMate = itMate.next();
		   if ((lineMate.length() == 0)) {
				resultat += "\n";
				continue;
			}

			String[] colsMate = lineMate.split("\t");
			String[] colsWapiti = lineWapiti.split("\t");
			int der = colsWapiti.length - 1;
			colsMate[5] = colsWapiti[der];
			Joiner joiner = Joiner.on("\t");
			resultat += joiner.join(colsMate) + "\n";
		}		
		return resultat;
	}

	/**
	 * Ajoute au Mate les POS de Wapiti dans la case PPos.
	 * @param mate
	 * @param wapitiOutput
	 * @return le String au format Mate contenant les PPos de Wapiti
	 * @throws IOException
	 */
	public static String POSWapitiToMate(String mate, String wapitiOutput) throws IOException {
			StringBuilder resultat = new StringBuilder();
			
			BufferedReader lecteur = Tools.StringToBufferedReader(mate);
			BufferedReader lecteurWapiti = Tools.StringToBufferedReader(wapitiOutput);
			String texteMate = lecteur.toString();
			String texteWapiti = lecteurWapiti.toString();

			while (((texteMate = lecteur.readLine()) != null)
					&& ((texteWapiti = lecteurWapiti.readLine()) != null)) {

				if ((texteMate.length() == 0)) {

					resultat.append("\n");
					// i++;
					continue;
				}

				String[] colsMate = texteMate.split("\t");
				String[] colsWapiti = texteWapiti.split("\t");
				int der = colsWapiti.length - 1;
				int avantDer = colsWapiti.length -2;
				if((colsWapiti[der].length() == 0)||(colsWapiti[der].equals("_"))){
					colsMate[5] = "_";
				}else{
					colsMate[5] = colsWapiti[der];
				}
				if((colsWapiti[avantDer].length() == 0)||(colsWapiti[avantDer].equals("_"))){
					colsMate[4] = "_";
				}else{
					colsMate[4] = colsWapiti[avantDer];
				}
				Joiner joiner = Joiner.on("\t");
				resultat.append(joiner.join(colsMate) + "\n");
			}

			lecteurWapiti.close();
		
		return resultat.toString();

	}

	
}
