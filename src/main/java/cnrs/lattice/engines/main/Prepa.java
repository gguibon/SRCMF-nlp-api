package cnrs.lattice.engines.main;

import java.io.IOException;

import cnrs.lattice.pipelines.Pipeline;

public class Prepa {
	private static String DIR = "";
	private static String XML = "";
	private static boolean TIGER = false;
	private static String OUTPUT = "";
	
	private Prepa(){
		
	}
	
	public static void launcher(String dirConll, String xml ,String output, boolean tiger) throws Exception{
		Prepa prepa = new Prepa();
		TIGER = tiger;
		DIR = dirConll;
		XML = xml;
		OUTPUT = output;
		prepa.run();
	}
	
	private Prepa run() throws IOException, Exception{
		if(TIGER){
		Pipeline.create()
				.catDir(DIR)
				.setXmlPath(XML)
				.fixParseError()
				.syntacticFixer()
//				.injectPosXmlTei()
				.injectPosTigerXml()
				.injectPosTreeTaggerPosLemma()
				.epurationLemmes()
				.exportConll(OUTPUT)
				;
		}else{
		Pipeline.create()
			.catDir(DIR)
			.setXmlPath(XML)
			.fixParseError()
			.syntacticFixer()
			.injectPosXmlTei()
//			.injectPosTigerXml()
			.injectPosTreeTaggerPosLemma()
			.epurationLemmes()
			.exportConll(OUTPUT)
			;
		}
		return this;
	}
}
