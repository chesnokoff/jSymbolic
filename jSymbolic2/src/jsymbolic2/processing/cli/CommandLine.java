package jsymbolic2.processing.cli;

import jsymbolic2.configuration.ConfigFileHeaderEnum;
import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.processing.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static jsymbolic2.commandline.CommandLineSwitchEnum.default_config_file_path;

class CommandLine implements Consumer<String[]> {
    // To hold feature extraction settings
    final boolean CONVERT_TO_ARFF = true;
    final boolean CONVERT_TO_CSV = true;
    private final String ARFF_FLAG = "-arff";
    private final String CSV_FLAG = "-csv";
    private final PrintStreams printStreams;
    private final boolean GUI_PROCESSING = false;

    public CommandLine(PrintStreams printStreams) {
        this.printStreams = printStreams;
    }

    @Override
    public void accept(String[] args) {
        // Try parsing default configuration file if it exists
        if (args.length == 3 && Files.exists(Paths.get(default_config_file_path))) {
            // Try loading configuration file at default path
            try {
                processWithConfigFile(args);
            }

            // Run without configuration file if does not exist at default path
            catch (Exception ex) {
                UserFeedbackGenerator.simplePrintln(printStreams.status_print_stream(), "NON-CRITICAL WARNING: Could not find a configurations file called " + default_config_file_path + "  in the jSymbolic home directory that is valid under current settings. As a result, processing will continue using standard settings (unless specified manually). Although a default configurations file is by no means necessary to use jSymbolic, it is often convenient. You can save one at anytime either manually or using the jSymbolic GUI, if you wish (see the manual for more details).\n");
                parseNoConfigFileCommandLineAndExtractAndSaveFeatures(args);
            }
            return;
        }
        // Run without configuration file if does not exist at default path
        // Or run without configuration file if does not exist at default path if three are some other
        // number of command line arguments than 3
        if (args.length == 3) {
            UserFeedbackGenerator.simplePrintln(printStreams.status_print_stream(), "NON-CRITICAL WARNING: Could not find a configurations file called " + default_config_file_path + " in the jSymbolic home directory that is valid under current settings. As a result, processing will continue using standard settings (unless specified manually). Although a default configurations file is by no means necessary to use jSymbolic, it is often convenient. You can save one at anytime either manually or using the jSymbolic GUI, if you wish (see the manual for more details).\n");
        }
        parseNoConfigFileCommandLineAndExtractAndSaveFeatures(args);
    }

    private void processWithConfigFile(String[] args) throws Exception {
        List<File> input_file_list = List.of(new File(args[0]));
        String feature_values_save_path = args[1];
        String feature_definitions_save_path = args[2];
        List<ConfigFileHeaderEnum> config_file_headers_to_check = Arrays.asList(ConfigFileHeaderEnum.FEATURE_HEADER, ConfigFileHeaderEnum.OPTION_HEADER);
        UserFeedbackGenerator.printParsingConfigFileMessage(printStreams.status_print_stream(), default_config_file_path);
        ConfigurationFileData config_file_data = new ConfigurationFileValidatorTxtImpl().parseConfigFile(default_config_file_path, config_file_headers_to_check, printStreams.error_print_stream());
        FeatureExtractionJobProcessor.extractAndSaveFeaturesConfigFileSettings(input_file_list,
                config_file_data,
                feature_values_save_path,
                feature_definitions_save_path,
                printStreams, GUI_PROCESSING);
    }

    private void parseNoConfigFileCommandLineAndExtractAndSaveFeatures(String[] args) {
        // Find out if CSV and/or ARFF files should be generated. Create reduced_args to hold the command line
        // arguments with CSV or ARFF flags, if any, removed.
        String[] reduced_args = new String[args.length];
        System.arraycopy(args, 0, reduced_args, 0, reduced_args.length);
        if (args.length > 2 && firstOrSecondArgumentIsArffOrCsvFlag(args)) {
            reduced_args = createReducedArgs(args);
        }

        // If there are a proper number of command line arguments, assuming no windowing
        if (reduced_args.length == 3) {
            processNoWindowing(reduced_args);
            return;
        }
        // If proper command line arguments are present for windowing
        if (argumentsContainWindowFlag(reduced_args)) {
            processWindowing(reduced_args);
            return;
        }
        // Indicate invalid choice of command line arguments
        UserFeedbackGenerator.indicateIncorrectCommandLineArgumentsAndEndExecution(printStreams.error_print_stream(), args);
    }

    private void processNoWindowing(String[] reduced_args) {
        String input_file_path = reduced_args[0];
        String ace_xml_feature_values_file_path = reduced_args[1];
        String ace_xml_feature_definitions_file_path = reduced_args[2];
        boolean save_overall_recording_features = true;
        SaveInfo saveInfo = new SaveInfo(ace_xml_feature_values_file_path, ace_xml_feature_definitions_file_path, save_overall_recording_features, CONVERT_TO_ARFF, CONVERT_TO_CSV);

        boolean save_features_for_each_window = false;
        double window_size = 0.0;
        double window_overlap = 0.0;
        WindowInfo windowInfo = new WindowInfo(save_features_for_each_window, window_size, window_overlap);

        FeatureExtractionJobProcessor.extractAndSaveDefaultFeatures(input_file_path,
                saveInfo,
                windowInfo,
                printStreams,
                GUI_PROCESSING);
    }

    private void processWindowing(String[] reduced_args) {
        String input_file_path = reduced_args[1];
        String ace_xml_feature_values_file_path = reduced_args[2];
        String ace_xml_feature_definitions_file_path = reduced_args[3];
        boolean save_features_for_each_window = true;
        boolean save_overall_recording_features = false;
        double window_size = Double.parseDouble(reduced_args[4]);
        double window_overlap = Double.parseDouble(reduced_args[5]);

        SaveInfo saveInfo = new SaveInfo(ace_xml_feature_values_file_path, ace_xml_feature_definitions_file_path, save_overall_recording_features, CONVERT_TO_ARFF, CONVERT_TO_CSV);
        WindowInfo windowInfo = new WindowInfo(save_features_for_each_window, window_size, window_overlap);
        FeatureExtractionJobProcessor.extractAndSaveDefaultFeatures(input_file_path,
                saveInfo, windowInfo,
                printStreams, GUI_PROCESSING);
    }

    private String[] createReducedArgs(String[] args) {
        String[] reduced_args;
        List<String> reduced_args_list = new ArrayList<>();
        for (String arg : args) {
            if (!arg.equalsIgnoreCase(CSV_FLAG) && !arg.equalsIgnoreCase(ARFF_FLAG))
                reduced_args_list.add(arg);
        }
        reduced_args = reduced_args_list.toArray(new String[0]);
        return reduced_args;
    }

    private boolean firstOrSecondArgumentIsArffOrCsvFlag(String[] args) {
        return args[0].equalsIgnoreCase(CSV_FLAG) ||
                args[0].equalsIgnoreCase(ARFF_FLAG) ||
                args[1].equalsIgnoreCase(CSV_FLAG) ||
                args[1].equalsIgnoreCase(ARFF_FLAG);
    }

    private boolean argumentsContainWindowFlag(String[] reduced_args) {
        // Define search patterns for parsing command line arguments
        String windowSizePattern = "\\d*.?\\d*";
        String windowFlag = "-window";
        String windowOffsetPattern = "0?.\\d*";
        return reduced_args.length == 6 && reduced_args[0].equals(windowFlag) &&
                reduced_args[4].matches(windowSizePattern) &&
                reduced_args[5].matches(windowOffsetPattern);
    }
}
