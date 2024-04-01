package jmh.runners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BenchmarkRunnerBuilder implements BenchmarkRunnerBuilderInterface {
    private String regexep = ".*Benchmark.";
    private int forks = 5;
    private int measurementIterations = 10;
    private int warmupIterations = 5;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private List<String> configs = new ArrayList<>();

    private int threadsNumber = 1;

    public BenchmarkRunnerBuilder setRegexep(String regexep) {
        this.regexep = regexep;
        return this;
    }

    public BenchmarkRunnerBuilder setForks(int forks) {
        this.forks = forks;
        return this;
    }

    public BenchmarkRunnerBuilder setMeasurementIterations(int measurementIterations) {
        this.measurementIterations = measurementIterations;
        return this;
    }

    public BenchmarkRunnerBuilder setWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public BenchmarkRunnerBuilder setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public BenchmarkRunnerBuilder setThreadsNumber(int threadsNumber) {
        this.threadsNumber = threadsNumber;
        return this;
    }

    public BenchmarkRunnerBuilder setConfigFiles(List<String> configs) {
        this.configs = configs;
        return this;
    }

    @Override
    public BenchmarkRunnerInterface createBenchmarkRunner() {
        return new BenchmarkRunner(regexep, forks, measurementIterations, warmupIterations, timeUnit,
                threadsNumber, configs);
    }
}