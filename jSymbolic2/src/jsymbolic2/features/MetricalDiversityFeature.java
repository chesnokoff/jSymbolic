package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import mckay.utilities.staticlibraries.StringMethods;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that is set to the number of different (unique) time signatures found in the piece.
 * Set to 1 if no time signature is specified.
 *
 * @author Cory McKay
 */
public class MetricalDiversityFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Metrical Diversity";
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
        return "R-8";
    }

    @Override()
    public String getDescription() {
        return "The number of different (unique) time signatures found in the piece. Set to 1 if no time signature is specified.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Default to 1
            value = 1.0;
            // If time signature specified
            if (!((LinkedList) sequence_info.overall_metadata[1]).isEmpty()) {
                // Convert data types
                Object[] numerator_objects = ((LinkedList) sequence_info.overall_metadata[1]).toArray();
                int[] numerators = new int[numerator_objects.length];
                for (int i = 0; i < numerators.length; i++) numerators[i] = (Integer) numerator_objects[i];
                Object[] denominator_objects = ((LinkedList) sequence_info.overall_metadata[2]).toArray();
                int[] denominators = new int[denominator_objects.length];
                for (int i = 0; i < denominators.length; i++) denominators[i] = (Integer) denominator_objects[i];
                // Join into united time signatures
                String[] time_signatures = new String[numerators.length];
                for (int i = 0; i < time_signatures.length; i++) time_signatures[i] = numerators[i] + "/" + denominators[i];
                // Find the number of unique time signatures
                value = (double) StringMethods.getCountsOfUniqueStrings(time_signatures).length;
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
