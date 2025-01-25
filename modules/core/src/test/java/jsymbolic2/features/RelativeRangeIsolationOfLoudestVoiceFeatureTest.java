package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.ddmal.midiUtilities.MidiBuildEvent;
import org.junit.jupiter.api.Test;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Created by dinamix on 7/25/16.
 */
public class RelativeRangeIsolationOfLoudestVoiceFeatureTest {
    @Test
    public void extractFeature() throws Exception {
        Sequence test_tracks = new Sequence(Sequence.PPQ, 256);
        Track t1_tracks = test_tracks.createTrack();
        Track t2_tracks = test_tracks.createTrack();
        //Velocities here are always 64
        MidiEvent e_tracks3 = MidiBuildEvent.createNoteOnEventVel(0, 0, 0, 100);
        MidiEvent e_tracks4 = MidiBuildEvent.createNoteOffEvent(0, 2, 0);
        MidiEvent e_tracks1 = MidiBuildEvent.createNoteOnEvent(4, 0, 1);
        MidiEvent e_tracks2 = MidiBuildEvent.createNoteOffEvent(4, 2, 1);
        MidiEvent e_tracks5 = MidiBuildEvent.createNoteOnEventVel(7, 1, 1, 50);
        MidiEvent e_tracks6 = MidiBuildEvent.createNoteOffEvent(7, 2, 1);
        t1_tracks.add(e_tracks3);
        t2_tracks.add(e_tracks2);
        t1_tracks.add(e_tracks4);
        t2_tracks.add(e_tracks1);
        t1_tracks.add(e_tracks5);
        t1_tracks.add(e_tracks6);

        MIDIIntermediateRepresentations inter = new MIDIIntermediateRepresentations(test_tracks);
        Feature actual_common = new RelativeRangeIsolationOfLoudestVoiceFeature();
        double[] actual_chord_type = actual_common.extractFeature(test_tracks, inter, null);
        double[] expected_chord_type = {1};
        assertArrayEquals(expected_chord_type, actual_chord_type, 0.001);
    }

}