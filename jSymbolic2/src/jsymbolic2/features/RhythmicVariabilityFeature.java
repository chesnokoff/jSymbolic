package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the beat histogram bin magnitudes
 *
 * @author Cory McKay
 */
public class RhythmicVariabilityFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Rhythmic Variability";
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
        return "RT-27";
    }

    @Override()
    public String getDescription() {
        return "Standard deviation of the beat histogram bin magnitudes";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Make the reduced histogram (excluding the first 40 empty bins)
            double[] reduced_histogram = new double[sequence_info.beat_histogram.length - 40];
            System.arraycopy(sequence_info.beat_histogram, 40, reduced_histogram, 0, reduced_histogram.length);
            // Calculate the value
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(reduced_histogram);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
