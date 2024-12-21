package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all melodic intervals that corresponds to the most common
 * melodic interval.
 *
 * @author Cory McKay
 */
public class PrevalenceOfMostCommonMelodicInterval implements Feature {

    @Override()
    public String getName() {
        return "Prevalence of Most Common Melodic Interval";
    }

    @Override()
    public String getCode() {
        return "M-6";
    }

    @Override()
    public String getDescription() {
        return "Fraction of all melodic intervals that corresponds to the most common melodic interval.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the highest bin
            int max_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.melodic_interval_histogram);
            // Calculate the feature value
            value = sequence_info.melodic_interval_histogram[max_index];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
