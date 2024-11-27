package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector consisting of two values. The first is the numerator of the first specified time signature
 * in the piece, and the second is the denominator of the same time signature. Set to 4/4 if no time signature
 * is specified.
 *
 * @author Cory McKay
 */
public class InitialTimeSignatureFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 2;
    }

    @Override()
    public String getName() {
        return "Initial Time Signature";
    }

    @Override()
    public String[] getDependencies() {
        return null;
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "R-1";
    }

    @Override()
    public String getDescription() {
        return "A feature vector consisting of two values. The first is the numerator of the first specified time signature in the piece, and the second is the denominator of the same time signature. Set to 4/4 if no time signature is specified.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[2];
            if (((LinkedList) sequence_info.overall_metadata[1]).isEmpty()) {
                result[0] = 4.0;
                result[1] = 4.0;
            } else {
                Object[] numerators_objects = ((LinkedList) sequence_info.overall_metadata[1]).toArray();
                double[] numerators = new double[numerators_objects.length];
                for (int i = 0; i < numerators.length; i++) numerators[i] = ((Integer) numerators_objects[i]).doubleValue();
                Object[] denominators_objects = ((LinkedList) sequence_info.overall_metadata[2]).toArray();
                double[] denominators = new double[denominators_objects.length];
                for (int i = 0; i < denominators.length; i++) denominators[i] = ((Integer) denominators_objects[i]).doubleValue();
                result[0] = numerators[0];
                result[1] = denominators[0];
            }
        }
        return result;
    }
}
