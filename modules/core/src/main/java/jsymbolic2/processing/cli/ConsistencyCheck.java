package jsymbolic2.processing.cli;

import jsymbolic2.processing.MIDIReporter;
import jsymbolic2.processing.MusicFilter;
import jsymbolic2.processing.PrintStreams;
import jsymbolic2.processing.SymbolicMusicFileUtilities;
import jsymbolic2.processing.UserFeedbackGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

class ConsistencyCheck implements Consumer<String[]> {
    private final PrintStreams printStreams;

    public ConsistencyCheck(PrintStreams printStreams) {
        this.printStreams = printStreams;
    }

    @Override
    public void accept(String[] args) {
        // Check valid number of command line arguments
        if (2 != args.length) {
            UserFeedbackGenerator.indicateIncorrectCommandLineArgumentsAndEndExecution(printStreams.error_print_stream(), args);
        }
        try {
            // Prepare the set of files (after recursive directory parsing and extension filtering, if
            // appropriate), to report on
            File[] midi_or_mei_file_list = SymbolicMusicFileUtilities.getRecursiveListOfFiles(args[1],
                    new MusicFilter(),
                    printStreams.error_print_stream(),
                    new ArrayList<>());

            // Prepare and output the reports
            if (null != midi_or_mei_file_list) {
                String report = MIDIReporter.prepareConsistencyReports(midi_or_mei_file_list,
                        true,
                        true,
                        true);
                UserFeedbackGenerator.simplePrint(printStreams.status_print_stream(), report);
            }
        }
        // If the MIDI file is not valid
        catch (Exception e) {
            UserFeedbackGenerator.printExceptionErrorMessage(printStreams.error_print_stream(), e);
        }
    }
}
