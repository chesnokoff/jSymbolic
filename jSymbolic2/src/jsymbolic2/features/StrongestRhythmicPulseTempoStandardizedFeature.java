package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the bin index of the tempo-standardized beat histogram bin with the highest
 * magnitude.
 *
 * @author Cory McKay
 */
public class StrongestRhythmicPulseTempoStandardizedFeature implements Feature {

    @Override()
    public String getName() {
        return "Strongest Rhythmic Pulse - Tempo Standardized";
    }

    @Override()
    public String getCode() {
        return "R-57";
    }

    @Override()
    public String getDescription() {
        return "Bin index of the tempo-standardized beat histogram bin with the highest magnitude.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the bin with the highest magnitude
            double max = 0;
            int max_index = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram_120_bpm_standardized.length; bin++) {
                if (sequence_info.beat_histogram_120_bpm_standardized[bin] > max) {
                    max = sequence_info.beat_histogram_120_bpm_standardized[bin];
                    max_index = bin;
                }
            }
            // Calculate the value
            value = (double) max_index;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
