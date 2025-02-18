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
import jsymbolic2.processing.SymbolicMusicFileUtilities;
import org.apache.commons.lang3.tuple.Pair;
import org.ddmal.jmei2midi.MeiSequence;
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
public class ProcessBenchmark {

  @Param("src/jmh/resources/configs/0_10000/all-features-config.txt")
  public String configFilePath;

  private ConfigurationFileData configFileData;
  MIDIFeatureProcessor processor;
  List<Pair<Sequence, MeiSequence>> list = new ArrayList<>();
  @Setup(Level.Trial)
  public void prepare() {
    List<ConfigFileHeaderEnum> configFileHeadersToCheck = Arrays.asList(
        ConfigFileHeaderEnum.values());
    try {
      configFileData = new ConfigurationFileValidatorTxtImpl().parseConfigFile(configFilePath,
          configFileHeadersToCheck, System.err);
      processor = new MIDIFeatureProcessor(configFileData.getWindowSize(),
          configFileData.getWindowOverlap(),
          FeatureExtractorAccess.getAllImplementedFeatureExtractors(),
          configFileData.getFeaturesToSaveBoolean(),
          configFileData.saveWindow(),
          configFileData.saveOverall(),
          configFileData.getFeatureValueSavePath(),
          configFileData.getFeatureDefinitionSavePath());
      for (File file : configFileData.getInputFileList().getValidFiles()) {

        Sequence full_sequence = null;
        MeiSequence mei_sequence = null;
        if (SymbolicMusicFileUtilities.isValidMidiFile(file)) {
          full_sequence = SymbolicMusicFileUtilities.getMidiSequenceFromMidiOrMeiFile(file,
              new ArrayList<>());
        } else if (SymbolicMusicFileUtilities.isValidMeiFile(file)) {
          mei_sequence = SymbolicMusicFileUtilities.getMeiSequenceFromMeiFile(file,
              new ArrayList<>());
          full_sequence = mei_sequence.getSequence();
        }
        list.add(Pair.of(full_sequence, mei_sequence));
      }
    } catch (Exception e) {
      throw new IllegalStateException("Failed to parse configuration file: " + e.getMessage(), e);
    }
  }

  @Benchmark
  public List<double[][]> benchmark() throws Exception {
    List<double[][]> results = new ArrayList<>();
    for (Pair<Sequence, MeiSequence> sequenceMeiSequencePair : list) {
      double[][] extracted = processor.extractFeatures(sequenceMeiSequencePair.getLeft(),
          sequenceMeiSequencePair.getRight());
      results.add(extracted);
    }
    return results;
  }
}
