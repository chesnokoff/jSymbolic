package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the differences between the highest and lowest
 * pitches in each channel that contains at least one note.
 *
 * @author Cory McKay
 */
public class VoiceEqualityRangeFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Voice Equality - Range";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-8";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Standard deviation of the differences between the highest and lowest pitches in each channel that contains at least one note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of channels with no note ons
            int silent_count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] == 0 || chan == (10 - 1))
                    silent_count++;
            }
            // Store the number of note ons in each channel with note ons
            double[] range = new double[sequence_info.channel_statistics.length - silent_count];
            int count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] != 0 && chan != (10 - 1)) {
                    int lowest = sequence_info.channel_statistics[chan][4];
                    int highest = sequence_info.channel_statistics[chan][5];
                    range[count] = (double) (highest - lowest);
                    count++;
                }
            }
            // Calculate the standard deviation
            if (range == null || range.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(range);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
