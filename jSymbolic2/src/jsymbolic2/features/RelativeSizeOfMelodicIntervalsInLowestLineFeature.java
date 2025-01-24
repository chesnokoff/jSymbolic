package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average melodic interval in semitones in the channel with the lowest
 * average pitch, divided by the average melodic interval in all channels that contain at least two notes.
 *
 * @author Cory McKay
 */
public class RelativeSizeOfMelodicIntervalsInLowestLineFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Relative Size of Melodic Intervals in Lowest Line";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-15";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average melodic interval in semitones in the channel with the lowest average pitch, divided by the average melodic interval in all channels that contain at least two notes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the channel with the lowest average pitch
            int min_so_far = 0;
            int lowest_chan = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][6] != 0 && chan != (10 - 1)) {
                    if (sequence_info.channel_statistics[chan][6] < min_so_far) {
                        min_so_far = sequence_info.channel_statistics[chan][6];
                        lowest_chan = chan;
                    }
                }
            }
            // Find the number of channels with no note ons (or that is channel 10 or that is the highest
            // channel)
            int silent_count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) if (sequence_info.channel_statistics[chan][0] == 0 || chan == (10 - 1))
                silent_count++;
            // Find the average melodic interval of notes in the other channels
            double[] intervals = new double[sequence_info.channel_statistics.length - silent_count];
            int count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] != 0 && chan != (10 - 1)) {
                    intervals[count] = (double) sequence_info.channel_statistics[chan][3];
                    count++;
                }
            }
            // Set value
            if (intervals == null || intervals.length == 0 || sequence_info.channel_statistics[lowest_chan][3] == 0)
                value = 0.0;
            else {
                double average = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(intervals);
                value = ((double) sequence_info.channel_statistics[lowest_chan][3]) / ((double) average);
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
