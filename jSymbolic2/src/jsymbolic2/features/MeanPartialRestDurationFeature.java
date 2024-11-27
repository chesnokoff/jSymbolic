package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that calculates the mean duration of rests in the piece, expressed as a fraction of
 * the duration of a quarter note. This is calculated voice-by-voice, where each rest included in the
 * calculation corresponds to a rest in one MIDI channel, regardless of what may or may not be happening
 * simultaneously in any other MIDI channels. Non-pitched (MIDI channel 10) notes ARE considered in this
 * calculation. Only channels containing at least one note are counted in this calculation. Rests shorter than
 * 0.1 of a quarter note are ignored in this calculation.
 *
 * @author Cory McKay
 */
public class MeanPartialRestDurationFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Mean Partial Rest Duration";
    }

    @Override()
    public String[] getDependencies() {
        return null;
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "R-47";
    }

    @Override()
    public String getDescription() {
        return "Mean duration of rests in the piece, expressed as a fraction of the duration of a quarter note. This is calculated voice-by-voice, where each rest included in the calculation corresponds to a rest in one MIDI channel, regardless of what may or may not be happening simultaneously in any other MIDI channels. Non-pitched (MIDI channel 10) notes ARE considered in this calculation. Only channels containing at least one note are counted in this calculation. Rests shorter than 0.1 of a quarter note are ignored in this calculation.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            if (sequence_info.rest_durations_separated_by_channel == null)
                value = 0.0;
            else {
                double[] rest_durations = mckay.utilities.staticlibraries.ArrayMethods.flattenMatrix(sequence_info.rest_durations_separated_by_channel);
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(rest_durations);
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
