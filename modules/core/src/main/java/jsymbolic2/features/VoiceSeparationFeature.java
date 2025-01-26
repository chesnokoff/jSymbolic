package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average separation in semitones between the average pitches of
 * consecutive channels (after sorting based on average pitch) that contain at least one note.
 *
 * @author Cory McKay
 */
public class VoiceSeparationFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Voice Separation";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-17";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average separation in semitones between the average pitches of consecutive channels (after sorting based on average pitch) that contain at least one note. ";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of channels with no note ons (or that is channel 10)
            int silent_count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) if (sequence_info.channel_statistics[chan][0] == 0 || chan == (10 - 1))
                silent_count++;
            // If there's only one voice
            if (silent_count > 14)
                value = 0.0;
            else {
                // Store the average melodic interval of notes in the other channels
                double[] average_pitches = new double[sequence_info.channel_statistics.length - silent_count];
                int count = 0;
                for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                    if (sequence_info.channel_statistics[chan][0] != 0 && chan != (10 - 1)) {
                        average_pitches[count] = (double) sequence_info.channel_statistics[chan][6];
                        count++;
                    }
                }
                // Sort the average pitches
                average_pitches = mckay.utilities.staticlibraries.SortingMethods.sortDoubleArray(average_pitches);
                // Find the intervals
                double[] intervals = new double[average_pitches.length - 1];
                for (int i = 0; i < average_pitches.length - 1; i++) intervals[i] = average_pitches[i + 1] - average_pitches[i];
                // Find the average interval
                if (intervals == null || intervals.length == 0)
                    value = 0.0;
                else
                    value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(intervals);
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
