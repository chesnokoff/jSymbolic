package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of vertical intervals on the wrapped vertical interval
 * histogram corresponding to the most common vertical interval.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class PrevalenceOfMostCommonVerticalIntervalFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Prevalence of Most Common Vertical Interval";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Wrapped Vertical Interval Histogram", "Most Common Vertical Interval" };
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "C-11";
    }

    @Override()
    public String getDescription() {
        return "Fraction of vertical intervals on the wrapped vertical interval histogram corresponding to the most common vertical interval.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] wrapped_vertical_interval_histogram = other_feature_values[0];
            int most_common_vertical_interval = (int) Math.round(other_feature_values[1][0]);
            value = wrapped_vertical_interval_histogram[most_common_vertical_interval];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
