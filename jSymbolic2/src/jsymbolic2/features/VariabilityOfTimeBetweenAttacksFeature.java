package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the times (in seconds) between Note On events
 * (regardless of MIDI channel).
 *
 * @author Cory McKay
 */
public class VariabilityOfTimeBetweenAttacksFeature implements Feature {

    @Override()
    public String getName() {
        return "Variability of Time Between Attacks";
    }

    @Override()
    public String getCode() {
        return "RT-9";
    }

    @Override()
    public String getDescription() {
        return "Standard deviation of the times (in seconds) between Note On events (regardless of MIDI channel).";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find number of intervals
            int number_of_intervals = -1;
            for (int tick = 0; tick < sequence_info.note_attack_tick_map.length; tick++) {
                // Check if an attack occured on this tick
                boolean attack = false;
                for (int chan = 0; chan < 16; chan++) {
                    if (sequence_info.note_attack_tick_map[tick][chan]) {
                        attack = true;
                        // exit the loop
                        chan = 17;
                    }
                }
                if (attack)
                    number_of_intervals++;
            }
            // Added check for NegativeArraySizeException
            double[] intervals;
            if (number_of_intervals < 0)
                // This array is empty
                intervals = new double[0];
            else
                // Fill in the array of intervals
                intervals = new double[number_of_intervals];
            number_of_intervals = 0;
            double time_so_far = 0.0;
            int tick_of_last_attack = -1;
            for (int tick = 0; tick < sequence_info.note_attack_tick_map.length; tick++) {
                // Check if an attack occured on this tick
                boolean attack = false;
                for (int chan = 0; chan < 16; chan++) {
                    if (sequence_info.note_attack_tick_map[tick][chan]) {
                        attack = true;
                        // Exit the loop
                        chan = 17;
                    }
                }
                if (!attack)
                    time_so_far += sequence_info.duration_of_ticks_in_seconds[tick];
                else {
                    if (tick_of_last_attack != -1) {
                        intervals[number_of_intervals] = time_so_far;
                        number_of_intervals++;
                    }
                    time_so_far = 0.0;
                    tick_of_last_attack = tick;
                }
            }
            if (intervals == null || intervals.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(intervals);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
