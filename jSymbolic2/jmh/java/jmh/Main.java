package jmh;


import jmh.process.CommandParser;
import jmh.runners.BenchmarkRunnerBuilder;
import jmh.runners.BenchmarkRunnerInterface;

public class Main {

    public static void main(String[] args) {
        CommandParser cmd = new CommandParser(args);
        if (cmd.askedHelp()) {
            cmd.printHelp();
            return;
        }
        BenchmarkRunnerInterface benchmarkRunner = cmd.generateBenchmarkBuilder();
        benchmarkRunner.run();
    }
}
