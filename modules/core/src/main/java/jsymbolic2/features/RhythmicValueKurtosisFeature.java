package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the kurtosis of the note durations in quarter notes of all notes in the
 * music. Provides a measure of how peaked or flat the rhythmic value distribution is. The higher the
 * kurtosis, the more the rhythmic values are clustered near the mean and the fewer outliers there are. This
 * calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not
 * influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given
 * note.
 *
 * @author Cory McKay
 */
public class RhythmicValueKurtosisFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Rhythmic Value Kurtosis";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-32";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Kurtosis of the note durations in quarter notes of all notes in the music. Provides a measure of how peaked or flat the rhythmic value distribution is. The higher the kurtosis, the more the rhythmic values are clustered near the mean and the fewer outliers there are. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getSampleExcessKurtosis(sequence_info.rhythmic_value_of_each_note_in_quarter_notes);
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
