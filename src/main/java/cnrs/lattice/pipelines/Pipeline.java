package cnrs.lattice.pipelines;

import is2.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Stopwatch;

import cnrs.lattice.engines.eval.Eval;
import cnrs.lattice.engines.readers.Reader;
import cnrs.lattice.engines.readers.Writer;
import cnrs.lattice.engines.treetagger.TreeTagger;
import cnrs.lattice.engines.wapiti.WapitiModel;
import cnrs.lattice.models.Word;
import cnrs.lattice.tools.corpus.Corpus;
import cnrs.lattice.tools.corpus.Fixer;
import cnrs.lattice.tools.corpus.Format;
import cnrs.lattice.tools.utils.Tools;
@SuppressWarnings("static-access")
public class Pipeline {
	
	private static String conll1 = "";
	private static String conll2 = "";
	private static String wapitiTest = "";
	private static String xmlPath = "";
	private static String wapitiInput = "";
	private static String wapitiOutput = "";
	private static String wapitiTemplatePath = "";
	private static String wapitiModelPath = "";
	private static String wapitiParams = "";
	private static String wapitiLibrary = "wapiti";
	private static String mateTrainSet = "";
	private static String mateTestSet = "";
	private static String mateModelPath = "";
	private static String mateOutput = "";
	
	private static String uas = "";
	private static String las = "";
	private static String acc = "";
	private static Stopwatch stopwatch = Stopwatch.createUnstarted();
	
	static
	{
	System.loadLibrary(wapitiLibrary); // Notice lack of lib prefix (.so)
	}
	
	private Pipeline() throws IOException{
		 System.out.format("START\t%s\n\n",Tools.time());
//		 stopwatch.start();
	}
	
	public static Pipeline create() throws IOException{
		return new Pipeline();
	}
	
	public Pipeline stopwatchStart() {
		stopwatch.start();
		return this;
	}
	
	public Pipeline stopwatchStop() throws IOException{
		System.out.format("TIME : \t%s\n", stopwatch);
		stopwatch.stop();
		return this;
	}
	
	/*
	 * 
	 * SETTERS
	 * 
	 */
	
	
	public Pipeline setConllContent1(String conllPath) throws Exception{
		System.out.printf("Input 1 : %s\n", conllPath);
		this.conll1 = Tools.readFile(conllPath);
		return this;
	}
	
	public Pipeline setConllContent2(String conllPath) throws Exception{
		this.conll2 = Tools.readFile(conllPath);
		return this;
	}
	
	public Pipeline setWapitiTest(String testPath) throws Exception{
		this.wapitiTest = Tools.readFile(testPath);
		return this;
	}
	
	public Pipeline setXmlPath(String xmlPath) throws Exception{
		this.xmlPath = xmlPath;
		return this;
	}
	
	public Pipeline setWapitiLibrary(String wapitiLibrary){
		this.wapitiLibrary = wapitiLibrary;
		return this;
	}
	
	public Pipeline setWapitiInput(String wapitiInput) throws IOException{
		this.wapitiInput = Tools.readFile(wapitiInput);
		return this;
	}
	
	public Pipeline setWapitiModel(String wapitiModelPath) {
		this.wapitiModelPath = wapitiModelPath;
		return this;
	}
	
	public Pipeline setWapitiTemplate(String wapitiTemplatePath){
		this.wapitiTemplatePath = wapitiTemplatePath;
		return this;
	}
	
//	/**
//	 * Mis afin de permettre de récupérer directement un fichier de résultats de wapiti pour intégrer ses valeurs
//	 *  dans le format mate. Permet de ne pas relancer sans cesse wapiti.
//	 * @param wapitiOutputPath
//	 * @return
//	 * @throws IOException
//	 */
//	public PipelineCorpus setWapitiResult(String wapitiOutputPath) throws IOException{
//		this.wapitiOutput = Tools.readFile(wapitiOutputPath);
//		return this;
//	}
	
	public Pipeline setMateModelPath(String mateModelPath){
		this.mateModelPath = mateModelPath;
		return this;
	}
	
	public Pipeline setMateTrainSet(String mateTrainSet) throws IOException{
		this.mateTrainSet = Tools.readFile(mateTrainSet);
		return this;
	}
	
	public Pipeline setMateTestSet(String mateTestSet) throws IOException{
		this.mateTestSet = Tools.readFile(mateTestSet);
		return this;
	}
	
	/*
	 * 
	 * GETTERS
	 * 
	 */
	
	public String getConllContent1() throws Exception{
		return this.conll1;
	}
	
	public String getConllContent2() throws Exception{
		return this.conll2;
	}
	
	public String getTestContent() throws Exception{
		return this.wapitiTest;
	}
	
	public String getWapitiInput() throws Exception{
		return this.wapitiInput;
	}
	
	public String getWapitiTest() throws Exception{
		return this.wapitiTest;
	}
	
	public String getWapitiOutput() throws Exception{
		return this.wapitiOutput;
	}
	
	public String getMateTrainSet() throws Exception{
		return this.mateTrainSet;
	}
	
	public String getMateTestSet() throws Exception{
		return this.mateTestSet;
	}
	
	public String getMateOutput() throws Exception{
		return this.mateOutput;
	}
	
	/*
	 * 
	 * FORMAT PROCESSES
	 * 
	 */
	
	public Pipeline catDir(String dirPath) throws Exception{
		System.out.format("\n\n%s\t%s\n","Concaténation des fichiers", Tools.time());
		String[] tab = Tools.dir2arrayPaths(dirPath);
		this.conll1 = Tools.cat(tab, "\n\n");
		return this;
	}
	
	public Pipeline fixParseError() throws Exception{
		System.out.format("\n\n%s\t%s\n","Correction des erreurs de parsing", Tools.time());
		this.conll1 = Fixer.CorrectionParseID(conll1);
		return this;
	}
	
	public Pipeline syntacticFixer()throws Exception{
		this.conll1 = Fixer.fixSyntaxDuplicataAndAnnotatorTag(conll1, 11);
		return this;
	}
	
	
	public Pipeline epurationLemmes() throws Exception{
		System.out.format("\n\n%s\t%s\n", "Epuration des lemmes" , Tools.time());
		HashMap<String, String> mapIdLemma = Corpus.getMapIdValue(this.conll1, 0, 3);
		int total = mapIdLemma.size();
		int current = 0;
		Iterator it = mapIdLemma.entrySet().iterator();
		while(it.hasNext()){
			current++;
			Tools.printProgress(total, current);
			Entry entry = (Entry) it.next();
			String value = (String) entry.getValue();
			String[] values = value.split("_");
			String key = entry.getKey().toString();
			mapIdLemma.put(key, values[0]);
		}
		this.conll1 = Corpus.putValueInConll(this.conll1, mapIdLemma, 3, false);
		return this;
	}
	
	public Pipeline injectPosXmlTei() throws Exception{
		System.out.format("\n\n%s\t%s\n","Injection des POS du XML TEI", Tools.time());
		HashMap<String, String> mapIdPos = Corpus.parseTeiXml(xmlPath);
		this.conll1 = Corpus.putValueInConll(conll1, mapIdPos, 5, true);
		return this;
	}

	public Pipeline injectPosTigerXml() throws Exception{
		System.out.format("\n\n%s\t%s\n","Injection des POS du Tiger XML", Tools.time());
		HashMap<String, String> mapIdPos = Corpus.parseTigerXml(xmlPath, "//t");
		this.conll1 = Corpus.putValueInConll(conll1, mapIdPos, 5, false);
		return this;
	}
	
	public Pipeline injectPosTreeTaggerPosLemma() throws Exception{
		System.out.format("\n\n%s\t%s\n","Prédiction et injection des POS et lemmes de TreeTagger", Tools.time());
		HashMap<String, String[]> map = TreeTagger.lemmatiserTT(
				Corpus.getMapIdValue(conll1,0,2)
				);
		HashMap<String, String> mapLemmes = new HashMap<String, String>();
		HashMap<String, String> mapPos = new HashMap<String, String>();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()){
			Entry entry = (Entry) it.next();
			String[] values = (String[]) entry.getValue();
			String key = entry.getKey().toString();
			mapLemmes.put(key, values[0]);
			mapPos.put(key, values[1]);
		}
		String conllLemmes = Corpus.putValueInConll(conll1, mapLemmes, 3, false);
		String conllTTpos = Corpus.putValueInConll(conllLemmes, mapPos, 4, false);
		this.conll1 = conllTTpos;
		
		return this;
	}
	
	/*
	 * 
	 * WAPITI PROCESSES
	 * 
	 */
	
	public Pipeline formatWapiti() throws Exception{
		System.out.format("\n\n%s\t%s\n","Mise au format Wapiti", Tools.time());
		List<Word> words = new Reader().readConll(this.conll1);
		Writer writer = new Writer();
		this.wapitiInput = writer.toWapitiGold(words);		
		return this;
	}
	
	public Pipeline trainWapiti() throws Exception{
		System.out.format("\n\n%s\t%s\n","Entrainement Wapiti", Tools.time());
		File template = new File(wapitiTemplatePath);
		File trainingData = new File(Tools.tempFile("wapitidata", ".wapiti", wapitiInput));
		File outputModel = new File(wapitiModelPath);
		WapitiModel.train(template, trainingData, outputModel, wapitiParams);
		return this;
	}
	
	public Pipeline testWapiti() throws Exception{
		System.out.format("\n\n%s\t%s\n","Etiquetage Wapiti", Tools.time());
		File outputModel = new File(wapitiModelPath);
		WapitiModel w = new WapitiModel(outputModel);
		this.wapitiOutput = w.label(wapitiTest);
		return this;
	}
	
	/*
	 * 
	 * MATE PROCESSES
	 * 
	 */
	
	public Pipeline formatMateGold() throws Exception{
		System.out.format("\n\n%s\t%s\n","Mise au format Mate avec Gold Pos", Tools.time());
		List<Word> words = new Reader().readConll(conll1);
		Writer writer = new Writer();
		mateTrainSet = writer.toMateGoldPOS(words);
		return this;
	}
	
	public Pipeline formatMatePPos(String wapitiOutputPath) throws Exception{
		this.wapitiOutput = Tools.readFile(wapitiOutputPath);
		formatMatePPos();
		return this;
	}
	
	/*
	 * debugger mateTestSet
	 */
	public Pipeline formatMatePPos() throws Exception{
		System.out.format("\n\n%s\t%s\n","Mise au format Mate avec PPos", Tools.time());
		List<Word> wordsMateGold = new Reader().readConll(conll2);
		String mate = new Writer().toMateWapitiPPOS(wordsMateGold);
		String mateTestSetPPOS = Format.POSWapitiToMate(mate, wapitiOutput);//mateTrainSet
		List<Word> wordsMatePpos = new Reader().readMate(mateTestSetPPOS);
		Writer writer = new Writer();
		this.mateTestSet = writer.toMateWapitiPPOS(wordsMatePpos);
		return this;
	}
	
	public Pipeline trainMate() throws Exception{
		System.out.format("\n\n%s\t%s\n","Entrainement de l'analyseur syntaxique avec Mate", Tools.time());
		String[] arguments = {"-model", mateModelPath, "-train", Tools.tempFile("trainSet", ".mate", mateTrainSet)};
		Parser.main(arguments);
		return this;
	}
	
	public Pipeline testMate() throws Exception{
		System.out.format("\n\n%s\t%s\n","Analyse syntaxique avec Mate", Tools.time());
		String outPath = Tools.tempFile("output", ".mate", mateOutput);
		String[] arguments = {"-model", mateModelPath, "-test", Tools.tempFile("testSet", ".mate", mateTestSet),
				 "-out", outPath};
		Parser.main(arguments);
		this.mateOutput = Tools.readFile(outPath);
		return this;
	}
	
	/*
	 * 
	 * EVAL
	 * 
	 */
	
	public Pipeline evalWapiti() throws Exception{
		this.acc = Eval.getAccuracy(7, 8, Tools.tempFile("wapiti",".wapitieval", wapitiOutput));
		String console = String.format("\n\nAccuracy : %s\n",
				acc);
		System.out.println(console);
		return this;
	}
	
	public Pipeline evalMate() throws Exception{
		this.las = Eval.getLAS(8, 9, 10, 11, Tools.tempFile("mate",".mateeval", mateOutput));
		this.uas = Eval.getAccuracy(8, 9, Tools.tempFile("mate",".mateeval", mateOutput));
		if(acc.length() == 0)this.acc = Eval.getAccuracy(4, 5, Tools.tempFile("mate",".mateeval", mateOutput));
		String console = String.format("\n\nAccuracy : %s\nUAS : %s\nLAS : %s\n",
				acc, uas, las);
		System.out.println(console);
		return this;
	}
	
	/*
	 * 
	 * EXPORTERS
	 * 
	 */
	
	public Pipeline exportWapitiOutput(String path) throws Exception{
		System.out.format("\n\n%s\t%s\t%s\n","Export de la sortie de wapiti", path , Tools.time());
		Tools.ecrire(path, wapitiOutput);
		return this;
	}
	
	public Pipeline exportWapitiInput(String path) throws Exception{
		System.out.format("\n\n%s\t%s\t%s\n","Export de l'entrée de wapiti", path , Tools.time());
		Tools.ecrire(path, wapitiInput);
		return this;
	}
	
	public Pipeline exportConll(String path) throws Exception{
		System.out.format("\n\n%s\t%s\t%s\n","Export du Conll traité", path , Tools.time());
		Tools.ecrire(path, conll1);
		return this;
	}
	
	public Pipeline exportConllComplet(String path) throws Exception{
		System.out.format("\n\n%s\t%s\t%s\n","Export du Conll traité", path , Tools.time());
//		Reader reader = new Reader();
//		List<Word> words = reader.readMate(conll1);
//		List<Word> words2 = reader.putValues(words);
//		Writer writer = new Writer();
//		String conllRempli = writer.toConll(words2);
//		Tools.ecrire(path, conllRempli);
		return this;
	}

	
	public Pipeline exportMateTrainset(String path) throws Exception{
		System.out.format("\n\n%s\t%s\t%s\n","Export du fichier d'entrainement de Mate", path , Tools.time());
		Tools.ecrire(path, mateTrainSet);
		return this;
	}
	
	public Pipeline exportMateTestset(String path) throws Exception{
		System.out.format("\n\n%s\t%s\t%s\n","Export du fichier de test de Mate", path , Tools.time());
		Tools.ecrire(path, mateTestSet);
		return this;
	}
	
	public Pipeline exportMateOutput(String path) throws Exception{
		System.out.format("\n\n%s\t%s\t%s\n","Export de la sortie de Mate", path , Tools.time());
		Tools.ecrire(path, mateOutput);
		return this;
	}
}
