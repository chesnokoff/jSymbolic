package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;
import java.util.LinkedList;

/**
 * A feature vector consisting of two values. The first is the numerator of the first specified time signature
 * in the piece, and the second is the denominator of the same time signature. Set to 4/4 if no time signature
 * is specified.
 *
 * @author Cory McKay
 */
public class InitialTimeSignatureFeature
        extends MIDIFeatureExtractor {
    /* CONSTRUCTOR ******************************************************************************************/


    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public InitialTimeSignatureFeature() {
        code = "R-1";
        String name = "Initial Time Signature";
        String description = "A feature vector consisting of two values. The first is the numerator of the first specified time signature in the piece, and the second is the denominator of the same time signature. Set to 4/4 if no time signature is specified.";
        boolean is_sequential = true;
        int dimensions = 2;
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
        double[] result = null;
        if (null != sequence_info) {
            result = new double[2];

            if (((LinkedList) sequence_info.overall_metadata[1]).isEmpty()) {
                result[0] = 4.0;
                result[1] = 4.0;
            } else {
                Object[] numerators_objects = ((LinkedList) sequence_info.overall_metadata[1]).toArray();
                double[] numerators = new double[numerators_objects.length];
                for (int i = 0; i < numerators.length; i++)
                    numerators[i] = ((Integer) numerators_objects[i]).doubleValue();
                Object[] denominators_objects = ((LinkedList) sequence_info.overall_metadata[2]).toArray();
                double[] denominators = new double[denominators_objects.length];
                for (int i = 0; i < denominators.length; i++)
                    denominators[i] = ((Integer) denominators_objects[i]).doubleValue();

                result[0] = numerators[0];
                result[1] = denominators[0];
            }
        }
        return result;
    }
}