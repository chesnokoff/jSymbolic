package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the cumulative amount of time during which one or
 * more notes were sounding in each channel that contains at least one note.
 *
 * @author Cory McKay
 */
public class VoiceEqualityNoteDurationFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Voice Equality - Note Duration";
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
        return "T-5";
    }

    @Override()
    public String getDescription() {
        return "Standard deviation of the cumulative amount of time during which one or more notes were sounding in each channel that contains at least one note.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of channels with no note ons
            int silent_count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] == 0)
                    silent_count++;
            }
            // Store the combined note durations in each channel with note ons
            double[] durations = new double[sequence_info.channel_statistics.length - silent_count];
            int count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] != 0) {
                    durations[count] = sequence_info.total_time_notes_sounding_per_channel[chan];
                    count++;
                }
            }
            // Calculate the standard deviation
            if (durations == null || durations.length == 0)
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
