package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average number of different channels in which notes are sounded
 * simultaneously. Rests are not included in this calculation.
 *
 * @author Cory McKay
 */
public class AverageNumberOfIndependentVoicesFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Average Number of Independent Voices";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-2";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average number of different channels in which notes are sounded simultaneously. Rests are not included in this calculation.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Instantiate of the variable holding the number of voices sounding at each tick
            int[] number_sounding = new int[sequence_info.note_sounding_on_a_channel_tick_map.length];
            // Find the number of voices sounding at each tick
            int rest_count = 0;
            for (int tick = 0; tick < sequence_info.note_sounding_on_a_channel_tick_map.length; tick++) {
                for (int chan = 0; chan < sequence_info.note_sounding_on_a_channel_tick_map[tick].length; chan++) {
                    if (sequence_info.note_sounding_on_a_channel_tick_map[tick][chan])
                        number_sounding[tick]++;
                }
                // Keep track of number of ticks with no notes sounding
                if (number_sounding[tick] == 0)
                    rest_count++;
            }
            // Only count the ticks where at least one note was sounding
            double[] final_number_sounding = new double[number_sounding.length - rest_count];
            int count = 0;
            for (int i = 0; i < number_sounding.length; i++) {
                if (number_sounding[i] > 0.5) {
                    final_number_sounding[count] = (double) number_sounding[i];
                    count++;
                }
            }
            // Calculate the average
            if (final_number_sounding == null || final_number_sounding.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(final_number_sounding);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
