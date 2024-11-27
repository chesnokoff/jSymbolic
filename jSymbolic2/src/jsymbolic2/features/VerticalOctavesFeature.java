package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all wrapped vertical intervals that are octaves. This is
 * weighted by how long intervals are held (e.g. an interval lasting a whole note will be weighted four times
 * as strongly as an interval lasting a quarter note).
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class VerticalOctavesFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Vertical Octaves";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Wrapped Vertical Interval Histogram", "Vertical Interval Histogram" };
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "C-22";
    }

    @Override()
    public String getDescription() {
        return "Fraction of all wrapped vertical intervals that are octaves. This is weighted by how long intervals are held (e.g. an interval lasting a whole note will be weighted four times as strongly as an interval lasting a quarter note).";
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
            double[] vertical_interval_histogram = other_feature_values[1];
            value = wrapped_vertical_interval_histogram[0] - vertical_interval_histogram[0];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
