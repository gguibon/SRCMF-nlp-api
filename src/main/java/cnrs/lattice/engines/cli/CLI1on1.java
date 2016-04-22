package cnrs.lattice.engines.cli;

import org.apache.commons.cli.*;

/**
 * Class to instanciate the different CLI's options
 * Sub CLI for 1on1 options
 * 
 * @author Gael Guibon
 * @organization LaTTiCE-CNRS
 * @email <a href="mailto:gael.guibon@gmail.com">
 * 
 */
public class CLI1on1 {

	@SuppressWarnings({ "deprecation", "static-access" })
	public static Options parseCli() {
		Options options = new Options();

		Option help = new Option("help", "print this message");

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
				
				
		options.addOption(help)
			.addOption(train)
			.addOption(test)
			.addOption(template)
			.addOption(wapitimodel)
			.addOption(matemodel)
			.addOption(output)
			;
		
		return options;

	}

}
