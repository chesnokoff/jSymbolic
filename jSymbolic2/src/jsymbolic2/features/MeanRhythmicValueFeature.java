package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the mean rhythmic value of the music, in quarter note units. So, for
 * example, a Mean Rhythmic Value of 0.5 would mean that the duration of an eighth note corresponds to the
 * mean average rhythmic value in the music. This calculation includes both pitched and unpitched notes, is
 * calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the
 * dynamics, voice or instrument of any given note.
 *
 * @author Cory McKay
 */
public class MeanRhythmicValueFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Mean Rhythmic Value";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-25";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "The mean rhythmic value of the music, in quarter note units. So, for example, a Mean Rhythmic Value of 0.5 would mean that the duration of an eighth note corresponds to the mean average rhythmic value in the music. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(sequence_info.rhythmic_value_of_each_note_in_quarter_notes);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
