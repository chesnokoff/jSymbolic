package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the ratio between the fraction of notes corresponding to the second most
 * common vertical interval on the wrapped vertical interval histogram and the fraction of vertical intervals
 * corresponding to the most common vertical interval. Set to 0 if either of these prevalences are 0.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class PrevalenceRatioOfTwoMostCommonVerticalIntervalsFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Prevalence Ratio of Two Most Common Vertical Intervals";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String[] getDependencies() {
        return new String[] { "Prevalence of Most Common Vertical Interval", "Prevalence of Second Most Common Vertical Interval" };
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "C-13";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Ratio between the fraction of notes corresponding to the second most common vertical interval on the wrapped vertical interval histogram and the fraction of vertical intervals corresponding to the most common vertical interval. Set to 0 if either of these prevalences are 0.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double prevalence_of_most_common_vertical_interval = other_feature_values[0][0];
            double prevalence_of_second_common_vertical_interval = other_feature_values[1][0];
            if (prevalence_of_most_common_vertical_interval == 0.0)
                value = 0.0;
            else if (prevalence_of_second_common_vertical_interval == 0.0)
                value = 0.0;
            else
                value = prevalence_of_second_common_vertical_interval / prevalence_of_most_common_vertical_interval;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
