package jsymbolic2.processing.cli;

import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.processing.PrintStreams;
import jsymbolic2.processing.UserFeedbackGenerator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static jsymbolic2.commandline.CommandLineSwitchEnum.default_config_file_path;

class PlainGui implements Consumer<String[]> {
    private final PrintStreams printStreams;

    public PlainGui(PrintStreams printStreams) {
        this.printStreams = printStreams;
    }

    @Override
    public void accept(String[] args) {
        // Try parsing default configuration file if it exists
        if (Files.exists(Paths.get(default_config_file_path))) {
            try {
                UserFeedbackGenerator.printParsingConfigFileMessage(printStreams.status_print_stream(), default_config_file_path);
                ConfigurationFileData config_file_data = new ConfigurationFileValidatorTxtImpl().parseConfigFileTwoThreeOrFour(default_config_file_path, printStreams.error_print_stream());
                new jsymbolic2.gui.OuterFrame(config_file_data);
            } catch (Exception e) {
                new jsymbolic2.gui.OuterFrame(null);
            }
        } else {
            new jsymbolic2.gui.OuterFrame(null);
        }
    }
}
