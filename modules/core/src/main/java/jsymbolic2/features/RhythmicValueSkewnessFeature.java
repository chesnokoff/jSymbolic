package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the skewness of the note durations in quarter notes of all notes in the
 * music. Provides a measure of how asymmetrical the rhythmic value distribution is to either the left or the
 * right of the mean rhythmic value. A value of zero indicates no skew. This calculation includes both pitched
 * and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is
 * calculated without regard to the dynamics, voice or instrument of any given note.
 *
 * @author Cory McKay
 */
public class RhythmicValueSkewnessFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Rhythmic Value Skewness";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-31";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Skewness of the note durations in quarter notes of all notes in the music. Provides a measure of how asymmetrical the rhythmic value distribution is to either the left or the right of the mean rhythmic value. A value of zero indicates no skew. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getMedianSkewness(sequence_info.rhythmic_value_of_each_note_in_quarter_notes);
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
