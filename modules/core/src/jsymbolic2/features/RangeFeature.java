package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the difference in semitones between the highest and lowest pitches.
 *
 * @author Cory McKay
 */
public class RangeFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Range";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-8";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Difference in semitones between the highest and lowest pitches.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the lowest and highest pitches
            int lowest = 128;
            int highest = -1;
            for (int bin = 0; bin < sequence_info.basic_pitch_histogram.length; bin++) {
                if (sequence_info.basic_pitch_histogram[bin] > 0.0 && lowest == 128)
                    lowest = bin;
                if (sequence_info.basic_pitch_histogram[bin] > 0.0)
                    highest = bin;
            }
            // Calculate the feature value
            if (lowest == 128 || highest == -1)
                value = 0.0;
            else
                value = (double) (highest - lowest);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
