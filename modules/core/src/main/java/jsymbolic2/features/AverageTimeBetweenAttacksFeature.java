package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average time (in seconds) between Note On events (regardless of MIDI
 * channel). Set to 0 if there are less than two attacks.
 *
 * @author Cory McKay
 */
public class AverageTimeBetweenAttacksFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Average Time Between Attacks";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-7";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average time (in seconds) between Note On events (regardless of MIDI channel). Set to 0 if there are less than two attacks.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double total_of_intervals = 0;
            int number_of_intervals = 0;
            double time_so_far = 0.0;
            int tick_of_last_attack = -1;
            for (int tick = 0; tick < sequence_info.note_attack_tick_map.length; tick++) {
                // Check if an attack occured on this tick
                boolean attack = sequence_info.note_attack_tick_map[tick][16];
                if (!attack)
                    time_so_far += sequence_info.duration_of_ticks_in_seconds[tick];
                else {
                    if (tick_of_last_attack != -1) {
                        total_of_intervals += time_so_far;
                        number_of_intervals++;
                    }
                    time_so_far = 0.0;
                    tick_of_last_attack = tick;
                }
            }
            if (number_of_intervals == 0)
                value = 0.0;
            else
                value = total_of_intervals / (double) number_of_intervals;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
