package jsymbolic2.processing;

import ace.datatypes.DataBoard;
import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.featureutils.FeatureExtractorAccess;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.ddmal.jmei2midi.MeiSequence;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;

import javax.sound.midi.Sequence;
import javax.swing.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Static methods for performing outer layer feature extraction jobs.
 *
 * @author Cory McKay and Tristano Tenaglia
 */
public enum FeatureExtractionJobProcessor {
    ;
    /* PUBLIC STATIC METHODS ********************************************************************************/

    /**
     * Extracts features from all the files in the specified files_and_folders_to_parse list and saves them in
     * an ACE XML feature values file and (if specified in the passed arguments) as Weka ARFF and/or CSV
     * files. Qualifying files (i.e. MIDI or MEI) in directories specified in this list are also processed.
     * Also saves an ACE XML feature definitions file. Only the features specified in features_to_extract will
     * be extracted. Provides status updates as processing continues. Any errors occurring during processing
     * are reported on error_print_stream, and are also collected for summarization at the end of processing.
     * Processing continues even if errors are encountered, with three exceptions: if the JVM runs out of
     * memory, if an MEI-specific feature is set to be extracted from a non-MEI file, or if
     * MIDIFeatureProcessor cannot be initialized. In the latter three cases, execution is terminated
     * immediately.
     *
     * @param paths_of_files_or_folders_to_parse A list of files and folders from which features should be
     *                                           extracted.
     * @param saveInfo
     * @param features_to_extract                An array indicating which features are to be saved. This
     *                                           array is ordered to match the array returned by the
     *                                           FeatureExtractorAccess.findSpecifiedFeatures.
     * @param windowInfo
     * @param printStreams
     * @param gui_processing                     True if this method is being called by a GUI, false
     *                                           otherwise. If it is true, then error summaries will only
     *                                           be partially printed, and out of memory errors will result
     *                                           in an error window being displayed and a direct printing
     *                                           of the associated error message to standard error.
     * @return A list of errors that may have occurred during processing.
     * Will be empty if no errors occurred. Note that this often
     * simply duplicates what is written to error_print_stream.
     */
    public static List<String> extractAndSaveSpecificFeatures(List<File> paths_of_files_or_folders_to_parse,
                                                              SaveInfo saveInfo, boolean[] features_to_extract,
                                                              WindowInfo windowInfo,
                                                              PrintStreams printStreams, boolean gui_processing) {
        // To hold reports of errors that may occur. Note that this often simply duplicates what is written to
        // error_print_stream.
        List<String> error_log = new ArrayList<>();

        try {
            // Prepare the feature extractor
            MIDIFeatureProcessor processor = new MIDIFeatureProcessor(windowInfo,
                    FeatureExtractorAccess.getAllImplementedFeatureExtractors(),
                    features_to_extract,
                    saveInfo.save_overall_recording_features());
            FilesPreprocessor filesPreprocessor = new FilesPreprocessor(paths_of_files_or_folders_to_parse,
                    printStreams.error_print_stream(), error_log);
            FilesReader filesReader = new FilesReader(List.of(new SequencePreprocessor()));
            List<ImmutablePair<String, Sequence>> midiPairs = filesReader.extractMidi(filesPreprocessor.getMidiFilesList());
            List<ImmutablePair<String, MeiSequence>> meiPairs = filesReader.extractMei(filesPreprocessor.getMeiFilesList());
            // Extract features and save the feature values in DataBoard
            DataBoard dataBoard = FeatureExtractionJobProcessor.extractFeatures(midiPairs, meiPairs,
                    processor,
                    saveInfo.feature_values_save_path(),
                    printStreams, error_log,
                    gui_processing);

            // Save features from DataBoard
            FeatureExtractionJobProcessor.saveFeatures(dataBoard, saveInfo.feature_definitions_save_path(),
                    saveInfo,
                    printStreams.status_print_stream());
            // Indicate that processing is done
            UserFeedbackGenerator.printExecutionFinished(printStreams.status_print_stream());
            // Return any errors that may have occurred
            return error_log;
        } catch (Exception e) {
            UserFeedbackGenerator.printExceptionErrorMessage(printStreams.error_print_stream(), e);
            error_log.add(e + ":" + e.getMessage());
            return error_log;
        }
    }


    /**
     * Extracts features from the specified path_of_file_or_folder_to_parse and saves feature values in an ACE
     * XML feature values file and (if specified in the passed arguments) as Weka ARFF and/or CSV files. Also
     * saves an ACE XML feature definitions file. The default jSymbolic features will be extracted, which will
     * be less than the total number of implemented features. If path_of_file_or_folder_to_parse refers to a
     * folder rather than a file, then all qualifying files (i.e. MIDI or MEI) in it have their features
     * extracted. Provides status updates as processing continues. Any errors occurring during processing are
     * reported on error_print_stream, and are also collected for summarization at the end of processing.
     * Processing continues even if errors are encountered, with three exceptions: if the JVM runs out of
     * memory, if an MEI-specific feature is set to be extracted from a non-MEI file, or if
     * MIDIFeatureProcessor cannot be initialized. In the latter three cases, execution is terminated
     * immediately.
     *
     * @param path_of_file_or_folder_to_parse The path of a file to extract features from, or of a directory
     *                                        holding files to extract features from.
     * @param saveInfo
     * @param windowInfo
     * @param printStreams
     * @param gui_processing                  True if this method is being called by a GUI, false
     *                                        otherwise. If it is true, then error summaries will only
     *                                        be partially printed, and out of memory errors will result
     *                                        in an error window being displayed and a direct printing
     *                                        of the associated error message to standard error.
     * @return A list of errors that may have occurred during processing.
     * Will be empty if no errors occurred. Note that this often
     * simply duplicates what is written to error_print_stream.
     */
    public static List<String> extractAndSaveDefaultFeatures(String path_of_file_or_folder_to_parse,
                                                             SaveInfo saveInfo, WindowInfo windowInfo,
                                                             PrintStreams printStreams, boolean gui_processing) {
        return FeatureExtractionJobProcessor.extractAndSaveSpecificFeatures(List.of(new File(path_of_file_or_folder_to_parse)),
                saveInfo,
                FeatureExtractorAccess.getDefaultFeaturesToSave(),
                windowInfo,
                printStreams,
                gui_processing);
    }


    /**
     * Extracts features from all the files in the specified paths_of_files_or_folders_to_parse list and saves
     * them in an ACE XML feature values file and (if specified in the config_file_data) as Weka ARFF and/or
     * CSV files. Qualifying files (i.e. MIDI or MEI) in directories specified in this
     * paths_of_files_or_folders_to_parse list are also processed. Also saves an ACE XML feature definitions
     * file. Extraction settings are based on the config_file_data, except for the output save paths, which
     * are specified in feature_values_save_path and feature_definitions_save_path, and the input files, which
     * are specified in paths_of_files_or_folders_to_parse. Provides status updates as processing continues.
     * Any errors occurring during processing are reported on error_print_stream, and are also collected for
     * summarization at the end of processing. Processing continues even if errors are encountered, with three
     * exceptions: if the JVM runs out of memory, if an MEI-specific feature is set to be extracted from a
     * non-MEI file, or if MIDIFeatureProcessor cannot be initialized. In the latter three cases, execution is
     * terminated immediately.
     *
     * @param paths_of_files_or_folders_to_parse A list of files and folders from which features should be
     *                                           extracted.
     * @param config_file_data                   Extraction settings parsed from a jSymbolic configuration
     *                                           settings file.
     * @param feature_values_save_path           The path to save the extracted features to (in the form of
     *                                           an ACE XML feature values file).
     * @param feature_definitions_save_path      The path to save the feature definitions of all features
     *                                           to be extracted (in the form of an ACE XML feature
     *                                           definitions file).
     * @param printStreams
     * @param gui_processing                     True if this method is being called by a GUI, false
     *                                           otherwise. If it is true, then error summaries will only
     *                                           be partially printed, and out of memory errors will result
     *                                           in an error window being displayed and a direct printing
     *                                           of the associated error message to standard error.
     * @return A list of errors that may have occurred during processing.
     * Will be empty if no errors occurred. Note that this often
     * simply duplicates what is written to error_print_stream.
     */
    public static List<String> extractAndSaveFeaturesConfigFileSettings(List<File> paths_of_files_or_folders_to_parse,
                                                                        ConfigurationFileData config_file_data,
                                                                        String feature_values_save_path,
                                                                        String feature_definitions_save_path,
                                                                        PrintStreams printStreams, boolean gui_processing) {
        SaveInfo saveInfo = new SaveInfo(feature_values_save_path, feature_definitions_save_path, config_file_data.saveOverall(), config_file_data.convertToArff(), config_file_data.convertToCsv());
        WindowInfo windowInfo = new WindowInfo(config_file_data.saveWindow(), config_file_data.getWindowSize(), config_file_data.getWindowOverlap());
        return FeatureExtractionJobProcessor.extractAndSaveSpecificFeatures(paths_of_files_or_folders_to_parse,
                saveInfo, config_file_data.getFeaturesToSaveBoolean(),
                windowInfo,
                printStreams, gui_processing);
    }

    /* PRIVATE STATIC METHODS *******************************************************************************/

    /**
     * Extracts features from all the files in the specified files_and_folders_to_parse list and saves them in
     * an ACE XML feature values file. Qualifying files (i.e. MIDI or MEI) in directories specified in this
     * list are also processed. Provides status updates as processing continues. Any errors occurring during
     * processing are reported on error_print_stream, and are also collected for summarization at the end of
     * processing. Processing continues even if errors are encountered, with two exceptions: if the JVM runs
     * out of memory or if an MEI-specific feature is set to be extracted from a non-MEI file. In the latter
     * two cases, execution is terminated immediately. Also saves the feature definitions of the features
     * selected for extraction in an ACE XML feature definitions file.
     *
     * @param midiSequences            A list of pairs. Key is name of sequence, value is Sequence to process.
     * @param meiSequences             A list of pairs. Key is name of sequence, value is MeiSequence to process.
     * @param processor                The MIDIFeatureProcessor holding feature extraction settings.
     * @param feature_values_save_path The path to save the extracted features to in the form of an ACE
     *                                 XML feature values file.
     * @param printStreams
     * @param error_log                A list of errors encountered so far. Errors are added to it if
     *                                 encountered. This will be printed to error_print_stream at the end
     *                                 of processing.
     * @param gui_processing           True if this method is being called by a GUI, false otherwise. If
     *                                 it is true, then error summaries will only be partially printed,
     *                                 and out of memory errors will result in an error window being
     *                                 displayed and a direct printing	of the associated error message to
     *                                 standard error.
     */
    private static DataBoard extractFeatures(List<ImmutablePair<String, Sequence>> midiSequences,
                                             List<ImmutablePair<String, MeiSequence>> meiSequences,
                                             MIDIFeatureProcessor processor,
                                             String feature_values_save_path,
                                             PrintStreams printStreams, List<String> error_log,
                                             boolean gui_processing) {
        // Verify that, if MEI-specific features have been chosen to be extracted, then none of the files
        // chosen to be parsed are non-MEI files. End execution if some are.

        if (midiSequences.isEmpty() && meiSequences.isEmpty()) {
            throw new IllegalArgumentException("There are no files to parse!");
        }

        int midiSequenceNumberToProcess = midiSequences.size();
        int meiSequenceNumberToProcess = meiSequences.size();
        if (processor.containsMeiFeatures()) {
            midiSequenceNumberToProcess = 0;
        }
        UserFeedbackGenerator.printFeatureExtractionStartingMessage(printStreams.status_print_stream(), midiSequenceNumberToProcess + meiSequenceNumberToProcess);
        // Extract features from each file
        for (int i = 0; i < midiSequenceNumberToProcess; ++i) {
            FeatureExtractionJobProcessor.extractFeaturesFromSequence(midiSequences.get(i).getLeft(), midiSequences.get(i).getRight(),
                    processor, i,
                    midiSequenceNumberToProcess + meiSequenceNumberToProcess,
                    printStreams, error_log, gui_processing);

        }
        for (int i = 0; i < meiSequenceNumberToProcess; ++i) {
            FeatureExtractionJobProcessor.extractFeaturesFromMeiSequence(
                    midiSequences.get(i).getLeft(),
                    processor,
                    midiSequenceNumberToProcess + i,
                    midiSequenceNumberToProcess + meiSequenceNumberToProcess,
                    printStreams,
                    error_log,
                    gui_processing,
                    meiSequences.get(i).getRight().getNonMidiStorage(),
                    meiSequences.get(i).getRight().getSequence());

        }
        DataBoard dataBoard = null;
        try {
            dataBoard = processor.generateDataBoard();
        } catch (Exception e) {
            UserFeedbackGenerator.printExceptionErrorMessage(printStreams.error_print_stream(), e);
            error_log.add(e + ": " + e.getMessage());
        }

        // Finalize the saving of the feature values ACE XML file
        UserFeedbackGenerator.printGeneratingAceXmlFeatureValuesFile(printStreams.status_print_stream(), feature_values_save_path);

        // Indicate that feature extraction is done, and provide a summary of results
        UserFeedbackGenerator.printFeatureExtractionCompleteMessage(printStreams.status_print_stream(),
                feature_values_save_path,
                midiSequenceNumberToProcess + meiSequenceNumberToProcess);

        // Print the error log summary
        UserFeedbackGenerator.printErrorSummary(printStreams.error_print_stream(), error_log, gui_processing);
        return dataBoard;
    }


    /**
     * Extracts all available features from a single MIDI file. Any errors encountered are printed to standard
     * error. Save the features as they are extracted to an ACE XML feature values file, and save the feature
     * definitions in an ACE XML feature definitions file.
     *
     * @param name                     Name of given sequence.
     * @param sequence                 The sequence to process.
     * @param processor                The MIDIFeatureProcessor to extract features with.
     * @param current_extraction_index Indicates the number of this particular input file in the overall
     *                                 extraction order.
     * @param total_files_to_process   The total number of input files that are being processed.
     * @param printStreams
     * @param error_log                A list of errors encountered so far. Errors are added to it if
     *                                 encountered.
     * @param gui_processing           True if this method is being called by a GUI, false otherwise. If
     *                                 it is true, then out of memory errors will result in an error window
     *                                 being displayed and a direct printing of the associated error
     *                                 message to standard error.
     */
    private static void extractFeaturesFromSequence(String name, Sequence sequence,
                                                    MIDIFeatureProcessor processor,
                                                    int current_extraction_index,
                                                    int total_files_to_process,
                                                    PrintStreams printStreams, List<String> error_log,
                                                    boolean gui_processing) {
        FeatureExtractionJobProcessor.extractFeaturesFromMeiSequence(name, processor, current_extraction_index, total_files_to_process, printStreams, error_log, gui_processing, null, sequence);
    }


    private static void extractFeaturesFromMeiSequence(String name,
                                                       MIDIFeatureProcessor processor,
                                                       int current_extraction_index,
                                                       int total_files_to_process,
                                                       PrintStreams printStreams,
                                                       List<String> error_log,
                                                       boolean gui_processing, MeiSpecificStorage nonMidiStorage, Sequence sequence) {
        try {
            // Extract features from input_file_path and save them in an ACE XML feature values file
            UserFeedbackGenerator.printFeatureExtractionProgressMessage(printStreams.status_print_stream(), name, current_extraction_index, total_files_to_process);
            processor.extractFeaturesFromSequence(name, sequence, nonMidiStorage);
            UserFeedbackGenerator.printFeatureExtractionDoneAFileProgressMessage(printStreams.status_print_stream(), name, current_extraction_index, total_files_to_process);
        } catch (OutOfMemoryError e) // Terminate execution if this happens
        {
            FeatureExtractionJobProcessor.processOutOfMemory(name, printStreams.error_print_stream(), gui_processing);
        } catch (Exception e) {
            String error_message = "Problem extracting features from " + name + "." +
                    "\n\tDetailed error message: " + e + ": " + e.getMessage();
            UserFeedbackGenerator.printErrorMessage(printStreams.error_print_stream(), error_message);
            error_log.add(error_message);
            e.printStackTrace(printStreams.error_print_stream());
        }
    }

    private static void processOutOfMemory(String name, PrintStream error_print_stream, boolean gui_processing) {
        String error_message = "The Java Runtime ran out of memory while processing:\n" +
                "     " + name + "\n" +
                "Please rerun jSymbolic with more memory assigned to the Java runtime heap.\n\n";
        UserFeedbackGenerator.printErrorMessage(error_print_stream, error_message);
        if (gui_processing) {
            System.err.println(error_message);
            java.awt.Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null,
                    error_message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        System.exit(-1);
    }


    /**
     * Convert the given ACE XML Feature Values file to a Weka ARFF and/or to a CSV file. Each file will have
     * the same file name as the ACE XML Feature Values file, but with an appropriately modified extension.
     * Does nothing if ace_xml_feature_values_file_path is null or empty, or if both save_ARFF and save_CSV
     * are false (null is returned in any of these cases).
     *
     * <p>The ARFF file will have a relation name "Music".</p
     *
     * @param dataBoard                  DataBoard with extracted features.
     * @param featureDefinitionsSavePath The path to save the ACE XML feature definitions file
     *                                   matching the featureValuesSavePath file.
     * @param saveInfo
     * @param status_print_stream
     */
    private static void saveFeatures(DataBoard dataBoard,
                                     String featureDefinitionsSavePath,
                                     SaveInfo saveInfo,
                                     PrintStream status_print_stream) {
        if (1 > dataBoard.getNumOverall()) {
            return;
        }
        FeaturesSaver.SaveXML(dataBoard, featureDefinitionsSavePath, saveInfo.feature_values_save_path(),
                status_print_stream);
        if (saveInfo.save_arff_file()) {
            FeaturesSaver.SaveARFF(dataBoard, saveInfo.feature_values_save_path(), status_print_stream);
        }
        if (saveInfo.save_csv_file()) {
            FeaturesSaver.SaveCSV(dataBoard, saveInfo.feature_values_save_path(), status_print_stream);
        }
    }
}