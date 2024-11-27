package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds a measure of the difference between the two most common rhythmic values in
 * the music. Measured in number of bins in the Rhythmic Value Histogram separating the two most common
 * rhythmic values.
 *
 * @author Cory McKay
 */
public class DifferenceBetweenMostCommonRhythmicValuesFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Difference Between Most Common Rhythmic Values";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Rhythmic Value Histogram" };
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "R-29";
    }

    @Override()
    public String getDescription() {
        return "A measure of the difference between the two most common rhythmic values in the music. Measured in number of bins in the Rhythmic Value Histogram separating the two most common rhythmic values.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] rhythmic_value_histogram = other_feature_values[0];
            int most_common_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(rhythmic_value_histogram);
            int second_most_common_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfSecondLargest(rhythmic_value_histogram);
            int difference = Math.abs(most_common_index - second_most_common_index);
            value = (double) difference;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
