package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * Ratio of all wrapped vertical intervals that are dissonant (2nds, tritones, and 7ths to all wrapped
 * vertical intervals that are consonant (unisons, 3rds, 4ths, 5ths, 6ths, octaves). This is weighted by how
 * long each of these intervals are held (e.g. an interval lasting a whole note will be weighted four times as
 * strongly as an interval lasting a quarter note). Set to 0 if there are no dissonant vertical intervals or
 * no consonant vertical intervals.
 *
 * @author Tristano Tenaglia Cory McKay
 */
public class VerticalDissonanceRatioFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Vertical Dissonance Ratio";
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
        return "C-24";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Ratio of all wrapped vertical intervals that are dissonant (2nds, tritones, and 7ths to all wrapped vertical intervals that are consonant (unisons, 3rds, 4ths, 5ths, 6ths, octaves). This is weighted by how long each of these intervals are held (e.g. an interval lasting a whole note will be weighted four times as strongly as an interval lasting a quarter note). Set to 0 if there are no dissonant vertical intervals or no consonant vertical intervals.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] wrappped_vertical_interval_histogram = other_feature_values[0];
            double dissonance = wrappped_vertical_interval_histogram[1] + wrappped_vertical_interval_histogram[2] + wrappped_vertical_interval_histogram[6] + wrappped_vertical_interval_histogram[10] + wrappped_vertical_interval_histogram[11];
            double consonance = wrappped_vertical_interval_histogram[0] + wrappped_vertical_interval_histogram[3] + wrappped_vertical_interval_histogram[4] + wrappped_vertical_interval_histogram[5] + wrappped_vertical_interval_histogram[7] + wrappped_vertical_interval_histogram[8] + wrappped_vertical_interval_histogram[9];
            if (dissonance == 0.0 || consonance == 0.0)
                value = 0.0;
            else
                value = dissonance / consonance;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
