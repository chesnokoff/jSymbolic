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
        if (args.length != 2)
            UserFeedbackGenerator.indicateIncorrectCommandLineArgumentsAndEndExecution(printStreams.error_print_stream(), args);

        String config_file_path = args[1];
        try {
            // Try parsing configuration file to see if it is valid
            UserFeedbackGenerator.printParsingConfigFileMessage(printStreams.status_print_stream(), config_file_path);
            new ConfigurationFileValidatorTxtImpl().parseConfigFileAllHeaders(config_file_path, printStreams.error_print_stream());

            // If the configuration file is valid as defined by this method
            UserFeedbackGenerator.simplePrintln(printStreams.status_print_stream(), "\n" + config_file_path + " is a valid configuration file that specifies features to be extracted, extraction options, input file paths and output file paths.\n");
        }

        // If the configuration file is not valid as defined by this method
        catch (Exception e) {
            UserFeedbackGenerator.simplePrintln(printStreams.status_print_stream(), "\n" + e.getMessage() + "\n");
        }
    }
}
