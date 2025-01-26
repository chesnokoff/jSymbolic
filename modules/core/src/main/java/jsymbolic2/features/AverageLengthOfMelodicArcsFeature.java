package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average number of notes that separate melodic peaks and troughs.
 * Similar assumptions are made in the calculation of this feature as for the Melodic Interval Histogram. Set
 * to 0 if no melodic arcs are found.
 *
 * @author Cory McKay
 */
public class AverageLengthOfMelodicArcsFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Average Length of Melodic Arcs";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "M-23";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average number of notes that separate melodic peaks and troughs. Similar assumptions are made in the calculation of this feature as for the Melodic Interval Histogram. Set to 0 if no melodic arcs are found.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int total_number_intervening_intervals = 0;
            int number_arcs = 0;
            for (int track = 0; track < sequence_info.melodic_intervals_by_track_and_channel.size(); track++) {
                LinkedList<Integer>[] melodic_intervals_by_channel = sequence_info.melodic_intervals_by_track_and_channel.get(track);
                for (int chan = 0; chan < melodic_intervals_by_channel.length; chan++) {
                    if (chan != (10 - 1)) {
                        // Convert list of melodic intervals in this channel to an array
                        Object[] list_contents = melodic_intervals_by_channel[chan].toArray();
                        int[] intervals = new int[list_contents.length];
                        for (int i = 0; i < intervals.length; i++) intervals[i] = ((Integer) list_contents[i]).intValue();
                        // Find the number of arcs
                        int direction = 0;
                        for (int i = 0; i < intervals.length; i++) {
                            // If arc is currently decending
                            if (direction == -1) {
                                if (intervals[i] < 0)
                                    total_number_intervening_intervals++;
                                else if (intervals[i] > 0) {
                                    total_number_intervening_intervals++;
                                    number_arcs++;
                                    direction = 1;
                                }
                            } else // If arc is currently ascending
                            if (direction == 1) {
                                if (intervals[i] > 0)
                                    total_number_intervening_intervals++;
                                else if (intervals[i] < 0) {
                                    total_number_intervening_intervals++;
                                    number_arcs++;
                                    direction = -1;
                                }
                            } else // If arc is currently stationary
                            if (direction == 0) {
                                if (intervals[i] > 0) {
                                    direction = 1;
                                    total_number_intervening_intervals++;
                                }
                                if (intervals[i] < 0) {
                                    direction = -1;
                                    total_number_intervening_intervals++;
                                }
                            }
                        }
                    }
                }
            }
            // Calculate the value
            if (number_arcs == 0)
                value = 0.0;
            else
                value = (double) total_number_intervening_intervals / (double) number_arcs;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
