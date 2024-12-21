package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the difference between the highest note and the lowest note played in the
 * channel with the highest average loudness (MIDI velocity), divided by the difference between the highest
 * note and the lowest note in the piece as a whole. Set to 0 if there if there are fewer than 2 pitches in
 * the music.
 *
 * @author Cory McKay
 */
public class RelativeRangeOfLoudestVoiceFeature implements Feature {

    @Override()
    public String getName() {
        return "Relative Range of Loudest Voice";
    }

    @Override()
    public String getCode() {
        return "T-10";
    }

    @Override()
    public String getDescription() {
        return "Difference between the highest note and the lowest note played in the channel with the highest average loudness (MIDI velocity), divided by the difference between the highest note and the lowest note in the piece as a whole. Set to 0 if there if there are fewer than 2 pitches in the music.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the loudest channel
            int max_so_far = -1;
            int loudest_chan = -1;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] != 0 && chan != (10 - 1)) {
                    if (sequence_info.channel_statistics[chan][2] > max_so_far) {
                        max_so_far = sequence_info.channel_statistics[chan][2];
                        loudest_chan = chan;
                    }
                }
            }
            if (loudest_chan == -1)
                value = 0.0;
            else {
                // Find the range of the loudest channel
                double loudest_range = (double) (sequence_info.channel_statistics[loudest_chan][5] - sequence_info.channel_statistics[loudest_chan][4]);
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
                if (lowest == 128 || highest == -1 || lowest == highest)
                    value = 0.0;
                else
                    value = loudest_range / ((double) (highest - lowest));
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
