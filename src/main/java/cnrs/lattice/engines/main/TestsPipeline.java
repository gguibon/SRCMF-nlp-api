package cnrs.lattice.engines.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Stopwatch;

import se.vxu.msi.malteval.MaltEvalConsole;
import cnrs.lattice.engines.readers.Reader;
import cnrs.lattice.pipelines.Pipeline;
import cnrs.lattice.tools.corpus.Fixer;
import cnrs.lattice.tools.corpus.Format;
import cnrs.lattice.tools.utils.Tools;

public class TestsPipeline {

	public static void main(String[] args) throws Exception {
		//epuration des corpus livrables
		List<File> listFiles = Tools.dir2listefiles("/home/gael/Documents/SRCMF/PostTLT2015/LivrableDatas/cured");
		for(File file : listFiles){
			String path = file.getAbsolutePath();
			String content = Fixer.fixIgnorerLine(path, 0, 5, 8, 9);
			content = Fixer.fixSyntaxDuplicataAndAnnotatorTag(content, 10);
			content = Fixer.fixSyntaxDuplicataAndAnnotatorTag(content, 10);
			content = Fixer.fixSyntaxDuplicataAndAnnotatorTag(content, 11);
			content = Fixer.fixSyntaxDuplicataAndAnnotatorTag(content, 11);
			Tools.ecrire("/home/gael/Documents/SRCMF/PostTLT2015/LivrableDatas/cured2/"+file.getName()+".cured",
					content);
		}
		//epuration des corpus livrables end
		
		//otinel parsing from scratch
//		LakmeLaunch.otinelLemmatizationTaggingParsing();
		
		//test otinel parsing using Gold Pos
//		LakmeLaunch.otinelParseWithGPOS(
//				"/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/otinelGoldPOSLemma/data/otinel_gpos_glemma.conll",
//				"/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/otinelGoldPOSLemma/models/grand.mategoldmodel");
		
		
//		looGraal();
		
//			LakmeLaunch.otinel();
		
//		String grandPath = "grand_ON_otinel";
//		String petitPath = "petit_ON_otinel";
//		String grandOtinel = Format.conll2MaltPredicted(grandPath+".conll");
//		String petitOtinel = Format.conll2MaltPredicted(petitPath + ".conll");
//		Tools.ecrire(grandPath + ".malt"
//				, grandOtinel);
//		Tools.ecrire(petitPath + ".malt", petitOtinel);
//		String[] arguments = {"-s",grandPath + ".malt",
//				"-g",petitPath+".malt","-v","1"};
//		MaltEvalConsole.main(arguments);
		
		//03.02.2016
//		String fixed = Fixer.fixIgnorerLine("/home/gael/workspace/SRCMF-NLP/yvain");
//		fixed = Fixer.fixIgnorerLine(Tools.tempFile("temp", ".temp",fixed));
//		fixed = Fixer.fixIgnorerLine(Tools.tempFile("temp", ".temp",fixed));
//		Tools.ecrire("temp",fixed);
	}
	
	
	public static void tests() throws Exception{
		
//		Tools t = new Tools();
//		System.out.println(t.accessRessourceFile("/template_tlt"));
//		System.exit(0);
		
		String dir_path = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Sources/Lapidaire/Lapidaireconll";
		List<String> paths = new ArrayList<String>();
		
//		File rootDir = new File(dir_path); //this is your root directory
//		for (File f : Files.fileTreeTraverser().preOrderTraversal(rootDir)) {
//		    // do whatever you need with the file/directory
//			System.out.println(f.getAbsolutePath());
//		    // if you need the relative path, with respect to rootDir
////		    Path relativePath = rootDir.toPath().getParent().relativize(f.toPath());
//		}
		
		
		/*
		paths = Tools.dir2listepaths(dir_path);
		String[] tab = new String[paths.size()];
		paths.toArray(tab);
//		Arrays.sort( tab );
		System.out.println(tab[0] + "\t" + paths.get(0) +"\n"+ tab[1] + "\t" + paths.get(1));
		
//		String[] array = paths.toArray(new String[paths.size()]);
		Tools.ecrire("/"+FilenameUtils.getPath(dir_path)+"asupprimer.conll", Tools.cat(tab, "\n\n"));
		*/
		
		
		
		String dir = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Sources/Lapidaire/Lapidaireconll";
//		String xml = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Sources/Beroul/Beroulxml/beroul_final.xml";
		String xml = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Sources/Lapidaire/Lapidairexml/Lapidfp.xml";
		
		String home = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Optinel";
		
		String conllCompletPath1 = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/lapidaire.conll";
		String conllCompletPath2 = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/lapidaire.conll";
		
		String trainWapiti = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/beroul.wapiti";
		String testWapiti = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/lapidaire.wapiti";
		String wapitiModelPath = home+"/srcmf4OptinelGrand.wapitimodel";
		String wapitiTemplatePath = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/template_tlt";
		String wapitiOutput = home+"/beroul_ON_lapidaire.wapiti";
		String conllCompletPath = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/lapidaire.conll";
		String mateModelPath = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/grand.matemodel";		
		String mateOutput = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/beroul_ON_lapidaire.mate";
		String mateTrainSet = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/beroul.mategold";
		String mateTestset = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Wapiti/beroul_ON_lapidaire.mateppos";
		
		Pipeline.create()
//				.catDir(dir)
//				.setXmlPath(xml)
//				.fixParseError()
//				.injectPosXmlTei()
////				.injectPosTigerXml()
//				.injectPosTreeTaggerPosLemma()
//				.fixParseError()
//				.epurationLemmes()
//				.exportConllComplet(conllCompletPath)
			
//				.setConllContent1(conllCompletPath)
//				.formatWapiti()
////				.exportWapitiInput(trainWapiti)
//		
//				.setWapitiInput(trainWapiti)
////				.setTestContent(testWapiti)
//				.setWapitiModel(wapitiModelPath)
//				.setWapitiTemplate(wapitiTemplatePath)
//				.trainWapiti()
////				.testWapiti()

//				.exportWapitiOutput(wapitiOutput)
				
				.setConllContent1(conllCompletPath)
				.formatMateGold()
				.formatMatePPos(wapitiOutput)
				.setMateModelPath(mateModelPath)
//				.exportMateTrainset(mateTrainSet)
//				.exportMateTestset(mateTestset)
				.trainMate()
//				.testMate()
//				.exportMateOutput(mateOutput)
				
//				.evalMate()
//				.stopwatchStop()
				;				
		

	}
	
	public static void looGraal ()throws Exception{
		String home = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/graalloo";
		String conllCompletPath = "/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/cat/sgraal.conll";
		String conllTestPath = "/home/gael/Documents/SRCMF/Lattice/Corpus/Corpus_conll_SRCMF_GOLDPOS_GOLDPARSE/graal.conll";
//		String conllTestPathPrep = home + "/otinelPrep.conll";
		String wapitiModelPath = String.format("%s/%s.wapitimodel",home, FilenameUtils.getBaseName(conllCompletPath));
		String wapitiTemplatePath = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/Tests/template_tlt";
		String mateModelPath = String.format("%s/%s.matemodel",home, FilenameUtils.getBaseName(conllCompletPath));;
		String wapitiTrainSet = String.format("%s/%s.wapiti",home, FilenameUtils.getBaseName(conllCompletPath));
		String mateTrainSet = String.format("%s/%s.mate", home, FilenameUtils.getBaseName(conllCompletPath));
		String wapitiTestSet = String.format("%s/%s.wapiti",home, FilenameUtils.getBaseName(conllTestPath));
		String mateTestSet = String.format("%s/%sTest.mate", home, FilenameUtils.getBaseName(conllTestPath));
		String wapitiOutput = String.format("%s/%s_ON_%s.wapiti", home, FilenameUtils.getBaseName(mateModelPath),
				FilenameUtils.getBaseName(conllTestPath));
		String mateOutput = String.format("%s/%s_ON_%s.mate", home, FilenameUtils.getBaseName(mateModelPath),
				FilenameUtils.getBaseName(conllTestPath));
		String conllOutput = String.format("%s/%s_ON_%s.conll", home, FilenameUtils.getBaseName(mateModelPath),
				FilenameUtils.getBaseName(conllTestPath));
		
//		Pipeline.create().stopwatchStart()
//		.setConllContent1(conllTestPath)
//		.epurationLemmes()
//		.fixParseError()
//		
//		.formatWapiti()
//		.exportWapitiInput(wapitiTestSet)
////		.setWapitiModel(wapitiModelPath)
////		.setWapitiTemplate(wapitiTemplatePath)
////		.trainWapiti()
//		
////		.formatMateGold()
////		.setMateModelPath(mateModelPath)
////		.exportMateTrainset(mateTrainSet)
////		.trainMate()
//		
////		.set
////		.testWapiti()
////		.testMate()
//		.stopwatchStop()
//		;				

		
		Pipeline.create()
		.setConllContent1(conllCompletPath)
		.setConllContent2(conllTestPath)
		.setWapitiInput(wapitiTrainSet)
		.setWapitiTest(wapitiTestSet)
		.setWapitiModel(wapitiModelPath)
		.setWapitiTemplate(wapitiTemplatePath)
		.testWapiti()
		.exportWapitiOutput(wapitiOutput)

		.formatMateGold()
		.formatMatePPos()
		.setMateModelPath(mateModelPath)
		.testMate()
		.exportMateOutput(mateOutput)
//		
		.evalMate()
		.evalWapiti()
		;
		
	}
}
