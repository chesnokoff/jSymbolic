package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the MIDI pitch value of the most frequently occurring pitch.
 *
 * @author Cory McKay
 */
public class MostCommonPitchFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Most Common Pitch";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-16";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "MIDI pitch value of the most frequently occuring pitch.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the highest bin
            double max = 0;
            int max_index = 0;
            for (int bin = 0; bin < sequence_info.basic_pitch_histogram.length; bin++) {
                if (sequence_info.basic_pitch_histogram[bin] > max) {
                    max = sequence_info.basic_pitch_histogram[bin];
                    max_index = bin;
                }
            }
            // Calculate the value
            value = (double) max_index;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
