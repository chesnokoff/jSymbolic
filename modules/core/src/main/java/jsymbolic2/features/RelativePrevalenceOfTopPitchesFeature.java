package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the relative frequency of the second most common pitch in the piece,
 * divided by the relative frequency of the most common pitch.
 *
 * @author Cory McKay
 */
public class RelativePrevalenceOfTopPitchesFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Relative Prevalence of Top Pitches";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-20";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Relative frequency of the second most common pitch in the piece, divided by the relative frequency of the most common pitch.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the highest bin
            int max_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.basic_pitch_histogram);
            // Find the second highest bin
            double second_max = 0;
            int second_max_index = 0;
            for (int bin = 0; bin < sequence_info.basic_pitch_histogram.length; bin++) {
                if (sequence_info.basic_pitch_histogram[bin] > second_max && bin != max_index) {
                    second_max = sequence_info.basic_pitch_histogram[bin];
                    second_max_index = bin;
                }
            }
            // Calculate the value
            if (sequence_info.basic_pitch_histogram[max_index] == 0.0)
                value = 0.0;
            else
                value = sequence_info.basic_pitch_histogram[second_max_index] / sequence_info.basic_pitch_histogram[max_index];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
