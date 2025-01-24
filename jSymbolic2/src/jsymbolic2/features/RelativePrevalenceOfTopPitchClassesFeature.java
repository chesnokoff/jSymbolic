package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the relative frequency of the of the second most common pitch class in the
 * piece, divided by the relative frequency of the most common pitch class.
 *
 * @author Cory McKay
 */
public class RelativePrevalenceOfTopPitchClassesFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Relative Prevalence of Top Pitch Classes";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-21";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Relative frequency of the of the second most common pitch class in the piece, divided by the relative frequency of the most common pitch class.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the highest bin
            int max_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.pitch_class_histogram);
            // Find the second highest bin
            double second_max = 0;
            int second_max_index = 0;
            for (int bin = 0; bin < sequence_info.pitch_class_histogram.length; bin++) {
                if (sequence_info.pitch_class_histogram[bin] > second_max && bin != max_index) {
                    second_max = sequence_info.pitch_class_histogram[bin];
                    second_max_index = bin;
                }
            }
            // Calculate the value
            if (sequence_info.pitch_class_histogram[max_index] == 0.0)
                value = 0.0;
            else
                value = sequence_info.pitch_class_histogram[second_max_index] / sequence_info.pitch_class_histogram[max_index];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
