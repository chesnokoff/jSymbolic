package jmh;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.sound.midi.Sequence;
import jsymbolic2.configuration.ConfigFileHeaderEnum;
import jsymbolic2.configuration.ConfigurationFileData;
import jsymbolic2.configuration.txtimplementation.ConfigurationFileValidatorTxtImpl;
import jsymbolic2.featureutils.FeatureExtractorAccess;
import jsymbolic2.processing.MIDIFeatureProcessor;
import jsymbolic2.processing.SequenceExtractor;
import jsymbolic2.processing.WindowInfo;
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
public class ExtractBenchmark {

  @Param("src/jmh/resources/configs/0_10000/all-features-config.txt")
  public String configFilePath;

  MIDIFeatureProcessor processor;
  List<Sequence> list = new ArrayList<Sequence>();
  List<double[][][]> results = new ArrayList<>();

  @Setup(Level.Trial)
  public void prepare() {
    List<ConfigFileHeaderEnum> configFileHeadersToCheck = Arrays.asList(
        ConfigFileHeaderEnum.values());
    try {
      ConfigurationFileData configFileData = new ConfigurationFileValidatorTxtImpl().parseConfigFile(
          configFilePath,
          configFileHeadersToCheck, System.err);
      processor = new MIDIFeatureProcessor(new WindowInfo(configFileData.saveWindow(),
          configFileData.getWindowSize(), configFileData.getWindowOverlap()),
          FeatureExtractorAccess.getAllImplementedFeatureExtractors(),
          configFileData.getFeaturesToSaveBoolean(),
          configFileData.saveOverall());
      for (File file : configFileData.getInputFileList().getValidFiles()) {
        Sequence sequence = SequenceExtractor.getMidiSequenceFromMidiFile(file,
            new ArrayList<>());
        list.add(sequence);
      }
    } catch (Exception e) {
      throw new IllegalStateException("Failed to parse configuration file: " + e.getMessage(), e);
    }
  }

  @Benchmark
  public List<Sequence> benchmark() throws Exception {
    for (Sequence sequenceMeiSequencePair : list) {
      double[][][] extracted = processor.getFeatures(new Sequence[]{sequenceMeiSequencePair}, null);
      results.add(extracted);
    }
    return list;
  }
}
