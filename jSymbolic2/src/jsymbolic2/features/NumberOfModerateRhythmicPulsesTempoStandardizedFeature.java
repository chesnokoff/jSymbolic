package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of tempo-standardized  beat histogram peaks with normalized
 * magnitudes over 0.01.
 *
 * @author Cory McKay
 */
public class NumberOfModerateRhythmicPulsesTempoStandardizedFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Number of Moderate Rhythmic Pulses - Tempo Standardized";
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
        return "R-55";
    }

    @Override()
    public String getDescription() {
        return "Number of tempo-standardized beat histogram peaks with normalized magnitudes over 0.01.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of sufficiently large peaks
            int count = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table_120_bpm_standardized.length; bin++) if (sequence_info.beat_histogram_thresholded_table_120_bpm_standardized[bin][1] > 0.001)
                count++;
            // Calculate the value
            value = (double) count;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
