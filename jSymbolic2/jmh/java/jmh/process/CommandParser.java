package jmh.process;

import jmh.runners.BenchmarkRunnerBuilder;
import jmh.runners.BenchmarkRunnerInterface;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandParser {

    private final Options options;
    // Regex to find benchmark classes to run
    private String regexep = ".*Benchmark.";
    private int forks = 5;
    private int measurementIterations = 10;
    private int warmupIterations = 5;

    private boolean neenHelp = false;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private List<String> configs = new ArrayList<>();

    private int threadsNumber = 1;

    public CommandParser(String[] args) {
        options = getOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException("Could not parse command. " + e);
        }
        parse(cmd);
    }

    private void parse(CommandLine cmd) {
        if (cmd.hasOption("h")) {
            neenHelp = true;
            return;
        }
        if (cmd.getOptionValue("r") != null) {
            regexep = cmd.getOptionValue("r");
        }
        if (cmd.getOptionValue("f") != null) {
            forks = Integer.parseInt(cmd.getOptionValue("f"));
        }
        if (cmd.getOptionValue("w") != null) {
            warmupIterations = Integer.parseInt(cmd.getOptionValue("w"));
        }
        if (EnumUtils.isValidEnum(TimeUnit.class, cmd.getOptionValue("t"))) {
            timeUnit = TimeUnit.valueOf(cmd.getOptionValue("t"));
        }
        if (cmd.getOptionValue("c") != null) {
            configs = Arrays.asList(cmd.getOptionValue("c").split(" "));
        }
        if (cmd.getOptionValue("i") != null) {
            measurementIterations = Integer.parseInt(cmd.getOptionValue("i"));
        }
        if (cmd.getOptionValue("t") != null) {
            measurementIterations = Integer.parseInt(cmd.getOptionValue("t"));
        }
    }

    private Options getOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "regex for benchmark classes");
        options.addOption("r", "regex", true, "regex for benchmark classes");
        options.addOption("f", "fork", true, "regex for benchmark classes");
        options.addOption("w", "warmup", true, "regex for benchmark classes");
        options.addOption("i", "measure_iterations", true, "regex for benchmark classes");
        options.addOption("u", "time_unit", true, "regex for benchmark classes");
        options.addOption("c", "config", true, "regex for benchmark classes");
        options.addOption("t", "threads", true, "regex for benchmark classes");
        return options;
    }

    public boolean askedHelp() {
        return neenHelp;
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ant", options);
    }

    public BenchmarkRunnerBuilder generateBenchmarkBuilder() {
        return new BenchmarkRunnerBuilder()
                .setRegexep(regexep)
                .setForks(forks)
                .setMeasurementIterations(measurementIterations)
                .setTimeUnit(timeUnit)
                .setThreadsNumber(threadsNumber)
                .setConfigFiles(configs);
    }
}