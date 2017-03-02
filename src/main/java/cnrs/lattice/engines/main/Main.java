package cnrs.lattice.engines.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cnrs.lattice.engines.eval.Eval;
import cnrs.lattice.tools.corpus.Corpus;
import cnrs.lattice.tools.corpus.Fixer;
import cnrs.lattice.tools.utils.Tools;

import com.google.common.base.Stopwatch;


public class Main {
	
	private static String mode = "";
	private static String train = "";
	private static String test = "";
	private static String output = "";
	private static String template = "";
	private static String wapitimodel = "";
	private static String matemodel = "";
	//preparation corpus
	private static String dir = "";
	private static String xml = "";
	private static boolean tiger = false;
	private static String tthome = "";
	private static String input = "";
	private static int actual = 0;
	private static int predicted = 0;
	private static String column = "";
	private static String matcher = "";
	
	public static void main(String[] args) throws Exception {
//		
//		StringBuilder sb = new StringBuilder();
//		for(String line : Tools.StringToList(Tools.readFile("/home/gael/legier.conll")) ){
//			if(line.length() == 0){
//				sb.append("\n");
//				continue;
//			}
//				String[] cols = line.split("\t");
//				if(cols.length < 2) System.out.println(line);
//				sb.append( cols[1] + "\t" + cols[2] + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n");
//		}
//		Tools.ecrire("/home/gael/legier_empty.conll", sb.toString());
		
		
		
//		System.exit(0);
		
//		String res = Corpus.parseTeiXmlLakmeGPosGLemma("/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/otinelGoldPOSLemma/Mende_token_num_pos.xml");
//		Tools.ecrire("temp", res);
		
		
//		List<String> conllLines = Tools.path2liste("/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/otinelLemmes.conll");
//		List<String> lemmes = Tools.ListeRedondante(conllLines, 1);
//		String conllContent = Tools.readFile("/home/gael/Documents/SRCMF/PostTLT2015/Lakme/lakmeOtinelParsingFIXED/petit_ON_otinel.conll");
//		String newConllContent = Corpus.putValueInConll(conllContent, lemmes, 4);
//		Tools.ecrire("temp2", newConllContent);
			
//
/**
 * cli commons		
 */
		
		Options options = cnrs.lattice.engines.cli.CLIMain.parseCli();
		parseCliMain (args, options);
		Stopwatch stopwatch = Stopwatch.createUnstarted();
		switch(mode){
		case "1on1":
			OneOnOne.launcher(mode, train, test, output, template, wapitimodel, matemodel);
			break;
		case "corpus":
			Prepa.launcher(dir, xml, output, tiger, tthome);
			break;
		case "eval":
			String str = Eval.getAccuracy(actual, predicted, input);
			System.out.println(str);
			break;
		case "fix":
			stopwatch.start();
			String content = Fixer.fixIgnorerLine(input);
			content = Fixer.fixIgnorerLine(Tools.tempFile("temp", ".temp", content));
			content = Fixer.fixIgnorerLine(Tools.tempFile("temp", ".temp", content));
			content = Fixer.fixIgnorerLine(Tools.tempFile("temp", ".temp", content));
			content = Fixer.fixIgnorerLine(Tools.tempFile("temp", ".temp", content));
			Tools.ecrire(output, content);
			System.out.println(stopwatch);
			stopwatch.stop();
			break;
		case "subcorpus":
			String subcorpus = Corpus.getSubCorpus(input, matcher, Integer.parseInt(column));
			Tools.ecrire(output, subcorpus);
			break;
		}
//		
//		
//		
//		/**
//		 * 
//		 */
		
		
//		String path = "/home/gael/Documents/SRCMF/PostTLT2015/TestAPI/srcmf-nlp/lapidaire.conll";
//		String fixed = Fixer.fixSyntaxDuplicataAndAnnotatorTag(Tools.readFile(path),
//				11);
//		Tools.ecrire(path+"fixed", fixed);
		
//		
		
		/************************************
		 * 
		 */
//		String path = "/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/Mende_token_num.xml";
//		
////		String res = Corpus.parseTeiLakmeRegex(path);
////		System.out.println(res);
////		Tools.ecrire("/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/Mende_token_num.conll", res);
//		
////		String path2 = Tools.tempFile("lakme", "xml", Corpus.parseTeiLakmeRegex(path));
//		
//		String res2 = Corpus.parseTeiXmlLakme(path);
//		System.out.println(res2);
//		Tools.ecrire("/home/gael/Documents/SRCMF/PostTLT2015/Lakme/Otinel/Mende_token_num.TEST", res2);
	}


	
	private static void parseCliMain (String[] args, Options options) throws ParseException{
		HelpFormatter formatter = new HelpFormatter();
		String help = "\n"
//				+ dauphin
				+ "SRCMF-NLP\n\n"
				+ "By Gaël Guibon \t"
				+ "gael.guibon at gmail dot com\n\n"
				+ "Initially made for SRCMF purposes."
				+ "Please use the -help option in order to use the SRCMF-NLP :\n\n"
				+ "USAGE EXAMPLE:\n"
				+ "java -Xmx6G -jar srcmf.jar 1on1 -train /path/to/train -test /path/to/test -out /path/to/out\n\n";
		
		
		if((args.length ==0)){
			System.out.println("You need to specify a mode\n" + help);
			formatter.printHelp( "ant", options );
			System.exit(1);
		}		

		String arg = args[0];
		switch(arg){
			case "1on1":
				mode = args[0];
				Options options1on1 = cnrs.lattice.engines.cli.CLI1on1.parseCli();
				accessCLI1on1(args, options1on1);
				break;
			case "corpus":
				mode = args[0];
				Options optionsPrepa = cnrs.lattice.engines.cli.CLIPrepa.parseCli();
				accessCLIPrepa(args, optionsPrepa);
				break;
			case "eval":
				mode = args[0];
				Options optionsEval = cnrs.lattice.engines.cli.CLIEval.parseCli();
				accessCLIEval(args, optionsEval);
				break;
			case "fix":
				mode = args[0];
				Options optionsFix = cnrs.lattice.engines.cli.CLIFix.parseCli();
				accessCLIFix(args, optionsFix);
				break;
			case "subcorpus":
				mode = args[0];
				Options optionsCorpus = cnrs.lattice.engines.cli.CLICorpus.parseCli();
				accessCLICorpus(args, optionsCorpus);
				break;
			default:
				System.out.println("You need to specify a mode\n" + help);
				formatter.printHelp( "ant", options );
				System.exit(1);
				break;
		}
		
	}
	
	/**
	 * Access sub command line for 1on1 command
	 * @param line
	 * @param options
	 * @throws ParseException 
	 */
	private static void accessCLI1on1(String[] args, Options options) throws ParseException{
		HelpFormatter formatter = new HelpFormatter();
		String help = "\n\n"
//				+ dauphin
				+ "SRCMF-NLP\n\n"
				+ "By Gaël Guibon \t"
				+ "gael.guibon at gmail dot com\n\n"
				+ "Initially made for SRCMF purposes."
				+ "Please use the -help option in order to use the SRCMF-NLP :\n\n"
				+ "USAGE EXAMPLE:\n"
				+ "java -Xmx6G -jar srcmf.jar 1on1 -train /path/to/train -test /path/to/test -out /path/to/out";
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse( options, args);
		
		if((line.getOptions().length == 0)||(line.hasOption("help"))){
			System.out.println(help);
			formatter.printHelp( "ant", options );
			System.exit(1);
		}
		
		if(line.hasOption("train")){
			train = line.getOptionValue("train");
		}
		if(line.hasOption("test")){
			test = line.getOptionValue("test");
		}
		if(line.hasOption("template")){
			template = line.getOptionValue("template");
		}
		if(line.hasOption("wapitimodel")){
			wapitimodel = line.getOptionValue("wapitimodel");
		}
		if(line.hasOption("matemodel")){
			matemodel = line.getOptionValue("matemodel");
		}
		if(line.hasOption("out")){
			output = line.getOptionValue("out");
		}
	}
	
	
	/**
	 * access sub command line parsing for corpus command
	 * @param line
	 * @param options
	 * @throws ParseException 
	 */
	private static void accessCLIPrepa(String[] args, Options options) throws ParseException{
		HelpFormatter formatter = new HelpFormatter();
		String help = "\n\n"
//				+ dauphin
				+ "SRCMF-NLP\n\n"
				+ "By Gaël Guibon \t"
				+ "gael.guibon at gmail dot com\n\n"
				+ "Initially made for SRCMF purposes."
				+ "Please use the -help option in order to use the SRCMF-NLP :\n\n"
				+ "USAGE EXAMPLE:\n"
				+ "java -Xmx6G -jar srcmf.jar corpus -dir /path/to/dirOfConll -xml /path/to/file.xml -out /path/to/out -tthome /path/to/treetagger_install_dir";
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse( options, args);
		
		if((line.getOptions().length == 0)||(line.hasOption("help"))){
			System.out.println(help);
			formatter.printHelp( "ant", options );
			System.exit(1);
		}
		
		if(line.hasOption("dir")){
			dir = line.getOptionValue("dir");
		}
		if(line.hasOption("xml")){
			xml = line.getOptionValue("xml");
		}
		if(line.hasOption("tiger")){
			tiger = true;
		}
		if(line.hasOption("tthome")){
			tthome = line.getOptionValue("tthome");
		}
		if(line.hasOption("out")){
			output = line.getOptionValue("out");
		}
	}
	
	/**
	 * access sub command line parsing for eval command
	 * @param line
	 * @param options
	 * @throws ParseException 
	 */
	private static void accessCLIEval(String[] args, Options options) throws ParseException{
		HelpFormatter formatter = new HelpFormatter();
		String help = "\n\n"
//				+ dauphin
				+ "SRCMF-NLP\n\n"
				+ "By Gaël Guibon \t"
				+ "gael.guibon at gmail dot com\n\n"
				+ "Initially made for SRCMF purposes."
				+ "Please use the -help option in order to use the SRCMF-NLP :\n\n"
				+ "USAGE EXAMPLE:\n"
				+ "java -Xmx6G -jar srcmf.jar 1on1 -train /path/to/train -test /path/to/test -out /path/to/out";
		
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse( options, args);
		if((line.getOptions().length == 0)||(line.hasOption("help"))){
			System.out.println(help);
			formatter.printHelp( "ant", options );
			System.exit(1);
		}
		
		if(line.hasOption("in")){
			input = line.getOptionValue("in");
		}
		if(line.hasOption("a")){
			actual = Integer.parseInt(line.getOptionValue("a"));
		}
		if(line.hasOption("p")){
			predicted = Integer.parseInt(line.getOptionValue("p"));
		}
	}
	
	
	/**
	 * access sub command line parsing for cli command
	 * @param line
	 * @param options
	 * @throws ParseException 
	 */
	private static void accessCLIFix(String[] args, Options options) throws ParseException{
		HelpFormatter formatter = new HelpFormatter();
		String help = "\n\n"
//				+ dauphin
				+ "SRCMF-NLP\n\n"
				+ "By Gaël Guibon \t"
				+ "gael.guibon at gmail dot com\n\n"
				+ "Initially made for SRCMF purposes."
				+ "Please use the -help option in order to use the SRCMF-NLP :\n\n"
				+ "USAGE EXAMPLE:\n"
				+ "java -Xmx6G -jar srcmf.jar fix -in /path/to/in -out /path/to/out";
		
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse( options, args);
		if((line.getOptions().length == 0)||(line.hasOption("help"))){
			System.out.println(help);
			formatter.printHelp( "ant", options );
			System.exit(1);
		}
		
		if(line.hasOption("in")){
			input = line.getOptionValue("in");
		}
		if(line.hasOption("out")){
			output = line.getOptionValue("out");
		}
	}

	
	/**
	 * access sub command line parsing for cli command
	 * @param line
	 * @param options
	 * @throws ParseException 
	 */
	private static void accessCLICorpus(String[] args, Options options) throws ParseException{
		HelpFormatter formatter = new HelpFormatter();
		String help = "\n\n"
//				+ dauphin
				+ "SRCMF-NLP\n\n"
				+ "By Gaël Guibon \t"
				+ "gael.guibon at gmail dot com\n\n"
				+ "Initially made for SRCMF purposes."
				+ "Please use the -help option in order to use the SRCMF-NLP :\n\n"
				+ "USAGE EXAMPLE:\n"
				+ "java -Xmx6G -jar srcmf.jar fix -in /path/to/in -out /path/to/out";
		
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse( options, args);
		if((line.getOptions().length == 0)||(line.hasOption("help"))){
			System.out.println(help);
			formatter.printHelp( "ant", options );
			System.exit(1);
		}
		
		if(line.hasOption("in")){
			input = line.getOptionValue("in");
		}
		if(line.hasOption("col")){
			column = line.getOptionValue("col");
		}
		if(line.hasOption("m")){
			matcher = line.getOptionValue("m");
		}
		if(line.hasOption("out")){
			output = line.getOptionValue("out");
		}
	}
}


