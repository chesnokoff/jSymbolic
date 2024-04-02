package jmh.runners;

/**
 * This interface implements building Benchmark runner
 */
public interface BenchmarkRunnerBuilderInterface {
    /**
     * @return Returns benchmark runner that was built
     */
    BenchmarkRunnerInterface createBenchmarkRunner();
}
