package cnrs.lattice.engines.cli;

import org.apache.commons.cli.*;

/**
 * Class to instanciate the different CLI's options
 * Main CLI to access sub CLI and modes options
 * 
 * @author Gael Guibon
 * @organization LaTTiCE-CNRS
 * @email <a href="mailto:gael.guibon@gmail.com">
 * 
 */
public class CLIMain {

	//@SuppressWarnings({ "deprecation", "static-access" })
	public static Options parseCli() {
		Options options = new Options();

		Option help = new Option("help", "print this message");
		Option mode = new Option("","CHOOSE A COMMAND BETWEEN:\n"
				+ "1on1 - mode to apply models on testset, or train then apply a model on a testset\n"
				+ "corpus - mode dedicated to corpus preparation\n"
				+ "eval - mode dedicated to evaluation"
				+ "\n"
				+ "fix - launch fixer tools (for now only \"Ignorer lines\")"
				+ ""
				);				
				
		options.addOption(help)
			.addOption(mode)
			;
		
		return options;

	}

}
