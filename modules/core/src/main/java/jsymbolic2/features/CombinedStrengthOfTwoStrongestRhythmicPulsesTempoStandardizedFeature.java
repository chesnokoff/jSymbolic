package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the sum of the magnitudes of the two tempo-standardized beat histogram
 * peaks with the highest magnitudes.
 *
 * @author Cory McKay
 */
public class CombinedStrengthOfTwoStrongestRhythmicPulsesTempoStandardizedFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Combined Strength of Two Strongest Rhythmic Pulses - Tempo Standardized";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-63";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Sum of the magnitudes of the two tempo-standardized beat histogram peaks with the highest magnitudes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the bin with the highest magnitude
            double highest_magnitude = 0.0;
            int index_of_bin_with_highest_magnitude = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram_120_bpm_standardized.length; bin++) {
                if (sequence_info.beat_histogram_120_bpm_standardized[bin] > highest_magnitude) {
                    highest_magnitude = sequence_info.beat_histogram_120_bpm_standardized[bin];
                    index_of_bin_with_highest_magnitude = bin;
                }
            }
            // Find the bin with the second highest magnitude
            double second_highest_magnitude = 0.0;
            for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table_120_bpm_standardized.length; bin++) if (sequence_info.beat_histogram_thresholded_table_120_bpm_standardized[bin][1] > second_highest_magnitude && bin != index_of_bin_with_highest_magnitude) {
                second_highest_magnitude = sequence_info.beat_histogram_thresholded_table_120_bpm_standardized[bin][1];
            }
            // Calculate the feature value
            value = highest_magnitude + second_highest_magnitude;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
