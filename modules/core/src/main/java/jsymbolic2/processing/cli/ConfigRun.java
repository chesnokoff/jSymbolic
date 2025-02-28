package jsymbolic2.processing.cli;

import jsymbolic2.configuration.ConfigFileHeaderEnum;
import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.processing.FeatureExtractionJobProcessor;
import jsymbolic2.processing.PrintStreams;
import jsymbolic2.processing.UserFeedbackGenerator;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

class ConfigRun implements Consumer<String[]> {

    private PrintStreams printStreams;

    public ConfigRun(PrintStreams printStreams) {
        this.printStreams = printStreams;
    }


    @Override
    public void accept(String[] args) {
        printStreams = new PrintStreams(printStreams.status_print_stream(), printStreams.error_print_stream());

        // The path of the configuration file
        String config_file_path = args[1];
        // If only configuration file path is specified in args
        if (2 == args.length) {
            try {
                processWithConfigFileOnly(config_file_path, printStreams);
            } catch (Exception e) {
                UserFeedbackGenerator.printExceptionErrorMessage(printStreams.error_print_stream(), e);
            }
            return;
        }

        // If input and output file paths are specified in args, in addition to the configuration
        // file path

        // Must be either 2 or 5 command line arguments for CONFIG_RUN option
        if (5 != args.length) {
            UserFeedbackGenerator.indicateIncorrectCommandLineArgumentsAndEndExecution(printStreams.error_print_stream(), args);
        }

        ConfigRun.processFiveArguments(args, config_file_path, printStreams);
    }

    private static void processFiveArguments(String[] args, String config_file_path, PrintStreams printStreams) {
        // Parse args
        String input_file_path = args[2];
        String feature_values_save_path = args[3];
        String feature_definitions_save_path = args[4];
        List<ConfigFileHeaderEnum> config_file_headers_to_check = Arrays.asList(ConfigFileHeaderEnum.FEATURE_HEADER, ConfigFileHeaderEnum.OPTION_HEADER);

        // Parse configuration file and extract features based on its contents and the supplied
        // command line arguments
        try {
            UserFeedbackGenerator.printParsingConfigFileMessage(printStreams.status_print_stream(), config_file_path);
            ConfigurationFileData config_file_data = new ConfigurationFileValidatorTxtImpl().parseConfigFile(config_file_path, config_file_headers_to_check, printStreams.error_print_stream());
            List<File> input_file_list = List.of(new File(input_file_path));
            FeatureExtractionJobProcessor.extractAndSaveFeaturesConfigFileSettings(input_file_list,
                    config_file_data,
                    feature_values_save_path,
                    feature_definitions_save_path,
                    printStreams, false);
        } catch (Exception e) {
            UserFeedbackGenerator.printExceptionErrorMessage(printStreams.error_print_stream(), e);
        }
    }

    private void processWithConfigFileOnly(String config_file_path, PrintStreams printStreams) throws Exception {
        UserFeedbackGenerator.printParsingConfigFileMessage(printStreams.status_print_stream(), config_file_path);
        ConfigurationFileData config_file_data = new ConfigurationFileValidatorTxtImpl().parseConfigFileAllHeaders(config_file_path, printStreams.error_print_stream());
        List<File> input_file_list = config_file_data.getInputFileList().getValidFiles();
        String feature_values_save_path = config_file_data.getFeatureValueSavePath();
        String feature_definitions_save_path = config_file_data.getFeatureDefinitionSavePath();
        FeatureExtractionJobProcessor.extractAndSaveFeaturesConfigFileSettings(input_file_list,
                config_file_data,
                feature_values_save_path,
                feature_definitions_save_path,
                printStreams, false);
    }
}
