package jmh;


import jmh.runners.BenchmarkRunnerBuilder;
import jmh.runners.BenchmarkRunnerInterface;

public class Main {

    public static void main(String[] args) {
        BenchmarkRunnerInterface benchmarkRunnerSingleThread = new BenchmarkRunnerBuilder().createBenchmarkRunner();
        BenchmarkRunnerInterface benchmarkRunnerMultithreading = new BenchmarkRunnerBuilder()
                .setThreadsNumber(5)
                .createBenchmarkRunner();
        benchmarkRunnerSingleThread.run();
        benchmarkRunnerMultithreading.run();
    }
}
