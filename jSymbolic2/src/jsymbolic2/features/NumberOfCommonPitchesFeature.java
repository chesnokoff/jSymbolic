package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of unique pitches that account individually for at least 9% of
 * all notes. Enharmonic equivalents are grouped together for the purpose of this calculation.
 *
 * @author Cory McKay
 */
public class NumberOfCommonPitchesFeature implements Feature {

    @Override()
    public String getName() {
        return "Number of Common Pitches";
    }

    @Override()
    public String getCode() {
        return "P-6";
    }

    @Override()
    public String getDescription() {
        return "Number of unique pitches that account individually for at least 9% of all notes. Enharmonic equivalents are grouped together for the purpose of this calculation.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of pitches
            int count = 0;
            for (int bin = 0; bin < sequence_info.basic_pitch_histogram.length; bin++) if (sequence_info.basic_pitch_histogram[bin] >= 0.09)
                count++;
            // Prepare the feature value
            value = (double) count;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
