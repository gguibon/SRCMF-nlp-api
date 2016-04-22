package cnrs.lattice.engines.cli;

import org.apache.commons.cli.*;

/**
 * Class to instanciate the different CLI's options
 * 
 * @author Gael Guibon
 * @organization LaTTiCE-CNRS
 * @email <a href="mailto:gael.guibon@gmail.com">
 * 
 */
public class CLI {

	@SuppressWarnings({ "deprecation", "static-access" })
	public static Options parseCli() {
		Options options = new Options();

		Option help = new Option("help", "print this message");
		Option mode = new Option("","choose a command:\n"
				+ "1on1 - apply models on testset, or train then apply a model on a testset\n");
		Option train = OptionBuilder.withArgName("path").hasArg()
				.withDescription("the train file - conll").create("train");
		Option test = OptionBuilder.withArgName("path").hasArg()
				.withDescription("the test file - conll").create("test");
		Option template = OptionBuilder.withArgName("path").hasArg()
				.withDescription("the template file - conll").create("template");
		Option wapitimodel = OptionBuilder.withArgName("path").hasArg()
				.withDescription("model for wapiti").create("wapitimodel");
		Option matemodel = OptionBuilder.withArgName("path").hasArg()
				.withDescription("model for mate").create("matemodel");
		Option output = OptionBuilder.withArgName("path").hasArg()
				.withDescription("the output file").create("out");
		
		
//options for corpus preparation
		Option dir = OptionBuilder.withArgName("directory path").hasArg()
				.withDescription("the directory with only conll inside").create("dir");
		Option xml = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("the path to the XML file containing POS tags").create("xml");
		Option tiger = new Option("tiger", "toggle this if using tigerXML instead of TEI XML");
		
		
		//options for evaluation
		Option input = OptionBuilder.withArgName("input file").hasArg()
				.withDescription("the basic input file").create("in");
		Option actual = OptionBuilder.withArgName("actual").hasArg()
				.withDescription("row index containing actual data").create("a");
		Option predicted = OptionBuilder.withArgName("predicted").hasArg()
				.withDescription("row index containing predicted data").create("p");
				
				
		options.addOption(help)
			.addOption(mode)
			.addOption(train)
			.addOption(test)
			.addOption(template)
			.addOption(wapitimodel)
			.addOption(matemodel)
			.addOption(output)
			.addOption(dir)
			.addOption(xml)
			.addOption(tiger)
			.addOption(input)
			.addOption(actual)
			.addOption(predicted)
			;
		
		return options;

	}

}
