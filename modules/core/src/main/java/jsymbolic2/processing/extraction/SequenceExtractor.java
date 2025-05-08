package jsymbolic2.processing.extraction;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import org.apache.commons.lang3.tuple.Pair;
import org.ddmal.jmei2midi.MeiSequence;

public class SequenceExtractor {

  private final List<File> filesList;
  SequencePreprocessor sequencePreprocessor = new SequencePreprocessor();

  public SequenceExtractor(List<File> filesList) {
    this.filesList = filesList;
  }

  public List<Pair<String, Sequence>> getMIDISequences() {
    return filesList.stream()
        .filter(file -> file.getName().endsWith(".mid") || file.getName().endsWith(".midi"))
        .map(file -> {
          try {
            return Pair.of(file.getName(),
                sequencePreprocessor.apply(MidiSystem.getSequence(file)));
          } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException("Could not parse " + file.getAbsolutePath() + " " + e);
          }
        })
        .toList();
  }

  public List<Pair<String, MeiSequence>> getMEISequences() {
    return filesList.stream()
        .filter(file -> file.getName().endsWith(".mei"))
        .map(file -> {
          try {
            return Pair.of(file.getName(), new MeiSequence(file));
          } catch (InvalidMidiDataException e) {
            throw new RuntimeException("Could not parse " + file.getAbsolutePath() + " " + e);
          }
        })
        .toList();
  }
}
