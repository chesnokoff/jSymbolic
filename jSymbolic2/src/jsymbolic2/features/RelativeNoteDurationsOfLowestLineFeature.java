package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average duration of notes (in seconds) in the channel with the lowest
 * average pitch, divided by the average duration of notes in all channels that contain at least one note.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class RelativeNoteDurationsOfLowestLineFeature implements Feature {

    @Override()
    public String getName() {
        return "Relative Note Durations of Lowest Line";
    }

    @Override()
    public String getCode() {
        return "T-14";
    }

    @Override()
    public String getDescription() {
        return "Average duration of notes (in seconds) in the channel with the lowest average pitch, divided by the average duration of notes in all channels that contain at least one note.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Access pre-calculated information in sequence_info
            int[][] channel_stats = sequence_info.channel_statistics;
            // Find the channel with the lowest average pitch, while also accumulating note counts and note
            // durations across channels
            int lowest_average_pitch = 128;
            int lowest_average_pitch_channel = 0;
            double total_note_durations = 0;
            double total_number_notes = 0;
            for (int channel = 0; channel < channel_stats.length; channel++) {
                if (channel_stats[channel][0] != 0 && channel != (10 - 1)) {
                    // Get lowest average pitch that is > 0 (since 0 means no notes)
                    int average_pitch = channel_stats[channel][6];
                    // Take note if this is the lowest voice
                    if (average_pitch < lowest_average_pitch) {
                        lowest_average_pitch = average_pitch;
                        lowest_average_pitch_channel = channel;
                    }
                    // Count total durations and numbers of notes
                    total_note_durations += channel_stats[channel][1];
                    total_number_notes += channel_stats[channel][0];
                }
            }
            // Calculate the average note duration for lowest average pitch
            double total_durations_in_lowest_channel = channel_stats[lowest_average_pitch_channel][1];
            double total_number_notes_in_lowest_channel = channel_stats[lowest_average_pitch_channel][0];
            if (total_durations_in_lowest_channel == 0.0 || total_number_notes_in_lowest_channel == 0. || total_note_durations == 0.0 || total_number_notes == 0.0)
                value = 0.0;
            else {
                double average_duration_of_notes_in_lowest_channel = total_durations_in_lowest_channel / total_number_notes_in_lowest_channel;
                double average_duration_of_notes_in_all_channels = total_note_durations / total_number_notes;
                value = average_duration_of_notes_in_lowest_channel / average_duration_of_notes_in_all_channels;
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
