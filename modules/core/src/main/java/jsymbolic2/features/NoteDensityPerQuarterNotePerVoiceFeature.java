package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average number of note onsets per unit of time corresponding to an
 * idealized quarter note, divided by the total number of voices present (i.e. the number of MIDI channels
 * that contain one or more notes in the piece). Takes into account all notes in all voices, including both
 * pitched and unpitched notes.
 *
 * @author Cory McKay
 */
public class NoteDensityPerQuarterNotePerVoiceFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Note Density per Quarter Note per Voice";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String[] getDependencies() {
        return new String[] { "Note Density per Quarter Note" };
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-11";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average number of note onsets per unit of time corresponding to an idealized quarter note, divided by the total number of voices present (i.e. the number of MIDI channels that contain one or more notes in the piece). Takes into account all notes in all voices, including both pitched and unpitched notes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double note_density_per_quarter_note = other_feature_values[0][0];
            value = note_density_per_quarter_note / sequence_info.number_of_active_voices;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
