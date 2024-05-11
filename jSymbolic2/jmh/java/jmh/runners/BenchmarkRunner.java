package jmh.runners;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BenchmarkRunner implements BenchmarkRunnerInterface {

    // Regex to find benchmark classes to run
    private final String regexep;

    // Number of forks
    private final int forks;

    private final int measurementIterations;
    private final int warmupIterations;
    // Time units for measuring
    private final TimeUnit timeUnit;
    private final int threadsNumber;

    // List of configs' paths
    private final List<String> configs;


    BenchmarkRunner(String regexep, int forks, int measurementIterations,
                    int warmupIterations, TimeUnit timeUnit, int threadsNumber, List<String> configs) {
        this.regexep = regexep;
        this.forks = forks;
        this.measurementIterations = measurementIterations;
        this.warmupIterations = warmupIterations;
        this.timeUnit = timeUnit;
        this.threadsNumber = threadsNumber;
        this.configs = configs;
    }

    /**
     * Starts benchmarking
     */
    @Override
    public void run() {
        Options optSingleThread = new OptionsBuilder()
                .include(regexep)
                .forks(forks)
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.JSON)
                .result("result.json")
                .measurementIterations(measurementIterations)
                .warmupIterations(warmupIterations)
                .timeUnit(timeUnit)
                .threads(threadsNumber)
                .param("configFilePath", configs.toArray(new String[0]))
                .build();
        try {
            Collection<RunResult> collection = new Runner(optSingleThread).run();

        } catch (RunnerException e) {
            throw new RuntimeException("Could not run benchmark. " + e);
        }
    }
}
