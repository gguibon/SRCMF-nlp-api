package cnrs.lattice.engines.main;

import java.io.IOException;

import com.google.common.base.Stopwatch;

import cnrs.lattice.pipelines.Pipeline;
import cnrs.lattice.tools.utils.Tools;

public class OneOnOne {
	
	private static String TRAIN = "";
	private static String TEST = "";
	private static String OUTPUT = "";
	private static String TEMPLATE = "/template_tlt"; // /resources/template_tlt ou /template_tlt
	private static String WAPITI_MODEL = "";
	private static String MATE_MODEL = "";
	
	private OneOnOne(){
		
	}
	
	public static void launcher(String mode, String train, String test, String output,
			String template, String wapitimodel, String matemodel) throws Exception{
		OneOnOne ooo = new OneOnOne();
		
		switch(mode){
		case "1on1":
			TRAIN = train;
			TEST = test;
			OUTPUT = output;
			if(template.length() > 0) TEMPLATE = template;
			WAPITI_MODEL = wapitimodel;
			MATE_MODEL = matemodel;
			if( (train.length() == 0)&&(test.length()!=0)&&
					(wapitimodel.length()!=0)&&(matemodel.length()!=0)&&(output.length()!=0) ){
				ooo.runTest();
			}else if( (train.length() != 0)&&(test.length()==0)&&
					(wapitimodel.length()!=0)&&(matemodel.length()!=0)&&(output.length()==0) ){
				ooo.runTrain();
			}else if( (train.length()!=0)&&(test.length()!=0)&&(output.length()!=0) ){
				ooo.run();
			}
			break;
		}

	}
	
	/**
	 * Training and test using wapiti and mate
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private OneOnOne run() throws IOException, Exception{
		
		Stopwatch stopwatch = Stopwatch.createStarted();
		
		String wapitiModelPath = Tools.tempFile("wapiti", ".wapitimodel", "");
		if(WAPITI_MODEL.length() != 0)wapitiModelPath = WAPITI_MODEL;
		String mateModelPath = Tools.tempFile("mate", ".matemodel", "");
		if(MATE_MODEL.length() != 0)mateModelPath = MATE_MODEL;
		String conllCompletPath1 = TRAIN;
		String conllCompletPath2 = TEST;
		
		String trainWapiti = Pipeline.create()
				.setConllContent1(conllCompletPath1)
				.fixParseError()
				.epurationLemmes()
				.formatWapiti()
				.getWapitiInput()
				;
		
		String testWapiti = Pipeline.create()
				.setConllContent1(conllCompletPath2)
				.fixParseError()
				.epurationLemmes()
				.formatWapiti()
				.getWapitiInput()
				;
		
		trainWapiti = Tools.tempFile("train", ".wapiti", trainWapiti);
		testWapiti = Tools.tempFile("test", ".wapiti", testWapiti);
		
		String template = new Tools().accessRessourceFile(TEMPLATE);
		String templatePath = Tools.tempFile("template", "", template);// /resources/template_tlt ou /template_tlt

		
		Pipeline.create()
				.setConllContent1(conllCompletPath1)
				.setConllContent2(conllCompletPath2)
				.setWapitiInput(trainWapiti)
				.setWapitiTest(testWapiti)
				.setWapitiModel(wapitiModelPath)
				.setWapitiTemplate(templatePath)
				.trainWapiti()
				.testWapiti()

				.formatMateGold()
				.formatMatePPos()
				.setMateModelPath(mateModelPath)
				.trainMate()
				.testMate()
				.exportMateOutput(OUTPUT)		
				.evalMate()
				;

		System.out.println(stopwatch);
		return this;			
	}
	
	/**
	 * Wapiti and mate training to export models. Given the conll for training.
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private OneOnOne runTrain() throws IOException, Exception{
		
		Stopwatch stopwatch = Stopwatch.createStarted();
		String wapitiModelPath = Tools.tempFile("wapiti", ".wapitimodel", "");
		if(WAPITI_MODEL.length() != 0)wapitiModelPath = WAPITI_MODEL;
		String mateModelPath = Tools.tempFile("mate", ".matemodel", "");
		if(MATE_MODEL.length() != 0)mateModelPath = MATE_MODEL;

		String conllCompletPath1 = TRAIN;
		
		String trainWapiti = Pipeline.create()
				.setConllContent1(conllCompletPath1)
				.fixParseError()
				.epurationLemmes()
				.formatWapiti()
				.getWapitiInput()
				;

		trainWapiti = Tools.tempFile("train", ".wapiti", trainWapiti);
		
		String template = new Tools().accessRessourceFile(TEMPLATE);
		String templatePath = Tools.tempFile("template", "", template);
		
		Pipeline.create()
				.setConllContent1(conllCompletPath1)
				.fixParseError()
				.epurationLemmes()
				.setWapitiInput(trainWapiti)
				.setWapitiModel(wapitiModelPath)
				.setWapitiTemplate(templatePath)
				.trainWapiti()
				.formatMateGold()
				.setMateModelPath(mateModelPath)
				.trainMate()
				;

		System.out.println(stopwatch);
		return this;			

	}	
	
	
	/**
	 * Apply mate and wapiti based on given models, on given test conll.
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
private OneOnOne runTest() throws IOException, Exception{
		
		Stopwatch stopwatch = Stopwatch.createStarted();
		String wapitiModelPath = Tools.tempFile("wapiti", ".wapitimodel", "");
		if(WAPITI_MODEL.length() != 0)wapitiModelPath = WAPITI_MODEL;
		String mateModelPath = Tools.tempFile("mate", ".matemodel", "");
		if(MATE_MODEL.length() != 0)mateModelPath = MATE_MODEL;

		String conllCompletPath2 = TEST;
		
		String testWapiti = Pipeline.create()
				.setConllContent1(conllCompletPath2)
				.fixParseError()
				.epurationLemmes()
				.formatWapiti()
				.getWapitiInput()
				;

		testWapiti = Tools.tempFile("test", ".wapiti", testWapiti);
		
		String template = new Tools().accessRessourceFile(TEMPLATE);
		String templatePath = Tools.tempFile("template", "", template);
		
		Pipeline.create()
				.setConllContent2(conllCompletPath2)
				.setWapitiTest(testWapiti)
				.setWapitiModel(wapitiModelPath)
				.setWapitiTemplate(templatePath)
				.testWapiti()
				.formatMateGold()
				.formatMatePPos()
				.setMateModelPath(mateModelPath)
				.testMate()
				.exportMateOutput(OUTPUT)
				.evalMate()
				;

		System.out.println(stopwatch);
		return this;			

	}


}
