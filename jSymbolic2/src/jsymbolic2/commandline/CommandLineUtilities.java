package jsymbolic2.commandline;

import jsymbolic2.processing.FeatureExtractionJobProcessor;
import jsymbolic2.processing.PrintStreams;
import jsymbolic2.processing.SaveInfo;
import jsymbolic2.processing.UserFeedbackGenerator;
import jsymbolic2.processing.WindowInfo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Holder class for static methods related to command line processing.
 *
 * @author Cory McKay and Tristano Tenaglia
 */
public enum CommandLineUtilities {
    ;
    /* PUBLIC STATIC METHODS ********************************************************************************/


    /**
     * Parses command line arguments that do not require accessing a configuration settings file. Carries
     * out feature extraction and saving of output files based on the specified arguments. Prints status
     * messages during processing to standard out, and error messages to standard err. Outputs an appropriate
     * error message if invalid arguments are specified.
     *
     * @param args Arguments with which jSymbolic was run at the command line. Note that this method is only
     *             equipped to deal with arguments that do not involve accessing a configuration settings
     *             file, and do not access GUI; any arguments that do in fact indicate either of these things
     *             this will be treated as invalid by this method.
     */
    public static void parseNoConfigFileCommandLineAndExtractAndSaveFeatures(String[] args) {
        // To hold feature extraction settings
        boolean convert_to_arff = true;
        boolean convert_to_csv = true;
        PrintStream status_print_stream = System.out;
        PrintStream error_print_stream = System.err;

        // Define search patterns for parsing command line arguments
        final String window_size_pattern = "\\d*.?\\d*";
        final String window_offset_pattern = "0?.\\d*";

        // Define flags
        final String arff_flag = "-arff";
        final String csv_flag = "-csv";
        final String window_flag = "-window";

        // Find out if CSV and/or ARFF files should be generated. Create reduced_args to hold the command line
        // arguments with CSV or ARFF flags, if any, removed.
        String[] reduced_args = new String[args.length];
        System.arraycopy(args, 0, reduced_args, 0, reduced_args.length);
        if (2 < args.length) {
            if (args[0].equalsIgnoreCase(csv_flag) ||
                    args[0].equalsIgnoreCase(arff_flag) ||
                    args[1].equalsIgnoreCase(csv_flag) ||
                    args[1].equalsIgnoreCase(arff_flag)) {

                List<String> reduced_args_list = new ArrayList<>();
                for (String arg : args)
                    if (!arg.equalsIgnoreCase(csv_flag) && !arg.equalsIgnoreCase(arff_flag))
                        reduced_args_list.add(arg);
                reduced_args = reduced_args_list.toArray(new String[0]);
            }
        }

        // If there are a proper number of command line arguments, assuming no windowing
        PrintStreams printStreams = new PrintStreams(status_print_stream, error_print_stream);
        if (3 == reduced_args.length) {
            String input_file_path = reduced_args[0];
            String ace_xml_feature_values_file_path = reduced_args[1];
            String ace_xml_feature_definitions_file_path = reduced_args[2];
            boolean save_features_for_each_window = false;
            boolean save_overall_recording_features = true;
            double window_size = 0.0;
            double window_overlap = 0.0;

            SaveInfo saveInfo = new SaveInfo(ace_xml_feature_values_file_path, ace_xml_feature_definitions_file_path, save_overall_recording_features, convert_to_arff, convert_to_csv);
            WindowInfo windowInfo = new WindowInfo(save_features_for_each_window, window_size, window_overlap);
            FeatureExtractionJobProcessor.extractAndSaveDefaultFeatures(input_file_path,
                    saveInfo,
                    windowInfo,
                    printStreams,
                    false);
        } else {
            // If proper command line arguments are present for windowing
            if (6 == reduced_args.length && reduced_args[0].equals(window_flag) &&
                    reduced_args[4].matches(window_size_pattern) &&
                    reduced_args[5].matches(window_offset_pattern)) {
                String input_file_path = reduced_args[1];
                String ace_xml_feature_values_file_path = reduced_args[2];
                String ace_xml_feature_definitions_file_path = reduced_args[3];
                boolean save_features_for_each_window = true;
                boolean save_overall_recording_features = false;
                double window_size = Double.parseDouble(reduced_args[4]);
                double window_overlap = Double.parseDouble(reduced_args[5]);

                SaveInfo saveInfo = new SaveInfo(ace_xml_feature_values_file_path, ace_xml_feature_definitions_file_path, save_overall_recording_features, convert_to_arff, convert_to_csv);
                WindowInfo windowInfo = new WindowInfo(save_features_for_each_window, window_size, window_overlap);
                FeatureExtractionJobProcessor.extractAndSaveDefaultFeatures(input_file_path,
                        saveInfo, windowInfo,
                        printStreams, false);
            }

            // Indicate invalid choice of command line arguments
            else
                UserFeedbackGenerator.indicateIncorrectCommandLineArgumentsAndEndExecution(System.err, args);
        }
    }


    /**
     * Returns instructions on how to properly format command line arguments for execution of jSymbolic,
     * including all available switches and options.
     *
     * @return Usage instructions for the jSymbolic command line interface.
     */
    public static String getCommandLineCorrectUsage() {
        return """
                
                Proper usage of jSymbolic via the command line requires one of the following command line argument configurations (see the manual for more details):
                
                1) No arguments (automaticallys run the GUI under default settings)
                2) <SymbolicMusicFileOrDirectoryInputPath> <AceXmlFeatureValuesOutputPath> <AceXmlFeatureDefinitionsOutputPath>
                \t-arff and/or -csv can be optionally be added before the above arguments.
                3) -window <SymbolicMusicFileInputPath> <AceXmlFeatureValuesOutputPath> <AceXmlFeatureDefinitionsOutputPath> <WindowLength> <WindowOverlapFraction>
                \t-arff and/or -csv can be optionally be added before the above arguments.
                4) -configrun <ConfigurationFilePath>
                5) -configrun <ConfigurationFilePath> <SymbolicMusicFileInputPath> <AceXmlFeatureValuesOutputPath> <AceXmlFeatureDefinitionsOutputPath>
                6) -configgui <ConfigurationFilePath>
                7) -validateconfigallheaders <ConfigurationFilePath>
                8) -validateconfigfeatureoption <ConfigurationFilePath>
                9) -consistencycheck <MidiFileOrMeiFileOrDirectoryPath>
                10) -mididump <MidiFileOrMeiFileOrDirectoryPath>
                11) -help
                
                Command line variable descriptions:
                * SymbolicMusicFileOrDirectoryInputPath: The file path of the MIDI or MEI file from which features are to be extracted. May alternatively be a directory holding one or more such files (sub-folders are searched recursively, and files must have qualifying MIDI or MEI extensions to be included).
                * AceXmlFeatureValuesOutputPath: The path of the ACE XML file to which extracted feature values will be saved.
                * AceXmlFeatureDefinitionsOutputPath: The path of the ACE XML file to to which metadata descriptions of the extracted features will be saved.
                * WindowLength: The duration in seconds of windows to be used during windowed feature extraction.
                * WindowOverlapFraction: A value between 0 and 1 specifying the fractional overlap between consecutive windows.
                * ConfigurationFilePath: The path of a configuration file to load jSymbolic settings from.
                * MidiOrMeiOrDirectoryPath: The path of a MIDI or MEI file to parse and report on. May alternatively be a directory holding one or more such files (sub-folders are searched recursively, and files must have qualifying MIDI or MEI extensions to be included). MEI files are converted to MIDI as part of this process.
                
                NOTE: All specified file paths must either be absolute or relative to the directory holding jSymbolic2.jar.
                
                """;
    }
}