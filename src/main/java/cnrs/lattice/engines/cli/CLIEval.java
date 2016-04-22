package cnrs.lattice.engines.cli;

import org.apache.commons.cli.*;

/**
 * Class to instanciate the different CLI's options
 * Sub CLI for evaluation
 * @author Gael Guibon
 * @organization LaTTiCE-CNRS
 * @email <a href="mailto:gael.guibon@gmail.com">
 * 
 */
public class CLIEval {

	@SuppressWarnings({ "deprecation", "static-access" })
	public static Options parseCli() {
		Options options = new Options();

		Option help = new Option("help", "print this message");
		

		Option input = OptionBuilder.withArgName("input file").hasArg()
				.withDescription("the basic input file").create("in");
		Option actual = OptionBuilder.withArgName("actual").hasArg()
				.withDescription("row index containing actual data").create("a");
		Option predicted = OptionBuilder.withArgName("predicted").hasArg()
				.withDescription("row index containing predicted data").create("p");
				
				
		options.addOption(help)
			.addOption(input)
			.addOption(actual)
			.addOption(predicted)
			;
		
		return options;

	}

}
