package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the absolute value of the difference (in semitones) between the most common
 * and second most common melodic intervals in the piece.
 *
 * @author Cory McKay
 */
public class DistanceBetweenMostPrevalentMelodicIntervalsFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Distance Between Most Prevalent Melodic Intervals";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "M-5";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Absolute value of the difference (in semitones) between the most common and second most common melodic intervals in the piece.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the bin with the highest magnitude
            int max_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.melodic_interval_histogram);
            // Find the bin with the second highest magnitude
            double second_max = 0;
            int second_max_index = 0;
            for (int bin = 0; bin < sequence_info.melodic_interval_histogram.length; bin++) {
                if (sequence_info.melodic_interval_histogram[bin] > second_max && bin != max_index) {
                    second_max = sequence_info.melodic_interval_histogram[bin];
                    second_max_index = bin;
                }
            }
            // Calculate the value
            int difference = Math.abs(max_index - second_max_index);
            value = (double) difference;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
