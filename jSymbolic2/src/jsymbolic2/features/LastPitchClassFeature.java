package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the pitch class of the last note in the piece. If there are multiple notes
 * with simultaneous attacks at the end of the piece, then the one with the lowest pitch is selected. A value
 * of 0 corresponds to C, and pitches increase chromatically by semitone in integer units (e.g. a value of 2
 * would mean that D is the most common pitch class). Enharmonic equivalents are treated as a single pitch
 * class. Set to 0 if there are no pitched notes.
 *
 * @author Cory McKay
 */
public class LastPitchClassFeature implements Feature {

    @Override()
    public String getName() {
        return "Last Pitch Class";
    }

    @Override()
    public String getCode() {
        return "P-37";
    }

    @Override()
    public String getDescription() {
        return "The pitch class of the last note in the piece. If there are multiple notes with simultaneous attacks at the end of the piece, then the one with the lowest pitch is selected. A value of 0 corresponds to C, and pitches increase chromatically by semitone in integer units (e.g. a value of 2 would mean that D is the most common pitch class). Enharmonic equivalents are treated as a single pitch class. Set to 0 if there are no pitched notes.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int lowest_last_pitch = 0;
            if (sequence_info.pitches_present_by_tick_excluding_rests.length > 0) {
                int last_tick_index = sequence_info.pitches_present_by_tick_excluding_rests.length;
                lowest_last_pitch = sequence_info.pitches_present_by_tick_excluding_rests[last_tick_index - 1][0];
            }
            int lowest_last_pitch_class = lowest_last_pitch % 12;
            value = (double) lowest_last_pitch_class;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
