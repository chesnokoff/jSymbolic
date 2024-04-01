package jmh.runners;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BenchmarkRunner implements BenchmarkRunnerInterface {

    private final String regexep;
    private final int forks;
    private final int measurementIterations;
    private final int warmupIterations;
    private final TimeUnit timeUnit;
    private final int threadsNumber;

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

    @Override
    public void run() {
        Options optSingleThread = new OptionsBuilder()
                .include(regexep)
                .forks(forks)
                .measurementIterations(measurementIterations)
                .warmupIterations(warmupIterations)
                .timeUnit(timeUnit)
                .threads(threadsNumber)
                .param("configFilePath", configs.toArray(new String[0]))
                .build();
        try {
            new Runner(optSingleThread).run();
        } catch (RunnerException e) {
            throw new RuntimeException("Could not run benchmark. " + e);
        }
    }
}
