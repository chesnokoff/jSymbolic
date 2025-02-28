package jsymbolic2.processing.cli;

import jsymbolic2.commandline.CommandLineUtilities;
import jsymbolic2.processing.PrintStreams;
import jsymbolic2.processing.UserFeedbackGenerator;

import java.util.function.Consumer;

class Help implements Consumer<String[]> {
    private final PrintStreams printStreams;

    public Help(PrintStreams printStreams) {
        this.printStreams = printStreams;
    }

    @Override
    public void accept(String[] args) {
        UserFeedbackGenerator.simplePrintln(printStreams.status_print_stream(), CommandLineUtilities.getCommandLineCorrectUsage());
    }
}
