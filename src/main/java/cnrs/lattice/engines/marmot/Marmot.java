package cnrs.lattice.engines.marmot;

import java.io.File;
import java.util.List;

import com.google.common.base.Joiner;

import cnrs.lattice.engines.eval.Eval;
import cnrs.lattice.engines.wapiti.WapitiModel;
import cnrs.lattice.pipelines.Pipeline;
import cnrs.lattice.tools.corpus.Corpus;
import cnrs.lattice.tools.corpus.Format;
import cnrs.lattice.tools.utils.Tools;

public class Marmot {
	static
	{
	System.loadLibrary("wapiti"); // Notice lack of lib prefix (.so)
	}
	public static void main(String[] args) throws Exception {
		// String path =
		// "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/lapidaire.conll";
		// Tools.ecrire("temp2", Format.conll2Marmot(path));
		// trainTagger("temp", 1, 2, "model");
		// testTagger("temp2", 1, "model", "out1");
		// List<String> gposList =
		// Tools.ListeRedondante(Tools.path2liste("temp2"),2);
		// Tools.ecrire("out1GPpos",Corpus.putValueInConll(Tools.readFile("out1"),
		// gposList, 4));
		// System.out.println(Eval.getAccuracy(4, 5, "out1GPpos"));
		// System.out.println(Eval.getAccuracy(4, 5,
		// "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/beroul_ON_lapidaire.mate"));
		
		
		//beroul / yvain marmot
		/*
		String beroulConll = "/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/SRCMF_AS/beroul.taglemfeat.conll";
		String yvainConll = "/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/SRCMF_AS/yvain.taglemfeat.conll";
		Tools.ecrire("beroul.marmot.tsv", Format.conll2Marmot(beroulConll, true));
		Tools.ecrire("yvain.marmot.tsv", Format.conll2Marmot(yvainConll, true));
		trainTagger("beroul.marmot.tsv", 1 , 2 , 3, "beroul.marmotmodel");
		testTagger("yvain.marmot.tsv", 1, "beroul.marmotmodel", "beroul_ON_yvain.marmot.tsv");
		List<String> gmorphList = Tools.ListeRedondante(Tools.path2liste("yvain.marmot.tsv"),2);
		Tools.ecrire("out",Corpus.putValueInConll(Tools.readFile("beroul_ON_yvain.marmot.tsv"),
				 gmorphList, 4));
		System.out.println(Eval.getAccuracy(4, 5, "out"));
		*/
		//beroul / yvain wapiti
		/*
		String trainWapiti = Pipeline.create()
				.setConllContent1(beroulConll)
				.injectPosTreeTaggerPosLemma()
				.fixParseError()
				.epurationLemmes()
				.formatWapiti()
				.getWapitiInput()
				;
		String testWapiti = Pipeline.create()
				.setConllContent1(yvainConll)
				.injectPosTreeTaggerPosLemma()
				.fixParseError()
				.epurationLemmes()
				.formatWapiti()
				.getWapitiInput()
				;
		Tools.ecrire("beroul.wapiti.tsv", trainWapiti);
		Tools.ecrire("yvain.wapiti.tsv", testWapiti);
		*/
		
//		Pipeline.create()
//				.setConllContent1(beroulConll)
//				.fixParseError()
//				.epurationLemmes()
//				.setWapitiInput("beroul.wapiti.tsv")
//				.setWapitiModel("beroul.wapitimodel")
//				.setWapitiTemplate("src/main/resources/template_tlt")
//				.trainWapiti()
//				;
		/*
		File outputModel = new File("beroul.wapitimodel");
		WapitiModel w = new WapitiModel(outputModel);
		String res = w.label(Tools.readFile("yvain.wapiti.tsv"));
		Tools.ecrire("beroul_ON_yvain.wapiti.tsv", res);
		System.out.println(Eval.getAccuracy(4, 5, "out"));
		System.out.println(Eval.getAccuracy(7, 8, "beroul_ON_yvain.wapiti.tsv"));
		*/
		/*
		List<String> gposList = Tools.ListeRedondante(Tools.path2liste("yvain.marmot.tsv"),3);
		Tools.ecrire("realOut",Corpus.putValueInConll(Tools.readFile("out"),
				 gposList, 6));
		System.out.println(Eval.getAccuracy(6, 7, "realOut"));
		*/
//		List<String> marmotposList = Tools.ListeRedondante(Tools.path2liste("realOut"),7);
//		Tools.ecrire("beroul_ON_yvain.pposmarmot.wapiti.tsv",Corpus.putValueInConll(Tools.readFile("yvain.wapiti.tsv"),
//				 marmotposList, 3));
		File outputModel = new File("beroul.wapitimodel");
		WapitiModel w = new WapitiModel(outputModel);
		String res = w.label(Tools.readFile("beroul_ON_yvain.pposmarmot.wapiti.tsv"));
		Tools.ecrire("OUT", res);
		System.out.println(Eval.getAccuracy(3, 7, "OUT"));
		System.out.println(Eval.getAccuracy(7, 8, "beroul_ON_yvain.wapiti.tsv"));
	}

	/**
	 * Training of Marmot tagger without morphological tags
	 * 
	 * @param filePath
	 * @param formIndex
	 * @param tagIndex
	 * @param modelPath
	 * @throws Exception
	 */
	public static void trainTagger(String filePath, int tagIndex, int formIndex,
			String modelPath) throws Exception {
		String[] arguments = {
				"-train-file",
				String.format("form-index=%s,tag-index=%s,%s",
						String.valueOf(formIndex), String.valueOf(tagIndex),
						filePath), "-tag-morph", "false", "-model-file",
				modelPath, "-num-iterations", "50", "-verbose", "true", };
		Joiner joiner = Joiner.on(" ");
		System.out.println("java -cp" + joiner.join(arguments));
		marmot.morph.cmd.Trainer.main(arguments);
	}
	
	
	/**
	 * Training of Marmot tagger without morphological tags
	 * 
	 * @param filePath
	 * @param formIndex
	 * @param tagIndex
	 * @param modelPath
	 * @throws Exception
	 */
	public static void trainTagger(String filePath, int formIndex,
			int tagIndex, int morphIndex, String modelPath) throws Exception {
		String[] arguments = {
				"-train-file",
				String.format("form-index=%s,tag-index=%s,morph-index=%s,%s",
						String.valueOf(formIndex), String.valueOf(tagIndex),
						String.valueOf(morphIndex),
						filePath), "-tag-morph", "true", "-model-file",
				modelPath, "-num-iterations", "50", "-verbose", "true", };
		Joiner joiner = Joiner.on(" ");
		System.out.println("java -cp" + joiner.join(arguments));
		marmot.morph.cmd.Trainer.main(arguments);
	}

	/**
	 * Apply the Marmot tagger using specified models and file
	 * 
	 * @param filePath
	 * @param formIndex
	 * @param modelPath
	 * @param outputPath
	 * @throws Exception
	 */
	public static void testTagger(String filePath, int formIndex,
			String modelPath, String outputPath) throws Exception {
		String[] arguments = {
				"-test-file",
				String.format("form-index=%s,%s", String.valueOf(formIndex),
						filePath), "-model-file", modelPath, "-pred-file",
				outputPath, };
		Joiner joiner = Joiner.on(" ");
		System.out.println("java -cp" + joiner.join(arguments));
		marmot.morph.cmd.Annotator.main(arguments);
	}
}
