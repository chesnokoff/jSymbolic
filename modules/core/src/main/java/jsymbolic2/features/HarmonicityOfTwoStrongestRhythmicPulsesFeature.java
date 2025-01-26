package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the bin index of the higher (in terms of bin index) of the two beat
 * histogram peaks with the highest magnitude, divided by the index of the lower (in terms of bin index) of
 * the two bins.
 *
 * @author Cory McKay
 */
public class HarmonicityOfTwoStrongestRhythmicPulsesFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Harmonicity of Two Strongest Rhythmic Pulses";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-22";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Bin index of the higher (in terms of bin index) of the two beat histogram peaks with the highest magnitude, divided by the index of the lower (in terms of bin index) of the two bins.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the bin with the highest magnitude
            double max = 0.0;
            // changed from 0 -> 1 for / by zero error
            int max_index = 1;
            for (int bin = 0; bin < sequence_info.beat_histogram.length; bin++) {
                if (sequence_info.beat_histogram[bin] > max) {
                    max = sequence_info.beat_histogram[bin];
                    max_index = bin;
                }
            }
            // Find the bin with the second highest magnitude
            double second_highest_bin_magnitude = 0.0;
            // changed from 0 -> 1 to avoid divide by zero error
            int second_hidgest_bin_index = 1;
            for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table.length; bin++) {
                if (sequence_info.beat_histogram_thresholded_table[bin][1] > second_highest_bin_magnitude && bin != max_index) {
                    second_highest_bin_magnitude = sequence_info.beat_histogram_thresholded_table[bin][1];
                    second_hidgest_bin_index = bin;
                }
            }
            // Calculate the featurevalue
            if (second_hidgest_bin_index == 0 || max_index == 0)
                value = 0.0;
            else if (max_index > second_hidgest_bin_index)
                value = (double) max_index / (double) second_hidgest_bin_index;
            else
                value = (double) second_hidgest_bin_index / (double) max_index;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
