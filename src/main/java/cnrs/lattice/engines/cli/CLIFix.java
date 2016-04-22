package cnrs.lattice.engines.cli;

import org.apache.commons.cli.*;

/**
 * Class to instanciate the different CLI's options
 * Sub CLI for corpus preparation
 * @author Gael Guibon
 * @organization LaTTiCE-CNRS
 * @email <a href="mailto:gael.guibon@gmail.com">
 * 
 */
public class CLIFix {

	@SuppressWarnings({ "deprecation", "static-access" })
	public static Options parseCli() {
		Options options = new Options();

		Option help = new Option("help", "print this message");
		
		Option input = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("file path with srcmf format").create("in");
		Option output = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("output file path with srcmf format").create("out");
		
		options.addOption(help)
			.addOption(input)
			.addOption(output)
			;
		
		return options;

	}

}
