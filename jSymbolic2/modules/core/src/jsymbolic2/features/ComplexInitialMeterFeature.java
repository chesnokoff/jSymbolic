package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that is set to 1 if the initial meter is a standard complex meter (i.e. if the
 * numerator of the time signature is 5, 7, 9, 11, 13, 15, 22 or 25) and to 0 otherwise.
 *
 * @author Cory McKay
 */
public class ComplexInitialMeterFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Complex Initial Meter";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String[] getDependencies() {
        return new String[] { "Initial Time Signature" };
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-4";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Set to 1 if the initial meter is a standard complex meter (i.e. if the numerator of the time signature is 5, 7, 9, 11, 13, 15, 22 or 25) and to 0 otherwise.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value = 0.0;
        if (sequence_info != null) {
            // Default to simple meter
            value = 0.0;
            // Get the numerator of the time signature
            double initial_time_signature_numerator = other_feature_values[0][0];
            // Set to compound meter if appropriate
            if (initial_time_signature_numerator == 5.0 || initial_time_signature_numerator == 7.0 || initial_time_signature_numerator == 9.0 || initial_time_signature_numerator == 11.0 || initial_time_signature_numerator == 13.0 || initial_time_signature_numerator == 15.0 || initial_time_signature_numerator == 22.0 || initial_time_signature_numerator == 25.0)
                value = 1.0;
        }
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
