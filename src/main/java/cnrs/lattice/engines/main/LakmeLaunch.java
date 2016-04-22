package cnrs.lattice.engines.main;

import is2.parser.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;

import cnrs.lattice.engines.eval.Eval;
import cnrs.lattice.engines.marmot.Marmot;
import cnrs.lattice.engines.readers.Reader;
import cnrs.lattice.engines.readers.Writer;
import cnrs.lattice.engines.treetagger.TreeTagger;
import cnrs.lattice.models.Word;
import cnrs.lattice.pipelines.Pipeline;
import cnrs.lattice.tools.corpus.Corpus;
import cnrs.lattice.tools.corpus.Format;
import cnrs.lattice.tools.utils.Tools;

public class LakmeLaunch {

	public static void main(String[] args) throws Exception{
		combinedPosTagging();
	}
	
	/**
	 * Test de combinaison des pos taggings
	 * @throws Exception
	 */
	public static void combinedPosTagging() throws Exception{
//		String path = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/lapidaire.conll";
//		Tools.ecrire("temp2", Format.conll2Marmot(path));
//		Marmot.trainTagger("temp", 1, 2, "model");
//		Marmot.testTagger("temp2", 1, "model", "out1");
//		List<String> gposList = Tools.ListeRedondante(Tools.path2liste("temp2"),2);
//		Tools.ecrire("out1GPpos",Corpus.putValueInConll(Tools.readFile("out1"), gposList, 4));
		System.out.println(Eval.getAccuracy(4, 5, "out1GPpos"));
		System.out.println(Eval.getAccuracy(4, 5, "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/beroul_ON_lapidaire.mate"));
	}
	
	/**
	 * one shot pour la lemmatization d'otinel en conservant les Glemma
	 * @date 05.02.2016
	 * @throws Exception
	 */
	public static void otinelLemmatizationTaggingParsing() throws Exception {
		String conllPath = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/otinelGoldPOSLemma/data/otinel_gpos_glemma.conll";
		String content = Tools.readFile(conllPath);
		content = Tools.remplaceColValue(content, "_", 3);
		content = Tools.remplaceColValue(content, "_", 5);
		content = Pipeline.create().setConllContent1(Tools.tempFile("temp", ".tp", content))
				.injectPosTreeTaggerPosLemma().epurationLemmes().getConllContent1();
		Tools.ecrire("temp", content);
		String matemodel = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/otinelGoldPOSLemma/models/petit.mategoldmodel";
		String wapitimodel = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/otinelGoldPOSLemma/models/petit.wapitimodel";
		String[] arguments = {"1on1","-test","temp","-matemodel", matemodel, "-wapitimodel", wapitimodel, "-out", "temp2"};
		Main.main(arguments);
		
		//injection des données gold + reformatage avec id en conll
		String res = Tools.readFile("temp2");
		String content1 = Tools.remplaceColValue(res, "_", 8);
		List<String> lines = Tools.path2liste(conllPath);
		List<String> ids = Tools.ListeRedondante(lines, 0);
		String content2 = Tools.addColValue(content1);
		String content3 = Tools.remplaceColValue(content2, ids, 0);
		List<String> lignes= Tools.StringToList(content3);
		List<String> lemmes = Tools.ListeRedondante(lines, 3);
		content3 = Tools.remplaceColValue(content3, lemmes, 3);
		List<String> gpos = Tools.ListeRedondante(lines, 5);
		content3 = Tools.remplaceColValue(content3, gpos, 5);
		Tools.ecrire("temp3", content3);
		System.out.println(content3);
		

		
		/*
		System.out.format("\n\n%s\t%s\n",
				"Prédiction et injection des POS et lemmes de TreeTagger",
				Tools.time());
		HashMap<String, String[]> map = TreeTagger.lemmatiserTT(Corpus
				.getMapIdValue(content, 0, 2));
		HashMap<String, String> mapLemmes = new HashMap<String, String>();
		HashMap<String, String> mapPos = new HashMap<String, String>();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String[] values = (String[]) entry.getValue();
			String key = entry.getKey().toString();
			mapLemmes.put(key, values[0]);
			mapPos.put(key, values[1]);
		}
		String conllLemmes = Corpus
				.putValueInConll(content, mapLemmes, 4, false);
		content = conllLemmes;
		Tools.ecrire("temp", content);
		
		System.out.format("\n\n%s\t%s\n", "Epuration des lemmes" , Tools.time());
		HashMap<String, String> mapIdLemma = Corpus.getMapIdValue(Tools.readFile("temp"), 0, 4);
		int total = mapIdLemma.size();
		int current = 0;
		Iterator it1 = mapIdLemma.entrySet().iterator();
		while(it1.hasNext()){
			current++;
			Tools.printProgress(total, current);
			Entry entry = (Entry) it1.next();
			String value = (String) entry.getValue();
			String[] values = value.split("_");
			String key = entry.getKey().toString();
			mapIdLemma.put(key, values[0]);
		}
		content = Corpus.putValueInConll(Tools.readFile("temp"), mapIdLemma, 4, false);
		Tools.ecrire("temp2", content);
		*/
		
	}

	/**
	 * One shot permettant de parser uniquement avec les gold pos
	 * 
	 * @throws Exception
	 * @TODO à ajouter correctement aux CLI
	 */
	public static void otinelParseWithGPOS(String path, String mateModel)
			throws Exception {
		String str = Tools.readFile(path);
		Reader reader = new Reader();
		List<Word> words = reader.readConll(str);
		Writer writer = new Writer();
		String res = writer.toMateGoldPOS(words);
		Tools.ecrire("temp", res);
		List<String> lines = Tools.path2liste("temp");
		List<String> replaces = Tools.ListeRedondante(lines, 5);
		String res2 = Tools.remplaceColValue(Tools.readFile("temp"), replaces,
				4);
		Tools.ecrire("temp2", res2);
		String[] arguments = { "-model", mateModel, "-test", "temp2", "-out",
				"temp3" };
		Parser.main(arguments);
		Format.materes2conllComplet("temp3", path, "temp4");
	}

	/**
	 * Entrainement des modèles qui serviront à prédire Otinel
	 * 
	 * @throws Exception
	 */
	public static void otinel() throws Exception {

		String home = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel";

		String conllCompletPath = home + "/petit.conll";
		String conllTestPath = home + "/otinel.conll";
		String conllTestPathPrep = home + "/otinel.conll";
		String wapitiModelPath = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/petit.wapitimodel";// home+"/petit.wapitimodel";
		String wapitiTemplatePath = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/template_tlt";
		String mateModelPath = home + "/petit.matemodel";
		String wapitiTrainSet = String.format("%s/%s.wapiti", home,
				FilenameUtils.getBaseName(conllCompletPath));
		String mateTrainSet = String.format("%s/%s.mate", home,
				FilenameUtils.getBaseName(conllCompletPath));
		String wapitiTestSet = String.format("%s/%s.wapiti", home,
				FilenameUtils.getBaseName(conllTestPath));
		String mateTestSet = String.format("%s/%sTest.mate", home,
				FilenameUtils.getBaseName(conllTestPath));
		String wapitiOutput = String.format("%s/%s_ON_%s.wapiti", home,
				FilenameUtils.getBaseName(mateModelPath),
				FilenameUtils.getBaseName(conllTestPath));
		String mateOutput = String.format("%s/%s_ON_%s.mate", home,
				FilenameUtils.getBaseName(mateModelPath),
				FilenameUtils.getBaseName(conllTestPath));
		String conllOutput = String.format("%s/%s_ON_%s.conll", home,
				FilenameUtils.getBaseName(mateModelPath),
				FilenameUtils.getBaseName(conllTestPath));

		/*
		 * Pipeline.create().stopwatchStart() .setConllContent1(conllTestPath)
		 * .injectPosTreeTaggerPosLemma() // .epurationLemmes()
		 * .exportConll(conllTestPathPrep) .formatWapiti()
		 * .exportWapitiInput(wapitiTestSet) ;
		 * 
		 * // // Pipeline.create() // .setConllContent(conllTestPathPrep) //
		 * .setTestContent(wapitiTestSet) // .setWapitiLibrary("wapiti") //
		 * .setWapitiTemplate(wapitiTemplatePath) //
		 * .setWapitiModel(wapitiModelPath) // .testWapiti() //
		 * .exportWapitiOutput(wapitiOutput) // ; // Pipeline.create()
		 * .setConllContent1(conllTestPathPrep) .setWapitiTest(wapitiOutput) //
		 * .exportMateTrainset(mateTrainSet) .formatMateGold() .formatMatePPos()
		 * .exportMateTestset(mateTestSet) .setMateModelPath(mateModelPath)
		 * .testMate() .exportMateOutput(mateOutput)
		 * .exportConllComplet(conllOutput) // .evalMate() ; // //
		 * Pipeline.create().stopwatchStart() //
		 * .setConllContent(conllCompletPath) // .epurationLemmes() //
		 * .formatWapiti() // .exportWapitiInput(wapitiTrainSet) //
		 * .setWapitiModel(wapitiModelPath) //
		 * .setWapitiTemplate(wapitiTemplatePath) // .trainWapiti() ////
		 * .formatMateGold() //// .setMateModelPath(mateModelPath) ////
		 * .exportMateTrainset(mateTrainSet) //// .trainMate() //
		 * .stopwatchStop() // ;
		 * 
		 * // System.exit(0);
		 */

		conllCompletPath = home + "/grand.conll";
		conllTestPath = home + "/otinelPrep.conll";
		wapitiModelPath = home + "/grand.wapitimodel";
		wapitiTemplatePath = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/template_tlt";
		mateModelPath = home + "/grand.matemodel";
		wapitiTrainSet = String.format("%s/%s.wapiti", home,
				FilenameUtils.getBaseName(conllCompletPath));
		mateTrainSet = String.format("%s/%s.mate", home,
				FilenameUtils.getBaseName(conllCompletPath));
		wapitiTestSet = String.format("%s/%s.wapiti", home,
				FilenameUtils.getBaseName(conllTestPath));
		mateTestSet = String.format("%s/%s.mate", home,
				FilenameUtils.getBaseName(conllTestPath));
		wapitiOutput = String.format("%s/%s_ON_%s.wapiti", home,
				FilenameUtils.getBaseName(wapitiModelPath),
				FilenameUtils.getBaseName(conllTestPath));
		mateOutput = String.format("%s/%s_ON_%s.mate", home,
				FilenameUtils.getBaseName(mateModelPath),
				FilenameUtils.getBaseName(conllTestPath));

		// Pipeline.create().stopwatchStart()
		// .setConllContent1(conllCompletPath)
		// .epurationLemmes()
		// .formatWapiti()
		// .exportWapitiInput(wapitiTrainSet)
		// .setWapitiModel(wapitiModelPath)
		// .setWapitiTemplate(wapitiTemplatePath)
		// .trainWapiti()

		// .formatMateGold()
		// .setMateModelPath(mateModelPath)
		// .exportMateTrainset(mateTrainSet)
		// .trainMate()
		// .stopwatchStop()
		// ;

		// Pipeline.create()
		// .setConllContent1(conllTestPath)
		// .injectPosTreeTaggerPosLemma()
		// .epurationLemmes()
		// .exportConllComplet(conllTestPath)
		// .formatWapiti()
		// .exportWapitiInput(wapitiTestSet)
		// ;
		//
		//
		Pipeline.create().setConllContent1(conllTestPath)
				.setWapitiTest(wapitiTestSet).setWapitiLibrary("wapiti")
				.setWapitiTemplate(wapitiTemplatePath)
				.setWapitiModel(wapitiModelPath).testWapiti()
				.exportWapitiOutput(wapitiOutput)

		//
		// .formatMateGold()
		// .formatMatePPos()
		// .setMateModelPath(mateModelPath)
		// .exportMateTestset(mateTestSet)
		// .testMate()
		// .exportMateOutput(mateOutput)
		// .exportConllComplet(conllOutput)
		// .evalMate()
		;

	}

}
