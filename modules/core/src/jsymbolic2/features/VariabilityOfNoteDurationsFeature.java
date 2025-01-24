package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of note durations (in seconds).
 *
 * @author Cory McKay
 */
public class VariabilityOfNoteDurationsFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Variability of Note Durations";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-14";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Standard deviation of note durations (in seconds).";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Put durations in an array
            Object[] durations_obj = sequence_info.note_durations.toArray();
            double[] durations = new double[durations_obj.length];
            for (int i = 0; i < durations.length; i++) durations[i] = ((Double) durations_obj[i]).doubleValue();
            // Calculate feature value
            if (durations.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(durations);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
