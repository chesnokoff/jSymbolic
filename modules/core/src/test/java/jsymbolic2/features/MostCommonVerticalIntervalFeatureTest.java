package jsymbolic2.features;

import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.ddmal.midiUtilities.MidiBuildEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Created by dinamix on 7/13/16.
 */
public class MostCommonVerticalIntervalFeatureTest {
    @BeforeEach
    public void setUp() throws Exception {

    }

    @AfterEach
    public void tearDown() throws Exception {

    }

    @Test
    public void extractFeature() throws Exception {
        Sequence test_tracks = new Sequence(Sequence.PPQ, 256);
        Track t1_tracks = test_tracks.createTrack();
        Track t2_tracks = test_tracks.createTrack();
        //Velocities here are always 64
        MidiEvent e_tracks3 = MidiBuildEvent.createNoteOnEvent(0, 0, 0);
        MidiEvent e_tracks4 = MidiBuildEvent.createNoteOffEvent(0, 3, 0);
        MidiEvent e_tracks1 = MidiBuildEvent.createNoteOnEvent(4, 0, 0);
        MidiEvent e_tracks2 = MidiBuildEvent.createNoteOffEvent(4, 1, 0);
        MidiEvent e_tracks5 = MidiBuildEvent.createNoteOnEvent(7, 0, 0);
        MidiEvent e_tracks6 = MidiBuildEvent.createNoteOffEvent(7, 3, 0);
        t1_tracks.add(e_tracks3);
        t2_tracks.add(e_tracks2);
        t1_tracks.add(e_tracks4);
        t2_tracks.add(e_tracks1);
        t1_tracks.add(e_tracks5);
        t1_tracks.add(e_tracks6);

        MIDIIntermediateRepresentations inter = new MIDIIntermediateRepresentations(test_tracks);

        double[] unwrapped_vertical_intervals = new VerticalIntervalHistogramFeature().extractFeature(test_tracks, inter, null);
        double[][] vertical_interval_other_features = new double[1][];
        vertical_interval_other_features[0] = unwrapped_vertical_intervals;
        MIDIFeatureExtractor actual_common = new MostCommonVerticalIntervalFeature();
        double[] vertical_intervals = new WrappedVerticalIntervalHistogramFeature().extractFeature(test_tracks, inter, vertical_interval_other_features);
        double[][] other_features = new double[1][];
        other_features[0] = vertical_intervals;
        double[] actual_chord_type = actual_common.extractFeature(test_tracks, inter, other_features);
        double[] expected_chord_type = {7};
        assertArrayEquals(expected_chord_type, actual_chord_type, 0.001);
    }

}