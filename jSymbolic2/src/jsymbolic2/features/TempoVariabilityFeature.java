package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the tempo in beats per minute.
 *
 * @author Cory McKay
 */
public class TempoVariabilityFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Tempo Variability";
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
        return "RT-3";
    }

    @Override()
    public String getDescription() {
        return "Standard deviation of the tempo in beats per minute.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Access necessary information
            double ticks_per_beat = (double) sequence.getResolution();
            double[] duration_of_ticks_in_seconds = sequence_info.duration_of_ticks_in_seconds;
            // Calculate all instantaneous tempos
            double[] beats_per_minute = new double[duration_of_ticks_in_seconds.length];
            for (int i = 0; i < beats_per_minute.length; i++) {
                double ticks_per_second = 1.0 / duration_of_ticks_in_seconds[i];
                double beats_per_second = ticks_per_second / ticks_per_beat;
                beats_per_minute[i] = beats_per_second * 60.0;
            }
            // Calculate the final feature value
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(beats_per_minute);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
