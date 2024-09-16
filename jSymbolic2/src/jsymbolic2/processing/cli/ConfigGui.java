package jsymbolic2.processing.cli;

import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.processing.PrintStreams;
import jsymbolic2.processing.UserFeedbackGenerator;

import java.util.function.Consumer;

class ConfigGui implements Consumer<String[]> {
    private final PrintStreams printStreams;

    public ConfigGui(PrintStreams printStreams) {
        this.printStreams = printStreams;
    }

    @Override
    public void accept(String[] args) {
        if (args.length != 2)
            UserFeedbackGenerator.indicateIncorrectCommandLineArgumentsAndEndExecution(printStreams.error_print_stream(), args);

        String config_file_path = args[1];
        try {
            UserFeedbackGenerator.printParsingConfigFileMessage(printStreams.status_print_stream(), config_file_path);
            ConfigurationFileData config_file_data = new ConfigurationFileValidatorTxtImpl().parseConfigFileTwoThreeOrFour(config_file_path, printStreams.error_print_stream());
            new jsymbolic2.gui.OuterFrame(config_file_data);
        } catch (Exception e) {
            UserFeedbackGenerator.printExceptionErrorMessage(printStreams.error_print_stream(), e);
        }
    }
}
