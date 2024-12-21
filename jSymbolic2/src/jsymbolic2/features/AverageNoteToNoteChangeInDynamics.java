package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average change of loudness from one note to the next note in the same
 * MIDI channel.
 *
 * @author Cory McKay
 */
@SuppressWarnings("ALL")
public class AverageNoteToNoteChangeInDynamics implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Average Note to Note Change in Dynamics";
    }

    @Override()
    public String getCode() {
        return "D-4";
    }

    @Override()
    public String getDescription() {
        return "Average change of loudness from one note to the next note in the same MIDI channel. ";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the total number of intervals between notes on each channel
            int number_changes = 0;
            for (int i = 0; i < sequence_info.note_loudnesses.length; i++) if (sequence_info.note_loudnesses[i].length > 1)
                number_changes += sequence_info.note_loudnesses[i].length;
            // Find all of the loudness intervals
            double[] loudness_intervals = new double[number_changes];
            int count = 0;
            for (int i = 0; i < sequence_info.note_loudnesses.length; i++) {
                if (sequence_info.note_loudnesses[i].length > 1) {
                    for (int j = 1; j < sequence_info.note_loudnesses[i].length; j++) {
                        loudness_intervals[count] = (double) Math.abs(sequence_info.note_loudnesses[i][j] - sequence_info.note_loudnesses[i][j - 1]);
                        count++;
                    }
                }
            }
            // Calculate the average
            if (loudness_intervals == null || loudness_intervals.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(loudness_intervals);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
