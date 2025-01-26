package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average number of note onsets per unit of time corresponding to an
 * idealized quarter note. Takes into account all notes in all voices, including both pitched and unpitched
 * notes.
 *
 * @author Cory McKay
 */
public class NoteDensityPerQuarterNoteFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Note Density per Quarter Note";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-10";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average number of note onsets per unit of time corresponding to an idealized quarter note. Takes into account all notes in all voices, including both pitched and unpitched notes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the total number of note ons
            int count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) count += sequence_info.channel_statistics[chan][0];
            // Calculate the notes per second
            double notes_per_second = 0.0;
            if (sequence_info.sequence_duration != 0)
                notes_per_second = (double) count / sequence_info.sequence_duration_precise;
            // Calculate the notes per quarter note
            value = notes_per_second * sequence_info.average_quarter_note_duration_in_seconds;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
