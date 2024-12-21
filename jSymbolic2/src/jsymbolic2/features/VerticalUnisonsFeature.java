package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all vertical intervals that are unisons. This is weighted
 * by how long intervals are held (e.g. an interval lasting a whole note will be weighted four times as
 * strongly as an interval lasting a quarter note).
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class VerticalUnisonsFeature implements Feature {

    @Override()
    public String getName() {
        return "Vertical Unisons";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Vertical Interval Histogram" };
    }

    @Override()
    public String getCode() {
        return "C-14";
    }

    @Override()
    public String getDescription() {
        return "Fraction of all vertical intervals that are unisons. This is weighted by how long intervals are held (e.g. an interval lasting a whole note will be weighted four times as strongly as an interval lasting a quarter note).";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] vertical_interval_histogram = other_feature_values[0];
            value = vertical_interval_histogram[0];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
