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
public class CLIPrepa {

	@SuppressWarnings({ "deprecation", "static-access" })
	public static Options parseCli() {
		Options options = new Options();

		Option help = new Option("help", "print this message");
		

		Option dir = OptionBuilder.withArgName("directory path").hasArg()
				.withDescription("the directory with only conll inside").create("dir");
		Option xml = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("the path to the XML file containing POS tags").create("xml");
		Option tiger = new Option("tiger", "toggle this if using tigerXML instead of TEI XML");
		Option tthome = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("path to the TreeTagger installation directory").create("tthome");
		Option out = OptionBuilder.withArgName("file path").hasArg()
				.withDescription("output file path").create("out");
		
				
		options.addOption(help)
			.addOption(dir)
			.addOption(xml)
			.addOption(tiger)
			.addOption(tthome)
			.addOption(out)
			;
		
		return options;

	}

}
