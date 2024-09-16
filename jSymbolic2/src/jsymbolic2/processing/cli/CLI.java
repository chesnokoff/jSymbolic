package jsymbolic2.processing.cli;

import jsymbolic2.processing.PrintStreams;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CLI {
    private final PrintStreams printStreams = new PrintStreams(System.out, System.err);
    private final Map<String, Consumer<String[]>> commands = initCommands();

    private Map<String, Consumer<String[]>> initCommands() {
        Map<String, Consumer<String[]>> commands = new HashMap<>();
        commands.put("-сonfiggui", new ConfigGui(printStreams));
        commands.put("-сonfigrun", new ConfigRun(printStreams));
        commands.put("-validateconfigallheaders", new ValidateConfigAllHeaders(printStreams));
        commands.put("-validateconfigfeatureoption", new ValidateConfigFeatureOption(printStreams));
        commands.put("-consistencycheck", new ConsistencyCheck(printStreams));
        commands.put("-mididump", new MidiDump(printStreams));
        commands.put("-help", new Help(printStreams));
        return commands;
    }


    public void run(String[] args) {
        {
            // If there are no command line arguments
            if (args == null || args.length == 0) {
                new PlainGui(printStreams).accept(args);
                return;
            }
            // If there are command line arguments
            // If the first command line argument is known
            commands.getOrDefault(args[0], new CommandLine(printStreams)).accept(args);
            // If the first command line argument is not a known switch. This could mean that invalid command
            // line arguments were specified, or it could mean that there is no switch, but three valid I/O
            // file paths were specified.
        }
    }
}
