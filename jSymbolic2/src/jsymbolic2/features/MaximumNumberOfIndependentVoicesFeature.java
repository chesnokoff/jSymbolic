package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the maximum number of different channels in which notes are sounded
 * simultaneously.
 *
 * @author Cory McKay
 */
public class MaximumNumberOfIndependentVoicesFeature implements Feature {

    @Override()
    public String getName() {
        return "Maximum Number of Independent Voices";
    }

    @Override()
    public String getCode() {
        return "T-1";
    }

    @Override()
    public String getDescription() {
        return "Maximum number of different channels in which notes are sounded simultaneously.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int max_so_far = 0;
            for (int tick = 0; tick < sequence_info.note_sounding_on_a_channel_tick_map.length; tick++) {
                int count = 0;
                for (int chan = 0; chan < sequence_info.note_sounding_on_a_channel_tick_map[tick].length; chan++) {
                    if (sequence_info.note_sounding_on_a_channel_tick_map[tick][chan])
                        count++;
                }
                if (count > max_so_far)
                    max_so_far = count;
            }
            value = (double) max_so_far;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
