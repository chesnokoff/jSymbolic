package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import javax.sound.midi.Sequence;

/**
 * A feature calculator that finds the fraction of all wrapped vertical intervals that are minor seconds. This
 * is weighted by how long intervals are held (e.g. an interval lasting a whole note will be weighted four
 * times as strongly as an interval lasting a quarter note).
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class VerticalMinorSecondsFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Vertical Minor Seconds";
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
        return "C-15";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Fraction of all wrapped vertical intervals that are minor seconds. This is weighted by how long intervals are held (e.g. an interval lasting a whole note will be weighted four times as strongly as an interval lasting a quarter note).";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] wrappped_vertical_interval_histogram = other_feature_values[0];
            value = wrappped_vertical_interval_histogram[1];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
