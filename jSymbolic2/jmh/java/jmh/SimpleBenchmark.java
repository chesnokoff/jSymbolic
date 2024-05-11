package jmh;

import jsymbolic2.configuration.ConfigFileHeaderEnum;
import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.processing.FeatureExtractionJobProcessor;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

@State(Scope.Thread)
public class SimpleBenchmark {

    @Param("jmh/resources/configs/0_10000/all-features-config.txt")
    public static String configFilePath;

    @State(Scope.Thread)
    public static class BenchmarkState {
        private ConfigurationFileData configFileData;

        @Setup
        public void prepare() {
            List<ConfigFileHeaderEnum> configFileHeadersToCheck = Arrays.asList(ConfigFileHeaderEnum.values());
            try {
                configFileData = new ConfigurationFileValidatorTxtImpl().parseConfigFile(configFilePath, configFileHeadersToCheck, System.err);
            } catch (Exception e) {
                throw new RuntimeException("Could not parse config file. " + e);
            }
        }
    }

    @Benchmark
    public ConfigurationFileData benchmark(BenchmarkState state, Blackhole blackhole) {
        ConfigurationFileData configFileData = state.configFileData;
        FeatureExtractionJobProcessor.extractAndSaveFeaturesConfigFileSettings(configFileData.getInputFileList().getValidFiles(),
                configFileData,
                configFileData.getFeatureValueSavePath(),
                configFileData.getFeatureValueSavePath(),
                new PrintStream(OutputStream.nullOutputStream()),
                new PrintStream(OutputStream.nullOutputStream()),
                false);
        return configFileData;
    }
}