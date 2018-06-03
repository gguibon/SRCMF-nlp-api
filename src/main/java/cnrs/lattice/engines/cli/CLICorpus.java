package cnrs.lattice.engines.cli;

import org.apache.commons.cli.*;

/**
 * Class to instanciate the different CLI's options
 * Sub CLI for corpus processes
 * @author Gael Guibon
 * @organization LaTTiCE-CNRS
 * @email <a href="mailto:gael.guibon@gmail.com">
 * 
 */
public class CLICorpus {

	@SuppressWarnings({ "deprecation", "static-access" })
	public static Options parseCli() {
		Options options = new Options();

		Option help = new Option("help", "print this message");
		

		Option input = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("the all in one conll file").create("in");
		Option col = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("the column number from 0 where the subfile is specified").create("col");
		Option matcher = OptionBuilder.withArgName("string to match").hasArg()
				.withDescription("the string to match the content for extraction").create("m");
		Option output = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("the subcorpus output filepath").create("out");
		
				
		options.addOption(help)
			.addOption(input)
			.addOption(col)
			.addOption(matcher)
			.addOption(output)
			;
		
		return options;

	}

}
