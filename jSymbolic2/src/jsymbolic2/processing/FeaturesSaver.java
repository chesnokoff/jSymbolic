package jsymbolic2.processing;

import ace.datatypes.DataBoard;

import java.io.File;
import java.io.PrintStream;

/**
 * This class is used to save extracted features to files. Extracted features are passed in DataBoard
 */
public class FeaturesSaver {

    /**
     * Validates filepath to write and writes into CSV file
     *
     * @param dataBoard DataBoard with information about features to save
     * @param filePath  Path to save the info
     * @throws RuntimeException Throws an informative
     *                          Runtime exception if it cannot write into file.
     */
    static void SaveCSV(DataBoard dataBoard, String filePath, PrintStream status_print_stream)
            throws RuntimeException {
        File file = new File(addExtension(filePath, "csv"));
        validateFile(file);
        try {
            UserFeedbackGenerator.printGeneratingCsvFile(status_print_stream, file.getPath());
            dataBoard.saveToCSV(file, true,
                    true, true,
                    true, true);
        } catch (Exception e) {
            throw new RuntimeException("Could not write CSV into " + filePath);
        }
    }

    /**
     * Validates filepath to write and writes into ARFF file
     *
     * @param dataBoard DataBoard with information about features to save
     * @param filePath  Path to save the info
     * @throws RuntimeException Throws an informative
     *                          Runtime exception if it cannot write into file.
     */
    static void SaveARFF(DataBoard dataBoard, String filePath,
                         PrintStream status_print_stream) throws RuntimeException {
        File file = new File(addExtension(filePath, "arff"));
        validateFile(file);
        try {
            UserFeedbackGenerator.printGeneratingArffFile(status_print_stream, file.getPath());
            dataBoard.saveToARFF("Music", file,
                    true, true);
        } catch (Exception e) {
            throw new RuntimeException("Could not write CSV into " + filePath);
        }
    }

    /**
     * Validates filepath to write and writes into ARFF file
     *
     * @param dataBoard           DataBoard with information about features to save
     * @param filePathDefinitions Path to save descriptions of features. May be null
     * @param filePathValues      Path to save values of features
     * @throws RuntimeException Throws an informative
     *                          Runtime exception if it cannot write into file.
     */
    static void SaveXML(DataBoard dataBoard, String filePathDefinitions, String filePathValues,
                        PrintStream status_print_stream) throws RuntimeException {
        File fileDefinitions = null;
        if (filePathDefinitions != null) {
            fileDefinitions = new File(addExtension(filePathDefinitions, "xml"));
            validateFile(fileDefinitions);
        }
        File fileValues = new File(addExtension(filePathValues, "xml"));
        validateFile(fileValues);
        try {
            UserFeedbackGenerator.printGeneratingAceXmlFeatureDefinitionsFile(status_print_stream, fileValues.getPath());
            UserFeedbackGenerator.printGeneratingAceXmlFeatureValuesFile(status_print_stream, fileValues.getPath());
            dataBoard.saveXMLFiles(null, fileDefinitions,
                    fileValues, null);
        } catch (Exception e) {
            throw new RuntimeException("Cannot not write XML: " + e + " " + e.getMessage());
        }
    }


    /**
     * Checks filepath and add extension to string
     *
     * @param filePath  Filepath to check
     * @param extension Extension to add
     * @return Filepath with required extension
     */
    private static String addExtension(String filePath, String extension) {
        if (filePath.endsWith("." + extension)) {
            return filePath;
        }
        StringBuilder dirBuilder = new StringBuilder(filePath);
        StringBuilder nameBuilder = new StringBuilder(filePath);
        int nameIndex = dirBuilder.lastIndexOf("/");
        nameBuilder.delete(0, nameIndex + 1);
        dirBuilder.delete(nameIndex + 1, filePath.length());
        if (nameBuilder.toString().contains(".")) {
            int dotIndex = nameBuilder.toString().indexOf(".");
            nameBuilder.delete(dotIndex, nameBuilder.length());
            nameBuilder.append(".").append(extension);
        }
        return dirBuilder.append(nameBuilder).toString();
    }

    /**
     * Checks file existence and rights to write
     *
     * @param file File to check
     * @throws IllegalArgumentException Throws exception if file does not exist
     *                                  or has no write access
     */
    private static void validateFile(File file) {
        if (file.exists() && !file.canWrite()) {
            throw new IllegalArgumentException("Cannot write to " + file.getPath() + ".");
        }
    }
}