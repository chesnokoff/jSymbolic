package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of semitones corresponding to the most frequently occurring
 * melodic interval.
 *
 * @author Cory McKay
 */
public class MostCommonMelodicIntervalFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Most Common Melodic Interval";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "M-2";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Number of semitones corresponding to the most frequently occurring melodic interval.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int max_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.melodic_interval_histogram);
            value = (double) max_index;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
