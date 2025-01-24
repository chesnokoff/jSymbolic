package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the interval in semitones corresponding to the wrapped vertical interval
 * histogram bin with the highest magnitude.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class MostCommonVerticalIntervalFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Most Common Vertical Interval";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String[] getDependencies() {
        return new String[] { "Wrapped Vertical Interval Histogram" };
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "C-8";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "The interval in semitones corresponding to the wrapped vertical interval histogram bin with the highest magnitude.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] wrapped_vertical_interval_histogram = other_feature_values[0];
            value = 0.0;
            double max_magnitude = 0.0;
            for (int interval = 0; interval < wrapped_vertical_interval_histogram.length; interval++) {
                if (wrapped_vertical_interval_histogram[interval] > max_magnitude) {
                    max_magnitude = wrapped_vertical_interval_histogram[interval];
                    value = interval;
                }
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
