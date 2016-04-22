package cnrs.lattice.tools.corpus;

import java.io.File;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import se.vxu.msi.malteval.MaltEvalConsole;
import cnrs.lattice.engines.readers.Reader;
import cnrs.lattice.engines.readers.Writer;
import cnrs.lattice.models.Word;
import cnrs.lattice.tools.utils.Tools;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;

public class Error {
	
	static HashMap<String, Integer> typoWrong = new HashMap<String, Integer>();
	static HashMap<String, Integer> typoGood = new HashMap<String, Integer>();
	static List<String> types = new ArrayList<String>();
	static StringBuilder count = new StringBuilder();
	
	
	public static void main(String[] args) throws Exception{
		String path = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/beroul_ON_lapidaire.mate";
		
		String content = Tools.readFile(path);
		List<Word> words = new Reader().readMate(content);
		content = new Writer().toMaltComplete(words, true);
		String pathMalt = Tools.tempFile("temp", ".temp", content);

//		String malt = Format.mate2Malt(path);
//		System.out.println(malt);
		//String goldMalt = switch2Gold(malt);
		//startMaltEval(Tools.tempFile("tempgold", ".temp", goldMalt), Tools.tempFile("temp", ".temp", malt));
		isolateLasError(pathMalt, "Obj");
		
//		content= Tools.tempFile("temp", ".temp", Fixer.fixIgnorerLine(content));
		
		
//		phase1("/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/graalloo/sgraal_ON_graal.mate",
//				"/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/old/graal_before_fixIgnorer.conll"
//				);
//		Tools.ecrire("temp2", getContexts("temp", 1));
//		Tools.ecrire("temp3", tagErrors("temp2", 5, 6, 7, 9, 8, 10));
//		StringBuilder sb = new StringBuilder();		
//		sb.append(getAccuracyError(Tools.readFile("temp3"), 5, 6));
//		sb.append(getAccuracyError(Tools.readFile("temp3"), 7, 9));
//		sb.append(getAccuracyError(Tools.readFile("temp3"), 8, 10));
//		Tools.ecrire("temp4", sb.toString());
//		
//		StringBuilder sb2 = new StringBuilder();
//		sb2.append("Tag\tAccuracy\tCorrects\tTotal\n");
//		sb2.append(getTagsAccuracy("temp4", 5, 6) + "\n");
//		sb2.append(getTagsAccuracy("temp4", 7, 9) + "\n");
//		sb2.append(getTagsAccuracy("temp4", 8, 10) + "\n");
//		sb2.append(getTagsLAS("temp4", 7, 8, 9, 10)+ "\n");
//		Tools.ecrire("temp5", sb2.toString());
		
	//test2
//		Tools.ecrire("temp3", tagErrors("temp", 4, 5, 8, 9, 10, 11));
//		StringBuilder sb = new StringBuilder();		
//		sb.append(getAccuracyError(Tools.readFile("temp3"), 5, 6));
//		sb.append(getAccuracyError(Tools.readFile("temp3"), 7, 9));
//		sb.append(getAccuracyError(Tools.readFile("temp3"), 8, 10));
//		Tools.ecrire("temp4", sb.toString());
		
//		StringBuilder sb2 = new StringBuilder();
//		sb2.append("Tag\tAccuracy\tCorrects\tTotal\n");
//		sb2.append(getTagsAccuracy("temp4", 5, 6) + "\n");
//		sb2.append(getTagsAccuracy("temp4", 7, 9) + "\n");
//		sb2.append(getTagsAccuracy("temp4", 8, 10) + "\n");
//		sb2.append(getTagsLAS("temp4", 7, 8, 9, 10)+ "\n");
//		Tools.ecrire("temp5", sb2.toString());
		
		
		
		/*
		 * 
		 */
//		String graalFixed = Fixer.fixIgnorerLineGraal("/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/old/zCorpusGraal.conll");
//		graalFixed = Fixer.fixIgnorerLineGraal(Tools.tempFile("graal", ".txt",graalFixed));
//		Tools.ecrire("graalFixed", graalFixed);
		
		/*// verif manque pos tag gold 
		List<String> posTagsFil = Tools.path2liste("/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/"
				+ "lapidaire"
				+ ".conll");
		int nb = 0;
		for(String line : posTagsFil){
			if(line.length() == 0)continue;
			String[] cols = line.split("\t");
			if(cols[5].equals("_")){
				System.out.println(line);
				nb++;
			}
		}
		System.out.println(nb);
		*/
		
			}
	
	/**
	 * Retourne les étiquettes d'une colonne et calcul pour chacune, leur accuracy
	 * @param path
	 * @param actual
	 * @param predicted
	 * @return les resultats en pourcentage de chaque etiquette
	 * @throws Exception
	 */
	public static String getTagsAccuracy(String path, int actual, int predicted) throws Exception{
		String content = Tools.readFile(path);
		Stopwatch stopwatch = Stopwatch.createStarted();
		List<String> posTags = Tools.getColumnElements(content, actual);		
		List<String> lines = Tools.path2liste(path);
		
		int current = 0;
		int fin = posTags.size();
		StringBuilder sb = new StringBuilder();
		for(String tag :  posTags){
			current++;
			Tools.printProgress(fin, current);
			
			float hits = 0;
			float total = 0;
			int index = 0;
			for(String line : lines){
				index ++;
				if(line.length() == 0)continue;
				String[] cols = line.split("\t");
				if(cols[actual].equals(tag)){
					total++;
					if(cols[actual].equals(cols[predicted])){
						hits++;
					}
				}

			}
			if(total == 0 ){
				sb.append(tag + "\tnone\n");
			}else{
				DecimalFormat df = new DecimalFormat("#.##");
				df.setRoundingMode(RoundingMode.HALF_UP);
				String tagRes = String.format("%s\t%s\t%s\t%s\n", tag , df.format((hits / total) * 100), hits, total);
				sb.append(tagRes);
			}
		}
		System.out.println(stopwatch);
		stopwatch.stop();
		return sb.toString();
	}
	
	/**
	 * Retourne les étiquettes de deux colonnes et calcul pour chacune, leur accuracy. 
	 * @param path
	 * @param actualHead
	 * @param actualDeprel
	 * @param predictedHead
	 * @param predictedDeprel
	 * @return
	 * @throws Exception
	 */
	public static String getTagsLAS(String path, int actualHead, int actualDeprel, 
			int predictedHead, int predictedDeprel) throws Exception{
		Stopwatch stopwatch = Stopwatch.createStarted();
		String content = Tools.readFile(path); 
		List<String> headdeprelTags = Tools.getColumnElements(content, actualHead, actualDeprel);		
		List<String> lines = Tools.path2liste(path);
		
		StringBuilder sb = new StringBuilder();
		int current = 0;
		int fin = headdeprelTags.size();
		System.out.println(fin);
		
		for(String tag : headdeprelTags ){
			current++;
			Tools.printProgress(fin, current);
			
			float hits = 0;
			float total = 0;
			int index = 0;
			for(String line : lines){
				index ++;
				if(line.length() == 0)continue;
				String[] cols = line.split("\t");
				String headdeprel = cols[actualHead] + "\t" + cols[actualDeprel];
				if( headdeprel.equals(tag)){
					total++;
					if(cols[actualHead].equals(cols[predictedHead]) && (cols[actualDeprel].equals(cols[predictedDeprel]))){
						hits++;
					}
				}

			}
			if(total == 0 ){
				sb.append(tag + "\tnone\n");
			}else{
				DecimalFormat df = new DecimalFormat("#.##");
				df.setRoundingMode(RoundingMode.HALF_UP);
				String tagRes = String.format("%s\t%s\t%s\t%s\n", tag , df.format((hits / total) * 100), hits, total);
				sb.append(tagRes);
			}
		}
		System.out.println(stopwatch);
		stopwatch.stop();
		return sb.toString();
	}
	
	/**
	 * Etiquette les types d'erreurs en ajoutant 4 colonnes : 
	 * EPOS , EHEAD, ESYN, ELAS.
	 * Fomat d'entree : GPOS(5) PPOS(6) GHEAD(7) GSYN(8) PHEAD(9) PSYN(10)  
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String tagErrors(String path, int pos, int ppos, int head, int phead, int syn, int psyn) throws Exception{
		List<String> lines = Tools.path2liste(path);
		StringBuilder sb = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		for(String line : lines){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			String EPOS = "_";
			String EHEAD = "_";
			String ESYN = "_";
			String ELAS = "_";
			String[] cols = line.split("\t");
			if(!cols[pos].equals(cols[ppos])){
				EPOS = "ErrorType : " + cols[pos];
			}
//			if(!cols[head].equals(cols[phead])){
//				EHEAD = "ErrorType : " + cols[head];
//			}
			if(( !cols[syn].equals(cols[psyn])) && (cols[head].equals(cols[phead])) ){
				ESYN = "ErrorType : " + cols[syn];
			}
//			if((!cols[8].equals(cols[10]))&&(!cols[7].equals(cols[9]))){
//				ELAS = "ErrorType : LAS";
//			}
			sb.append(String.format("%s\t%s\t%s\n", joiner.join(cols), EPOS, ESYN));
		}
		return sb.toString();
	}
	
	/**
	 * Ajoute les contextes droits et gauches autour de la colonne forme.
	 * @param path
	 * @param formCol La colonne où se trouvent les formes
	 * @return
	 * @throws Exception
	 */
	public static String getContexts(String path, int formCol) throws Exception{
		List<String> lines = Tools.path2liste(path);
		List<String> forms = Tools.ListeRedondante(lines, formCol);
		int i = 0;
		StringBuilder sb = new StringBuilder();
		Joiner joinerC = Joiner.on(" ");
		Joiner joiner = Joiner.on("\t");
		for(String line : lines){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			int j = i-1;
			 
			List<String> left = new ArrayList<String>();
			while(( j > (i-15))&&(j > -1)&&(j < forms.size())){
				left.add(forms.get(j));
				j--;
			}
			
			int k = i+1;
			List<String> right = new ArrayList<String>();
			while(( k < (i+15))&&(k < forms.size())){
				right.add(forms.get(k));
				k++;
			}
			Collections.reverse(left);
			String[] cols = line.split("\t");
			String[] newCols = {cols[0], joinerC.join(left), cols[1], joinerC.join(right), cols[3], 
					cols[4], cols[5], cols[8], cols[10], cols[9], cols[11]};
			sb.append(joiner.join(newCols) + "\n" );
			i++;
			
		}
		return sb.toString();
	}
	
	
	/**
	 * Ajoute les contextes droits et gauches autour de la colonne forme.
	 * Sort un format independant decelui en entree
	 * @param path
	 * @param formCol La colonne où se trouvent les formes
	 * @return
	 * @throws Exception
	 */
	public static String getContextIndependent(String path, int formCol) throws Exception{
		List<String> lines = Tools.path2liste(path);
		List<String> forms = Tools.ListeRedondante(lines, formCol);
		int i = 0;
		StringBuilder sb = new StringBuilder();
		Joiner joinerC = Joiner.on(" ");
		Joiner joiner = Joiner.on("\t");
		for(String line : lines){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			int j = i-1;
			 
			List<String> left = new ArrayList<String>();
			while(( j > (i-15))&&(j > -1)&&(j < forms.size())){
				left.add(forms.get(j));
				j--;
			}
			
			int k = i+1;
			List<String> right = new ArrayList<String>();
			while(( k < (i+15))&&(k < forms.size())){
				right.add(forms.get(k));
				k++;
			}
			Collections.reverse(left);
			String[] cols = line.split("\t");
//			String[] newCols = {cols[0], joinerC.join(left), cols[1], joinerC.join(right), cols[3], 
//					cols[4], cols[5], cols[8], cols[10], cols[9], cols[11]};
			int index = 0;
			List<String> newCols = new ArrayList<String>();
			for(String col : cols){
				if(index == (formCol -1)){
					newCols.add(col);
					newCols.add(joinerC.join(left));
					index++;
					continue;
				}
				if(index == (formCol +1)){
					newCols.add(joinerC.join(right));
					newCols.add(col);
					index++;
					continue;
				}
				newCols.add(col);
				index++;
			}
			sb.append(joiner.join(newCols) + "\n" );
			i++;
			
		}
		return sb.toString();
	}
	
	/**
	 * inject gold values and complete mate res
	 * dependant and not great reusability
	 * @TODO refactor this
	 * @throws Exception
	 */
	public static void phase1(String materes, String goldconll) throws Exception{
//		String graalLOO = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/graalloo/sgraal_ON_graal.mate";
		String content = Fixer.fixSyntaxDuplicataAndAnnotatorTag(Tools.readFile(materes), 10);
		content = Fixer.fixSyntaxDuplicataAndAnnotatorTag(content, 11);
//		String graalGold = "/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/graal.conll";
		List<String> linesGold = Tools.path2liste(goldconll);
		List<String> listGPOS = Tools.ListeRedondante(linesGold, 5);
		List<String> linesLOO = Tools.StringToList(content);
		int i = 0;
		StringBuilder sb = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		for(String line : linesLOO){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			String[] cols = line.split("\t");
			cols[4] = listGPOS.get(i);
			sb.append(joiner.join(cols) + "\n" );
			i++;
		}
		Tools.ecrire("temp", sb.toString());
	}
	
	
	/**
	 * launch malt eval as simply as with CLI
	 * @param path1
	 * @param path2
	 * @throws Exception
	 */
	public static void startMaltEval(final String path1, final String path2) throws Exception{
			String[] arguments = {"-s",path1,"-g",path2,"-v","1"};
			MaltEvalConsole.main(arguments);
	}
	
	/**
	 * tag each syn error, give accuracy for all of them, and 
	 * and laucnh malt with sentences containing the selected tag
	 * @param path
	 * @param tag
	 * @throws Exception
	 */
	public static void isolateError(String path, String tag) throws Exception{
		String pathOnly = FilenameUtils.getPath(path);
		String fileName = FilenameUtils.getBaseName(path);
		String content = Tools.readFile(path);
		List<Word> words = new Reader().readMate(content);
		content = new Writer().toMaltComplete(words, true);
		String pathMalt = Tools.tempFile("temp", ".temp", content);
		
		List<String> sentences = Corpus.getSentences(pathMalt);
		StringBuilder sb = new StringBuilder();
		for(String sentence : sentences){
			String errors = getAccuracyError(sentence,
					7, 9);
			if(errors.length() > 0){
				sb.append(errors +"\n");
			}
		}
		StringBuilder count = new StringBuilder();
		List<String> listTypes = Tools.ListeNonRedondante(types, 0);
//		System.out.println(types.toString());
		for(String type : listTypes){
			if(typoGood.get(type) == null){
				count.append(
						String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, "0 %"
						,typoWrong.get(type), "0")
						);
				continue;
			}
			if(typoWrong.get(type) == null){
				count.append(
						String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, "100 %"
						,"0", typoGood.get(type))
						);
				continue;
			}
			double good = typoGood.get(type);
			double bad = typoWrong.get(type);
			double total = good + bad;
			double percent = (good/total)*100;
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.HALF_UP);
//			System.out.println(df.format(0.912385)); //Le nombre sera arrondi à 0,91239.
//			System.out.println((typoWrong.get(type) / total)*100);
			count.append(
					String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, 
//							String.valueOf(type.length())
							df.format(percent)+" %"
							,typoWrong.get(type), typoGood.get(type))
					);
		}
		count.append("\n\n\n" + sb.toString());
		
		String out = String.format("/%s/ERRORS_%s.malt",pathOnly, fileName);
		Tools.ecrire(out,
				count.toString());
		
		List<String> sents = Corpus.getSentences(out);
		StringBuilder select = new StringBuilder();
		for(String sent : sents){
			List<String> lines = Tools.StringToList(sent);
			boolean contain = false;
			for(String line : lines){
				String[] cols =  line.split("\t");
				if((cols.length) > 10){
					String errorType = cols[10].replaceAll("ErrorType : ", "");
					if(errorType.equals(tag))contain = true;
				}
			}
			if(contain)select.append(sent+"\n");
		}
//		System.out.println(select.toString());
		
		Tools.ecrire(out+"_"+tag,select.toString());
		String temp = String.format("/%s/temp",pathOnly, fileName);
		String switched = switch2Gold(select.toString());
		Tools.ecrire(temp, switched);
		startMaltEval(out+"_"+tag, temp);
		File fi = new File(temp);
		Files.deleteIfExists(fi.toPath());
		fi = new File(out+"_"+tag);
		Files.deleteIfExists(fi.toPath());
	}
	
	
	/**
	 * tag each syn error, give accuracy for all of them, and 
	 * and laucnh malt with sentences containing the selected tag
	 * @param path
	 * @param tag
	 * @throws Exception
	 */
	public static String[] isolateLasError(String pathMalt, String tag) throws Exception{
		System.out.println("isolate started");
		List<String> sentences = Corpus.getSentences(pathMalt);
		StringBuilder sb = new StringBuilder();
		for(String sentence : sentences){
			String errors = getWrongSynError(sentence,
					6, 8, 7, 9);
			if(errors.length() > 0){
				sb.append(errors +"\n");
			}
		}
		StringBuilder count = new StringBuilder();
		List<String> listTypes = Tools.ListeNonRedondante(types, 0);
		for(String type : listTypes){
			if(typoGood.get(type) == null){
				count.append(
//						String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, "0 %"
//						,typoWrong.get(type), "0")
						String.format("%s\t%s\n", type, "0 %")
						);
				continue;
			}
			if(typoWrong.get(type) == null){
				count.append(
//						String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, "100 %"
//						,"0", typoGood.get(type))
						String.format("%s\t%s\n", type, "100 %")
						);
				continue;
			} 
			double good = typoGood.get(type);
			double bad = typoWrong.get(type);
			double total = good + bad;
			double percent = (good/total)*100;
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.HALF_UP);
			count.append(
//					String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, 
//							df.format(percent)+" %"
//							,typoWrong.get(type), typoGood.get(type))
					String.format("%s\t%s\n", type, df.format(percent)+" %")
					);
		}
		
		
		String scoresPath = Tools.tempFile("scores", ".temp", count.toString());
		String taggedTextPath = Tools.tempFile("taggedText", ".temp", sb.toString());
		count.append("\n\n\n" + sb.toString());
		
		//////////////////////
		List<String> sents = Corpus.getSentences(taggedTextPath);
		StringBuilder select = new StringBuilder();
		for(String sent : sents){
			List<String> lines = Tools.StringToList(sent);
			boolean contain = false;
			for(String line : lines){
				String[] cols =  line.split("\t");
				if((cols.length) > 10){
					String errorType = cols[10].replaceAll("ErrorType : ", "");
					if(errorType.equals(tag))contain = true;
				}
			}
			if(contain)select.append(sent+"\n");
		}

		String selectPath = Tools.tempFile("select", ".temp", select.toString());
		String switched = switch2Gold(select.toString());
		String selectSwitchedPath = Tools.tempFile("selectSwitched", ".temp", switched);
		//startMaltEval(selectSwitchedPath, selectPath);
//		File fi = new File(temp);
//		Files.deleteIfExists(fi.toPath());
//		fi = new File(out+"_"+tag);
//		Files.deleteIfExists(fi.toPath());
		String[] paths = {scoresPath,taggedTextPath,selectPath,selectSwitchedPath};
		return paths;
	}
	
	/**
	 * Gives a string with the accuracy of syntactic tags with the correct head.
	 * @param pathMalt
	 * @return
	 * @throws Exception
	 */
	public static String getDeprelScores(String pathMalt)throws Exception{
		List<String> sentences = Corpus.getSentences(pathMalt);
		StringBuilder sb = new StringBuilder();
		for(String sentence : sentences){
			String errors = getWrongSynError(sentence,
					6, 8, 7, 9);
			if(errors.length() > 0){
				sb.append(errors +"\n");
			}
		}
		StringBuilder count = new StringBuilder();
		List<String> listTypes = Tools.ListeNonRedondante(types, 0);
		for(String type : listTypes){
			if(typoGood.get(type) == null){
				count.append(
//						String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, "0 %"
//						,typoWrong.get(type), "0")
						String.format("%s\t%s\n", type, "0 %")
						);
				continue;
			}
			if(typoWrong.get(type) == null){
				count.append(
//						String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, "100 %"
//						,"0", typoGood.get(type))
						String.format("%s\t%s\n", type, "100 %")
						);
				continue;
			} 
			double good = typoGood.get(type);
			double bad = typoWrong.get(type);
			double total = good + bad;
			double percent = (good/total)*100;
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.HALF_UP);
			count.append(
//					String.format("%s\t%s\tMauvais : %s\tBon : %s\n", type, 
//							df.format(percent)+" %"
//							,typoWrong.get(type), typoGood.get(type))
					String.format("%s\t%s\n", type, df.format(percent)+" %")
					);
		}
		return count.toString();
		
	}
	

	
	public static String getAccuracyError(File path, int actual, int predicted) throws Exception{		
		return getAccuracyError(Tools.readFile(path.getAbsolutePath()),actual, predicted);
	}
	/**
	 * gives error types and tags the line containing it.
	 * For simple accuracy (two values)
	 * @param str
	 * @param actual
	 * @param predicted
	 * @return
	 * @throws Exception
	 */
	public static String getAccuracyError(String str, int actual, int predicted) throws Exception{
		List<String> lines = Tools.StringToList(str);
		StringBuilder sb = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		for(String line : lines){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			String[] cols = line.split("\t");
			types.add(cols[actual]);
			if(!cols[actual].equals(cols[predicted])){
				sb.append(String.format("%s\tErrorType : %s\n", joiner.join(cols), cols[actual]));
				if(typoWrong.containsKey(cols[actual])){
					typoWrong.put(cols[actual], (typoWrong.get(cols[actual]))+1);
				}else{
					typoWrong.put(cols[actual], 1);
				}
			}else{
				sb.append(joiner.join(cols) + "\n");
				if(typoGood.containsKey(cols[actual])){
					typoGood.put(cols[actual], (typoGood.get(cols[actual]))+1);
				}else{
					typoGood.put(cols[actual], 1);
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * gives syntactic errors when the head is correct
	 * Append an indicator at the end of the line
	 * @param str
	 * @param actualHead
	 * @param predictedHead
	 * @param actualDeprel
	 * @param predictedDeprel
	 * @return
	 * @throws Exception
	 */
	public static String getWrongSynError(String str, int actualHead, int predictedHead, int actualDeprel, int predictedDeprel) throws Exception{
		List<String> lines = Tools.StringToList(str);
		StringBuilder sb = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		for(String line : lines){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			String[] cols = line.split("\t");
			types.add(cols[actualDeprel]);
			if(!cols[actualDeprel].equals(cols[predictedDeprel])&&(cols[actualHead].equals(cols[predictedHead]))){
				sb.append(String.format("%s\tErrorType : %s\n", joiner.join(cols), cols[actualDeprel]));
				if(typoWrong.containsKey(cols[actualDeprel])){
					typoWrong.put(cols[actualDeprel], (typoWrong.get(cols[actualDeprel]))+1);
				}else {
					typoWrong.put(cols[actualDeprel], 1);
				}
			}else {
				sb.append(joiner.join(cols) + "\n");
				if(typoGood.containsKey(cols[actualDeprel])){
					typoGood.put(cols[actualDeprel], (typoGood.get(cols[actualDeprel]))+1);
				}else{
					typoGood.put(cols[actualDeprel], 1);
				}
			}
		}
		return sb.toString();
	}
	
	public static String switch2Gold(String maltContent) throws Exception{
		List<String> lines = Tools.StringToList(maltContent);
		StringBuilder sb = new StringBuilder();
		Joiner joiner = Joiner.on("\t");
		for(String line : lines){
			if(line.length() == 0){
				sb.append("\n");
				continue;
			}
			String[] cols = line.split("\t");
//			cols[8] = cols[6];
//			cols[9] = cols[7];
			cols[6] = cols[8];
			cols[7] = cols[9];
			sb.append(joiner.join(cols)+"\n");
		}
		return sb.toString();
	}
	
	
}
