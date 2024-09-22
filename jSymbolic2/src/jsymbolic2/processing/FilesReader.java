package jsymbolic2.processing;

import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.ddmal.jmei2midi.MeiSequence;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilesReader {
    public List<ImmutablePair<String, Sequence>> extractMidi(Collection<File> files) throws Exception {
        List<ImmutablePair<String, Sequence>> sequences = new ArrayList<>(files.size());
        for (File file : files) {
            sequences.add(ImmutablePair.of(file.getName(), extractMidi(file)));
        }
        return sequences;
    }

    public List<ImmutablePair<String, MeiSequence>> extractMei(Collection<File> files) throws Exception {
        List<ImmutablePair<String, MeiSequence>> sequences = new ArrayList<>(files.size());
        for (File file : files) {
            sequences.add(ImmutablePair.of(file.getName(), extractMei(file)));
        }
        return sequences;
    }

    private Sequence extractMidi(File file) throws Exception {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            throw new IllegalArgumentException("File " + file.getPath() + " is invalid.");
        }
        Sequence sequence;
        if (isValidMidiFile(file)) {
            sequence = getMidiSequenceFromMidiFile(file);
        } else if (isValidMeiFile(file)) {
            sequence = getMidiSequenceFromMeiFile(file);
        } else {
            throw new Exception("The specified file, " + file + ", is not a valid MIDI or MEI file.");
        }
        return sequence;
    }

    private MeiSequence extractMei(File file) throws Exception {
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            throw new IllegalArgumentException("File " + file.getPath() + " is invalid.");
        }
        MeiSequence sequence;
        if (isValidMeiFile(file))
            sequence = getMeiSequenceFromMeiFile(file);
        else {
            throw new Exception("The specified file, " + file + ", is not a valid MEI file.");
        }
        return sequence;
    }


    private Sequence getMidiSequenceFromMidiFile(File file)
            throws IOException, InvalidMidiDataException {
        Sequence sequence;
        try {
            sequence = MidiSystem.getSequence(file);
        } catch (IOException e) {
            throw e;
        }
        return sequence;
    }

    private Sequence getMidiSequenceFromMeiFile(File file)
            throws MeiXmlReader.MeiXmlReadException, InvalidMidiDataException {
        Sequence sequence = null;
        try {
            MeiSequence meiSequence = new MeiSequence(file);
            sequence = meiSequence.getSequence();
        } catch (MeiXmlReader.MeiXmlReadException | InvalidMidiDataException e) {
            throw e;
        }
        return sequence;
    }

    private MeiSequence getMeiSequenceFromMeiFile(File file)
            throws InvalidMidiDataException, MeiXmlReader.MeiXmlReadException {
        try {
            return new MeiSequence(file);
        } catch (InvalidMidiDataException | MeiXmlReader.MeiXmlReadException e) {
            throw e;
        }
    }

    private boolean isValidMidiFile(File file) {
        try {
            MidiSystem.getMidiFileFormat(file);
        } catch (InvalidMidiDataException | IOException e) {
            return false;
        }
        return true;
    }

    private boolean isValidMeiFile(File file) {
        try {
            MeiXmlReader.loadFile(file);
        } catch (MeiXmlReader.MeiXmlReadException e) {
            return false;
        }
        return true;
    }
}
