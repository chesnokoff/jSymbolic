package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * The interval in semitones between the wrapped vertical interval histogram bins with the two most common
 * vertical intervals.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class DistanceBetweenTwoMostCommonVerticalIntervalsFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Distance Between Two Most Common Vertical Intervals";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String[] getDependencies() {
        return new String[] { "Most Common Vertical Interval", "Second Most Common Vertical Interval" };
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "C-10";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "The interval in semitones between the wrapped vertical interval histogram bins with the two most common vertical intervals.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double most_common_vertical_interval = other_feature_values[0][0];
            double second_common_vertical_interval = other_feature_values[1][0];
            value = Math.round(Math.abs(most_common_vertical_interval - second_common_vertical_interval));
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
