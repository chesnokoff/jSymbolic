package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

/**
 * A feature calculator that calculates a measure of the difference between the shortest rhythmic values and
 * the longest rhythmic values in the music. Measured in number of bins in the Rhythmic Value Histogram
 * separating the first (i.e. the one with the shortest rhythmic value) non-zero bin and the last (i.e. the
 * one with the longest rhythmic value) non-zero bin.
 *
 * @author Cory McKay
 */
public class RangeOfRhythmicValuesFeature
        extends MIDIFeatureExtractor {
    /* CONSTRUCTOR ******************************************************************************************/


    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public RangeOfRhythmicValuesFeature() {
        code = "R-14";
        String name = "Range of Rhythmic Values";
        String description = "A measure of the difference between the shortest rhythmic values and the longest rhythmic values in the music. Measured in number of bins in the Rhythmic Value Histogram separating the first (i.e. the one with the shortest rhythmic value) non-zero bin and the last (i.e. the one with the longest rhythmic value) non-zero bin.";
        boolean is_sequential = true;
        int dimensions = 1;
        definition = new FeatureDefinition(name, description, is_sequential, dimensions);
        dependencies = new String[]{"Rhythmic Value Histogram"};
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
            double[] rhythmic_value_histogram = other_feature_values[0];
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getHistogramRangeInBins(rhythmic_value_histogram);
        } else value = -1.0;

        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}