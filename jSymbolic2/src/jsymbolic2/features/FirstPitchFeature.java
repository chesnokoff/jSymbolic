package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the MIDI pitch value of the first note in the piece. If there are multiple
 * notes with simultaneous attacks at the beginning of the piece, then the one with the lowest pitch is
 * selected. Set to 0 if there are no pitched notes.
 *
 * @author Cory McKay
 */
public class FirstPitchFeature implements Feature {

    @Override()
    public String getName() {
        return "First Pitch";
    }

    @Override()
    public String getCode() {
        return "P-34";
    }

    @Override()
    public String getDescription() {
        return "The MIDI pitch value of the first note in the piece. If there are multiple notes with simultaneous attacks at the beginning of the piece, then the one with the lowest pitch is selected. Set to 0 if there are no pitched notes.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int lowest_first_pitch = 0;
            if (sequence_info.pitches_present_by_tick_excluding_rests.length > 0)
                lowest_first_pitch = sequence_info.pitches_present_by_tick_excluding_rests[0][0];
            value = (double) lowest_first_pitch;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
