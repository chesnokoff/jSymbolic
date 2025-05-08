package jsymbolic2.processing.cli;

import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.processing.PrintStreams;
import jsymbolic2.processing.UserFeedbackGenerator;

import java.util.function.Consumer;

class ValidateConfigAllHeaders implements Consumer<String[]> {
    private final PrintStreams printStreams;

    public ValidateConfigAllHeaders(PrintStreams printStreams) {
        this.printStreams = printStreams;
    }

    @Override
    public void accept(String[] args) {
        // Check valid number of command line arguments
        if (2 != args.length)
            UserFeedbackGenerator.indicateIncorrectCommandLineArgumentsAndEndExecution(printStreams.errorPrintStream(), args);

        String config_file_path = args[1];
        try {
            // Try parsing configuration file to see if it is valid
            UserFeedbackGenerator.printParsingConfigFileMessage(printStreams.statusPrintStream(), config_file_path);
            new ConfigurationFileValidatorTxtImpl().parseConfigFileAllHeaders(config_file_path, printStreams.errorPrintStream());

            // If the configuration file is valid as defined by this method
            UserFeedbackGenerator.simplePrintln(printStreams.statusPrintStream(), "\n" + config_file_path + " is a valid configuration file that specifies features to be extracted, extraction options, input file paths and output file paths.\n");
        }

        // If the configuration file is not valid as defined by this method
        catch (Exception e) {
            UserFeedbackGenerator.simplePrintln(printStreams.statusPrintStream(), "\n" + e.getMessage() + "\n");
        }
    }
}
