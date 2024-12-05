package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

/**
 * A feature calculator that finds the number of pitch classes that occur at least once in the piece.
 * Enharmonic equivalents are grouped together for the purpose of this calculation.
 *
 * @author Cory McKay
 */
public class NumberOfPitchClassesFeature
        extends MIDIFeatureExtractor {
    /* CONSTRUCTOR ******************************************************************************************/


    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public NumberOfPitchClassesFeature() {
        code = "P-5";
        String name = "Number of Pitch Classes";
        String description = "Number of pitch classes that occur at least once in the piece. Enharmonic equivalents are grouped together for the purpose of this calculation.";
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
            // Find the number of pitches
            int count = 0;
            for (int bin = 0; bin < sequence_info.pitch_class_histogram.length; bin++)
                if (0.0 < sequence_info.pitch_class_histogram[bin])
                    count++;

            // Calculate the value
            value = count;
        } else value = -1.0;

        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}