package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of notes that correspond to the most common pitch.
 *
 * @author Cory McKay
 */
public class PrevalenceOfMostCommonPitchFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Prevalence of Most Common Pitch";
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
        return "P-18";
    }

    @Override()
    public String getDescription() {
        return "Fraction of notes that correspond to the most common pitch.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the highest bin
            double max = 0;
            int max_index = 0;
            for (int bin = 0; bin < sequence_info.basic_pitch_histogram.length; bin++) {
                if (sequence_info.basic_pitch_histogram[bin] > max) {
                    max = sequence_info.basic_pitch_histogram[bin];
                    max_index = bin;
                }
            }
            // Calculate the value
            value = sequence_info.basic_pitch_histogram[max_index];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
