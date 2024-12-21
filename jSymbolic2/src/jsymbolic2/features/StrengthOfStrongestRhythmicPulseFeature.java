package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the magnitude of the beat histogram bin with the highest magnitude.
 *
 * @author Cory McKay
 */
public class StrengthOfStrongestRhythmicPulseFeature implements Feature {

    @Override()
    public String getName() {
        return "Strength of Strongest Rhythmic Pulse";
    }

    @Override()
    public String getCode() {
        return "RT-23";
    }

    @Override()
    public String getDescription() {
        return "Magnitude of the beat histogram bin with the highest magnitude.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the highest bin
            double max = 0.0;
            for (int bin = 0; bin < sequence_info.beat_histogram.length; bin++) if (sequence_info.beat_histogram[bin] > max)
                max = sequence_info.beat_histogram[bin];
            value = max;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
