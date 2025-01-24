package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.List;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of notes played within the range of another channel, divided by
 * the total number of notes in the piece as a whole.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class VoiceOverlapFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Voice Overlap";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-16";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Number of notes played within the range of another channel, divided by the total number of notes in the piece as a whole.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Access pre-calculated information in sequence_info
            int[][] channel_stats = sequence_info.channel_statistics;
            List<List<Integer>> channel_pitch = sequence_info.list_of_note_on_pitches_by_channel;
            // Find notes that are within the range of any other channel
            int notes_inside_range = 0;
            for (int channel = 0; channel < channel_pitch.size(); channel++) {
                List<Integer> channel_notes = channel_pitch.get(channel);
                for (Integer current_pitch : channel_notes) {
                    // Compare pitch of current_pitch to the range of every other channel
                    for (int other_channel = 0; other_channel < channel_stats.length; other_channel++) {
                        if (channel == other_channel)
                            continue;
                        int highest_pitch = channel_stats[other_channel][5];
                        int lowest_pitch = channel_stats[other_channel][4];
                        if (current_pitch <= highest_pitch && current_pitch >= lowest_pitch) {
                            notes_inside_range++;
                            // only current_pitch on channel once
                            break;
                        }
                    }
                }
            }
            double total_notes = sequence_info.total_number_note_ons;
            if (total_notes == 0.0)
                value = 0.0;
            else
                value = notes_inside_range / total_notes;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
