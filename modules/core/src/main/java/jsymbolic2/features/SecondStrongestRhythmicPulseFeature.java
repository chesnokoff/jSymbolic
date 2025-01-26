package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the bin index of the beat histogram peak with the second highest magnitude.
 *
 * @author Cory McKay
 */
public class SecondStrongestRhythmicPulseFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Second Strongest Rhythmic Pulse";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-21";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Bin index of the beat histogram peak with the second highest magnitude.";
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
            int max_index = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram.length; bin++) {
                if (sequence_info.beat_histogram[bin] > max) {
                    max = sequence_info.beat_histogram[bin];
                    max_index = bin;
                }
            }
            // Find the second highest bin
            double second_highest_magnitude = 0.0;
            int second_highest_index = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table.length; bin++) {
                if (sequence_info.beat_histogram_thresholded_table[bin][1] > second_highest_magnitude && bin != max_index) {
                    second_highest_magnitude = sequence_info.beat_histogram_thresholded_table[bin][1];
                    second_highest_index = bin;
                }
            }
            // Calculate the feature value
            value = (double) second_highest_index;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
