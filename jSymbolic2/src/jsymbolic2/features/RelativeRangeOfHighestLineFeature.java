package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the pitch difference in semitones between the highest note and the lowest
 * note played in the channel with the highest average pitch, divided by the difference between the highest
 * note and the lowest note in the piece overall. Set to 0 if there if there are fewer than 2 pitches in the
 * music.
 *
 * @author Cory McKay
 */
public class RelativeRangeOfHighestLineFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Relative Range of Highest Line";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-12";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Pitch difference in semitones between the highest note and the lowest note played in the channel with the highest average pitch, divided by the difference between the highest note and the lowest note in the piece overall. Set to 0 if there if there are fewer than 2 pitches in the music.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the channel with the highest average pitch
            int max_so_far = -1;
            int highest_chan = -1;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][6] != 0 && chan != (10 - 1)) {
                    if (sequence_info.channel_statistics[chan][6] > max_so_far) {
                        max_so_far = sequence_info.channel_statistics[chan][6];
                        highest_chan = chan;
                    }
                }
            }
            if (highest_chan == -1)
                value = 0.0;
            else {
                // Find the range of the highest channel
                double range_of_highest = (double) (sequence_info.channel_statistics[highest_chan][5] - sequence_info.channel_statistics[highest_chan][4]);
                // Finde the overall range
                int lowest = 128;
                int highest = -1;
                for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                    if (sequence_info.channel_statistics[chan][0] != 0 && chan != (10 - 1)) {
                        if (sequence_info.channel_statistics[chan][4] < lowest)
                            lowest = sequence_info.channel_statistics[chan][4];
                        if (sequence_info.channel_statistics[chan][5] > highest)
                            highest = sequence_info.channel_statistics[chan][5];
                    }
                }
                // Set value
                // Set value
                if (lowest == 128 || highest == -1 || lowest == highest)
                    value = 0.0;
                else
                    value = range_of_highest / ((double) (highest - lowest));
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
