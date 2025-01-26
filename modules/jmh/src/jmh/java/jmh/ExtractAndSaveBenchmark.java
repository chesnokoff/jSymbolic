package jmh;

import jsymbolic2.configuration.ConfigFileHeaderEnum;
import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.processing.FeatureExtractionJobProcessor;
import jsymbolic2.processing.PrintStreams;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ExtractAndSaveBenchmark {

  @Param("src/jmh/resources/configs/all-features-config.txt")
  public String configFilePath;

  private ConfigurationFileData configFileData;

  @Setup(Level.Trial)
  public void prepare() {
    List<ConfigFileHeaderEnum> configFileHeadersToCheck = Arrays.asList(ConfigFileHeaderEnum.values());
    try {
      configFileData = new ConfigurationFileValidatorTxtImpl().parseConfigFile(configFilePath, configFileHeadersToCheck, System.err);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to parse configuration file: " + e.getMessage(), e);
    }
  }

  @Benchmark
  public void benchmark() {
    FeatureExtractionJobProcessor.extractAndSaveFeaturesConfigFileSettings(
        configFileData.getInputFileList().getValidFiles(),
        configFileData,
        configFileData.getFeatureValueSavePath(),
        configFileData.getFeatureValueSavePath(),
        new PrintStreams(new PrintStream(OutputStream.nullOutputStream()), new PrintStream(OutputStream.nullOutputStream())),
        false
    );
  }
}
