package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that calculates the fraction of the duration of each MIDI channel during which no note
 * is sounding on that channel, averaged across all channels that contain at least one note. Non-pitched (MIDI
 * channel 10) notes ARE considered in this calculation.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class AverageRestFractionAcrossVoicesFeature implements Feature {

    @Override()
    public String getName() {
        return "Average Rest Fraction Across Voices";
    }

    @Override()
    public String getCode() {
        return "R-43";
    }

    @Override()
    public String getDescription() {
        return "Fraction of the duration of each MIDI channel during which no note is sounding on that channel, averaged across all channels that contain at least one note. Non-pitched (MIDI channel 10) notes ARE considered in this calculation.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Get information from sequence_info
            int[][] channel_stats = sequence_info.channel_statistics;
            double[] total_time_notes_sounding_per_channel = sequence_info.total_time_notes_sounding_per_channel;
            // Find the total amount of rest time accumulated across channels, as well as the number of
            // channels with notes
            double total_rest_time_all_channels_combined = 0.0;
            int number_channels_with_notes = 0;
            for (int channel = 0; channel < channel_stats.length; channel++) {
                // Only process this channel if it contains at least one note
                int total_notes_on_this_channel = channel_stats[channel][0];
                if (total_notes_on_this_channel == 0)
                    continue;
                // Note there are notes this channel and accumulate the total amount of rest time
                number_channels_with_notes++;
                double total_non_silence_this_channel = total_time_notes_sounding_per_channel[channel];
                total_rest_time_all_channels_combined += (sequence_info.sequence_duration_precise - total_non_silence_this_channel);
            }
            // Find the average across channels, and then scale by duration
            if (number_channels_with_notes == 0 || sequence_info.sequence_duration_precise == 0.0)
                value = 0.0;
            else {
                value = total_rest_time_all_channels_combined / (double) number_channels_with_notes;
                value = value / sequence_info.sequence_duration_precise;
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
