package jsymbolic2.processing;

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

  SequenceExtractor(List<File> filesList) {
    this.filesList = filesList;
  }

  List<Pair<String, Sequence>> getMIDISequences() {
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

  List<Pair<String, MeiSequence>> getMEISequences() {
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

  public static Sequence getMidiSequenceFromMidiFile(File file, List<String> error_log)
      throws IOException, InvalidMidiDataException {
    Sequence sequence = null;
    try {
      sequence = MidiSystem.getSequence(file);
    } catch (IOException e) {
      error_log.add("The specified path, " + file + ", does not refer to a valid file.");
      throw e;
    } catch (InvalidMidiDataException e) {
      error_log.add("The specified file, " + file + ", is not a valid MIDI or MEI file.");
      throw e;
    }
    return sequence;
  }
}
