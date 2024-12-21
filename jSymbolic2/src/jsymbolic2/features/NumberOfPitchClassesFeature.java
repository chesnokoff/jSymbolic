package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of pitch classes that occur at least once in the piece.
 * Enharmonic equivalents are grouped together for the purpose of this calculation.
 *
 * @author Cory McKay
 */
public class NumberOfPitchClassesFeature implements Feature {

    @Override()
    public String getName() {
        return "Number of Pitch Classes";
    }

    @Override()
    public String getCode() {
        return "P-5";
    }

    @Override()
    public String getDescription() {
        return "Number of pitch classes that occur at least once in the piece. Enharmonic equivalents are grouped together for the purpose of this calculation.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of pitches
            int count = 0;
            for (int bin = 0; bin < sequence_info.pitch_class_histogram.length; bin++) if (sequence_info.pitch_class_histogram[bin] > 0.0)
                count++;
            // Calculate the value
            value = (double) count;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
