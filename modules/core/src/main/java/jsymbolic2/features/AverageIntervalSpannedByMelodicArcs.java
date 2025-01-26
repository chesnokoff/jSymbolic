package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average melodic interval (in semitones) separating the top note of
 * melodic peaks and the bottom note of adjacent melodic troughs. Similar assumptions are made in the
 * calculation of this feature as for the Melodic Interval Histogram.
 *
 * @author Cory McKay
 */
public class AverageIntervalSpannedByMelodicArcs extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Average Interval Spanned by Melodic Arcs";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "M-24";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average melodic interval (in semitones) separating the top note of melodic peaks and the bottom note of adjacent melodic troughs. Similar assumptions are made in the calculation of this feature as for the Melodic Interval Histogram.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int total_intervals = 0;
            int number_intervals = 0;
            for (int track = 0; track < sequence_info.melodic_intervals_by_track_and_channel.size(); track++) {
                LinkedList<Integer>[] melodic_intervals_by_channel = sequence_info.melodic_intervals_by_track_and_channel.get(track);
                for (int chan = 0; chan < melodic_intervals_by_channel.length; chan++) {
                    if (// Exclude unpitched Channel 10 notes
                    chan != (10 - 1)) {
                        // Convert the list of melodic intervals in this channel to an array
                        Object[] list_contents = melodic_intervals_by_channel[chan].toArray();
                        int[] intervals = new int[list_contents.length];
                        for (int i = 0; i < intervals.length; i++) intervals[i] = ((Integer) list_contents[i]).intValue();
                        // Find the number of arcs
                        int direction = 0;
                        int interval_so_far = 0;
                        for (int i = 0; i < intervals.length; i++) {
                            // If arc is currently decending
                            if (direction == -1) {
                                if (intervals[i] < 0)
                                    interval_so_far += Math.abs(intervals[i]);
                                else if (intervals[i] > 0) {
                                    total_intervals += interval_so_far;
                                    number_intervals++;
                                    interval_so_far = Math.abs(intervals[i]);
                                    direction = 1;
                                }
                            } else // If arc is currently ascending
                            if (direction == 1) {
                                if (intervals[i] > 0)
                                    interval_so_far += Math.abs(intervals[i]);
                                else if (intervals[i] < 0) {
                                    total_intervals += interval_so_far;
                                    number_intervals++;
                                    interval_so_far = Math.abs(intervals[i]);
                                    direction = -1;
                                }
                            } else // If arc is currently stationary
                            if (direction == 0) {
                                if (intervals[i] > 0) {
                                    direction = 1;
                                    interval_so_far += Math.abs(intervals[i]);
                                }
                                if (intervals[i] < 0) {
                                    direction = -1;
                                    interval_so_far += Math.abs(intervals[i]);
                                }
                            }
                        }
                    }
                }
            }
            // Calculate the value
            if (number_intervals == 0)
                value = 0.0;
            else
                value = (double) total_intervals / (double) number_intervals;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
