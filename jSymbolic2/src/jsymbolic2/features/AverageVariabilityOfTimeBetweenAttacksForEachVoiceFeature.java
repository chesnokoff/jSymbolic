package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average of the standard deviations (in seconds) of each individual MIDI
 * channel's time between Note On events. Only channels that contain at least one note are included in this
 * calculation.
 *
 * @author Cory McKay
 */
public class AverageVariabilityOfTimeBetweenAttacksForEachVoiceFeature implements Feature {

    @Override()
    public String getName() {
        return "Average Variability of Time Between Attacks for Each Voice";
    }

    @Override()
    public String getCode() {
        return "RT-10";
    }

    @Override()
    public String getDescription() {
        return "Average of the standard deviations (in seconds) of each individual MIDI channel's time between Note On events. Only channels that contain at least one note are included in this calculation.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[][] intervals = new double[16][];
            // Record the intervals
            for (int chan = 0; chan < 16; chan++) {
                // Mark with null if no notes contained in channel
                if (sequence_info.channel_statistics[chan][0] == 0)
                    intervals[chan] = null;
                else // If notes are contained in channel
                {
                    // Find number of intervals
                    int number_of_intervals = -1;
                    for (int tick = 0; tick < sequence_info.note_attack_tick_map.length; tick++) if (sequence_info.note_attack_tick_map[tick][chan])
                        number_of_intervals++;
                    // Fill in the array of intervals
                    intervals[chan] = new double[number_of_intervals];
                    number_of_intervals = 0;
                    double time_so_far = 0.0;
                    int tick_of_last_attack = -1;
                    for (int tick = 0; tick < sequence_info.note_attack_tick_map.length; tick++) {
                        // Check if an attack occured on this tick
                        boolean attack = false;
                        if (sequence_info.note_attack_tick_map[tick][chan])
                            attack = true;
                        if (!attack)
                            time_so_far += sequence_info.duration_of_ticks_in_seconds[tick];
                        else {
                            if (tick_of_last_attack != -1) {
                                intervals[chan][number_of_intervals] = time_so_far;
                                number_of_intervals++;
                            }
                            time_so_far = 0.0;
                            tick_of_last_attack = tick;
                        }
                    }
                }
            }
            // Find the standard deviations
            int number_channels_with_notes = 16;
            for (int chan = 0; chan < 16; chan++) if (intervals[chan] == null)
                number_channels_with_notes--;
            double[] sds = new double[number_channels_with_notes];
            int count = 0;
            for (int chan = 0; chan < 16; chan++) {
                if (intervals[chan] != null) {
                    sds[count] = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(intervals[chan]);
                    count++;
                }
            }
            // Calculate average of standard deviations
            if (sds == null || sds.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(sds);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
