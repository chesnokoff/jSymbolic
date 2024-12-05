package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

/**
 * A feature calculator that calculates the fraction of the duration of each MIDI channel during which no note
 * is sounding on that channel, averaged across all channels that contain at least one note. Non-pitched (MIDI
 * channel 10) notes ARE considered in this calculation.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class AverageRestFractionAcrossVoicesFeature
        extends MIDIFeatureExtractor {
    /* CONSTRUCTOR ******************************************************************************************/


    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public AverageRestFractionAcrossVoicesFeature() {
        code = "R-43";
        String name = "Average Rest Fraction Across Voices";
        String description = "Fraction of the duration of each MIDI channel during which no note is sounding on that channel, averaged across all channels that contain at least one note. Non-pitched (MIDI channel 10) notes ARE considered in this calculation.";
        boolean is_sequential = true;
        int dimensions = 1;
        definition = new FeatureDefinition(name, description, is_sequential, dimensions);
        dependencies = null;
        offsets = null;
    }


    /* PUBLIC METHODS ***************************************************************************************/


    /**
     * Extract this feature from the given sequence of MIDI data and its associated information.
     *
     * @param sequence             The MIDI data to extract the feature from.
     * @param sequence_info        Additional data already extracted from the the MIDI sequence.
     * @param other_feature_values The values of other features that may be needed to calculate this feature.
     *                             The order and offsets of these features must be the same as those returned
     *                             by this class' getDependencies and getDependencyOffsets methods,
     *                             respectively. The first indice indicates the feature/window, and the
     *                             second indicates the value.
     * @throws Exception Throws an informative exception if the feature cannot be calculated.
     * @return The extracted feature value(s).
     */
    @Override
    public double[] extractFeature(Sequence sequence,
                                   MIDIIntermediateRepresentations sequence_info,
                                   double[][] other_feature_values)
            throws Exception {
        double value;
        if (null != sequence_info) {
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
                if (0 == total_notes_on_this_channel)
                    continue;

                // Note there are notes this channel and accumulate the total amount of rest time
                number_channels_with_notes++;
                double total_non_silence_this_channel = total_time_notes_sounding_per_channel[channel];
                total_rest_time_all_channels_combined += (sequence_info.sequence_duration_precise - total_non_silence_this_channel);
            }

            // Find the average across channels, and then scale by duration
            if (0 == number_channels_with_notes || 0.0 == sequence_info.sequence_duration_precise)
                value = 0.0;
            else {
                value = total_rest_time_all_channels_combined / number_channels_with_notes;
                value = value / sequence_info.sequence_duration_precise;
            }
        } else value = -1.0;

        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}