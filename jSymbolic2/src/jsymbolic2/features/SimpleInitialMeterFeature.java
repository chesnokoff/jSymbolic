package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that is set to 1 if the initial meter is a standard simple meter (i.e. if the
 * numerator of the time signature is 2, 3 or 4) and to 0 otherwise.
 *
 * @author Cory McKay
 */
public class SimpleInitialMeterFeature implements Feature {

    @Override()
    public String getName() {
        return "Simple Initial Meter";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Initial Time Signature" };
    }

    @Override()
    public String getCode() {
        return "R-2";
    }

    @Override()
    public String getDescription() {
        return "Set to 1 if the initial meter is a standard simple meter (i.e. if the numerator of the time signature is 2, 3 or 4) and to 0 otherwise.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value = 0.0;
        if (sequence_info != null) {
            // Default to simple meter
            value = 0.0;
            // Get the numerator of the time signature
            double initial_time_signature_numerator = other_feature_values[0][0];
            // Set to compound meter if appropriate
            if (initial_time_signature_numerator == 2.0 || initial_time_signature_numerator == 3.0 || initial_time_signature_numerator == 4.0)
                value = 1.0;
        }
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
