package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that calculates the longest amount of uninterrupted time (expressed as a fraction of
 * the duration of a quarter note) in which no notes are sounding on at least one active MIDI channel.
 * Non-pitched (MIDI channel 10) notes ARE considered in this calculation. Only channels containing at least
 * one note are counted in this calculation. Rests shorter than 0.1 of a quarter note are ignored in this
 * calculation.
 *
 * @author Cory McKay
 */
public class LongestPartialRestFeature implements Feature {

    @Override()
    public String getName() {
        return "Longest Partial Rest";
    }

    @Override()
    public String getCode() {
        return "R-45";
    }

    @Override()
    public String getDescription() {
        return "Longest amount of uninterrupted time (expressed as a fraction of the duration of a quarter note) in which no notes are sounding on at least one active MIDI channel. Non-pitched (MIDI channel 10) notes ARE considered in this calculation. Only channels containing at least one note are counted in this calculation. Rests shorter than 0.1 of a quarter note are ignored in this calculation.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            if (sequence_info.rest_durations_separated_by_channel == null)
                value = 0.0;
            else {
                double[] rest_durations = mckay.utilities.staticlibraries.ArrayMethods.flattenMatrix(sequence_info.rest_durations_separated_by_channel);
                int index_of_largest = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(rest_durations);
                value = rest_durations[index_of_largest];
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
