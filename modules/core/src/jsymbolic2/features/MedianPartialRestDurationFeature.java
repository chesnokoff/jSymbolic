package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that calculates the median duration of rests in the piece, expressed as a fraction of
 * the duration of a quarter note. This is calculated voice-by-voice, where each rest included in the
 * calculation corresponds to a rest in one MIDI channel, regardless of what may or may not be happening
 * simultaneously in any other MIDI channels. Non-pitched (MIDI channel 10) notes ARE considered in this
 * calculation. Only channels containing at least one note are counted in this calculation. Rests shorter than
 * 0.1 of a quarter note are ignored in this calculation.
 *
 * @author Cory McKay
 */
public class MedianPartialRestDurationFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Median Partial Rest Duration";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-49";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Median duration of rests in the piece, expressed as a fraction of the duration of a quarter note. This is calculated voice-by-voice, where each rest included in the calculation corresponds to a rest in one MIDI channel, regardless of what may or may not be happening simultaneously in any other MIDI channels. Non-pitched (MIDI channel 10) notes ARE considered in this calculation. Only channels containing at least one note are counted in this calculation. Rests shorter than 0.1 of a quarter note are ignored in this calculation.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            if (sequence_info.rest_durations_separated_by_channel == null)
                value = 0.0;
            else {
                double[] rest_durations = mckay.utilities.staticlibraries.ArrayMethods.flattenMatrix(sequence_info.rest_durations_separated_by_channel);
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getMedianValue(rest_durations);
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
