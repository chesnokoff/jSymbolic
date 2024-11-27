package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that calculates a measure of the difference between the shortest rhythmic values and
 * the longest rhythmic values in the music. Measured in number of bins in the Rhythmic Value Histogram
 * separating the first (i.e. the one with the shortest rhythmic value) non-zero bin and the last (i.e. the
 * one with the longest rhythmic value) non-zero bin.
 *
 * @author Cory McKay
 */
public class RangeOfRhythmicValuesFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Range of Rhythmic Values";
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
        return "R-14";
    }

    @Override()
    public String getDescription() {
        return "A measure of the difference between the shortest rhythmic values and the longest rhythmic values in the music. Measured in number of bins in the Rhythmic Value Histogram separating the first (i.e. the one with the shortest rhythmic value) non-zero bin and the last (i.e. the one with the longest rhythmic value) non-zero bin.";
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
            value = (double) mckay.utilities.staticlibraries.MathAndStatsMethods.getHistogramRangeInBins(rhythmic_value_histogram);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
