package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

/**
 * A feature calculator that finds the sum of the magnitudes of the two beat histogram peaks with the highest
 * magnitudes.
 *
 * @author Cory McKay
 */
public class CombinedStrengthOfTwoStrongestRhythmicPulsesFeature
        extends MIDIFeatureExtractor {
    /* CONSTRUCTOR ******************************************************************************************/


    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public CombinedStrengthOfTwoStrongestRhythmicPulsesFeature() {
        code = "RT-26";
        String name = "Combined Strength of Two Strongest Rhythmic Pulses";
        String description = "Sum of the magnitudes of the two beat histogram peaks with the highest magnitudes.";
        boolean is_sequential = true;
        int dimensions = 1;
        definition = new FeatureDefinition(name, description, is_sequential, dimensions);
        dependencies = null;
        offsets = null;
    }


    /* PUBLIC METHODS ***************************************************************************************/


    /**
     * Extract this feature from the given sequence of MIDI data and its associated information.
     *
     * @param sequence             The MIDI data to extract the feature from.
     * @param sequence_info        Additional data already extracted from the the MIDI sequence.
     * @param other_feature_values The values of other features that may be needed to calculate this feature.
     *                             The order and offsets of these features must be the same as those returned
     *                             by this class' getDependencies and getDependencyOffsets methods,
     *                             respectively. The first indice indicates the feature/window, and the
     *                             second indicates the value.
     * @throws Exception Throws an informative exception if the feature cannot be calculated.
     * @return The extracted feature value(s).
     */
    @Override
    public double[] extractFeature(Sequence sequence,
                                   MIDIIntermediateRepresentations sequence_info,
                                   double[][] other_feature_values)
            throws Exception {
        double value;
        if (null != sequence_info) {
            // Find the bin with the highest magnitude
            double highest_magnitude = 0.0;
            int index_of_bin_with_highest_magnitude = 0;
            for (int bin = 0; bin < sequence_info.beat_histogram.length; bin++) {
                if (sequence_info.beat_histogram[bin] > highest_magnitude) {
                    highest_magnitude = sequence_info.beat_histogram[bin];
                    index_of_bin_with_highest_magnitude = bin;
                }
            }

            // Find the bin with the second highest magnitude
            double second_highest_magnitude = 0.0;
            for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table.length; bin++)
                if (sequence_info.beat_histogram_thresholded_table[bin][1] > second_highest_magnitude &&
                        bin != index_of_bin_with_highest_magnitude) {
                    second_highest_magnitude = sequence_info.beat_histogram_thresholded_table[bin][1];
                }

            // Calculate the feature value
            value = highest_magnitude + second_highest_magnitude;
        } else value = -1.0;

        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
