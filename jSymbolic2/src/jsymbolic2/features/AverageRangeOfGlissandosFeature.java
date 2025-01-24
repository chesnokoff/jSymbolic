package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average range of MIDI Pitch Bends, where "range" is defined as the
 * greatest value of the absolute difference between 64 and the second data byte of all MIDI Pitch Bend
 * messages falling between the Note On and Note Off messages of any note in the piece. Set to 0 if there are
 * no MIDI Pitch Bends in the piece.
 *
 * @author Cory McKay
 */
public class AverageRangeOfGlissandosFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Average Range of Glissandos";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-39";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average range of MIDI Pitch Bends, where \"range\" is defined as the greatest value of the absolute difference between 64 and the second data byte of all MIDI Pitch Bend messages falling between the Note On and Note Off messages of any note in the piece. Set to 0 if there are no MIDI Pitch Bends in the piece.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            if (// If there are no pitch bends
            sequence_info.pitch_bends_list.isEmpty())
                value = 0.0;
            else {
                // Generate array of pitch bends
                Object[] notes_objects = sequence_info.pitch_bends_list.toArray();
                LinkedList[] notes = new LinkedList[notes_objects.length];
                for (int i = 0; i < notes.length; i++) notes[i] = (LinkedList) notes_objects[i];
                int[][] pitch_bends = new int[notes.length][];
                for (int i = 0; i < notes.length; i++) {
                    Object[] this_note_pitch_bends_objects = notes[i].toArray();
                    pitch_bends[i] = new int[this_note_pitch_bends_objects.length];
                    for (int j = 0; j < pitch_bends[i].length; j++) pitch_bends[i][j] = ((Integer) this_note_pitch_bends_objects[j]).intValue();
                }
                // Find the range of the bend for each note
                double[] greatest_differences = new double[pitch_bends.length];
                for (int note = 0; note < greatest_differences.length; note++) {
                    int greatest_so_far = 0;
                    for (int bend = 0; bend < pitch_bends[note].length; bend++) if (Math.abs(64 - pitch_bends[note][bend]) > greatest_so_far)
                        greatest_so_far = Math.abs(64 - pitch_bends[note][bend]);
                    greatest_differences[note] = (double) greatest_so_far;
                }
                // Calculate the feature value
                if (greatest_differences.length == 0)
                    value = 0.0;
                else
                    value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(greatest_differences);
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
