package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the magnitude of the tempo-standardized beat histogram peak with the second
 * highest magnitude.
 *
 * @author Cory McKay
 */
public class StrengthOfSecondStrongestRhythmicPulseTempoStandardizedFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Strength of Second Strongest Rhythmic Pulse - Tempo Standardized";
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
        return "R-61";
    }

    @Override()
    public String getDescription() {
        return "Magnitude of the tempo-standardized beat histogram peak with the second highest magnitude.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

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
            value = second_highest_magnitude;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
