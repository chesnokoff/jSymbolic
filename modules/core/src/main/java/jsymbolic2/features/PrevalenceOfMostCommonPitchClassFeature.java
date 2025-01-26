package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of notes that correspond to the most common pitch class.
 *
 * @author Cory McKay
 */
public class PrevalenceOfMostCommonPitchClassFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Prevalence of Most Common Pitch Class";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-19";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Fraction of notes that correspond to the most common pitch class.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the highest bin
            double max = 0;
            int max_index = 0;
            for (int bin = 0; bin < sequence_info.pitch_class_histogram.length; bin++) {
                if (sequence_info.pitch_class_histogram[bin] > max) {
                    max = sequence_info.pitch_class_histogram[bin];
                    max_index = bin;
                }
            }
            // Calculate the value
            value = sequence_info.pitch_class_histogram[max_index];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
