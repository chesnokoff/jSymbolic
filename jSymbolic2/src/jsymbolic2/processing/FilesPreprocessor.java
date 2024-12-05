package jsymbolic2.processing;

import ca.mcgill.music.ddmal.mei.MeiXmlReader;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilesPreprocessor {
    private final ArrayList<File> fullListOfFiles = new ArrayList<>();

    FilesPreprocessor(List<File> filesAndDirectories,
                      PrintStream errorPrintStream,
                      List<String> errorLog) {
        for (var file : filesAndDirectories) {
            if (file.isDirectory()) {
                List<Path> files_in_subfolder;
                try {
                    files_in_subfolder = Files.walk(Paths.get(file.getAbsolutePath())).filter(name -> new MusicFilter().accept(name.toFile())).collect(Collectors.toList());
                    for (Path path_in_subfolder : files_in_subfolder)
                        fullListOfFiles.add(path_in_subfolder.toFile());
                } catch (IOException ioe) {
                    String error_message = "Could not traverse files from this folder: " + file.getAbsolutePath();
                    if (null != errorPrintStream)
                        UserFeedbackGenerator.printWarningMessage(errorPrintStream, error_message);
                    errorLog.add(error_message);
                }
                continue;
            }
            if (MusicFilter.passesFilter(file))
                fullListOfFiles.add(file);
        }
    }

    public List<File> getMidiFilesList() {
        ArrayList<File> returnList = new ArrayList<>();
        for (var file : fullListOfFiles) {
            if (canExtractMidiFrom(file)) {
                returnList.add(file);
            }
        }
        return returnList;
    }

    public List<File> getMeiFilesList() {
        ArrayList<File> returnList = new ArrayList<>();
        for (var file : fullListOfFiles) {
            if (canExtractMeiFrom(file)) {
                returnList.add(file);
            }
        }
        return returnList;
    }

    private boolean canExtractMidiFrom(File file) {
        return validateFile(file) && (isValidMidiFile(file) || isValidMeiFile(file));
    }

    private boolean canExtractMeiFrom(File file) {
        return validateFile(file) && isValidMeiFile(file);
    }

    private boolean validateFile(File file) {
        if (null == file) {
            return false;
        }
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            return false;
        }
        if (!file.isFile()) {
            return false;
        }
        return file.canRead();
    }

    private boolean isValidMidiFile(File file) {
        try {
            MidiSystem.getMidiFileFormat(file);
        } catch (InvalidMidiDataException | IOException e) {
            return false;
        }
        return true;
    }

    private boolean isValidMeiFile(File file) {
        try {
            MeiXmlReader.loadFile(file);
        } catch (MeiXmlReader.MeiXmlReadException e) {
            return false;
        }
        return true;
    }
}
