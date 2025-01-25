package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.apache.commons.lang3.ArrayUtils;

/**
 * A feature calculator that finds the rhythmic value of the shortest note in the piece, expressed as a
 * fraction of a quarter note. For example, a value of 0.5 indicates that the shortest note is an eighth note.
 * This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is
 * not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given
 * note.
 *
 * @author Cory McKay
 */
public class ShortestRhythmicValueFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Shortest Rhythmic Value";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-23";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Rhythmic value of the shortest note in the piece, expressed as a fraction of a quarter note. For example, a value of 0.5 indicates that the shortest note is an eighth note. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null && ArrayUtils.isNotEmpty(sequence_info.rhythmic_value_of_each_note_in_quarter_notes)) {
            int index_of_smallest = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfSmallest(sequence_info.rhythmic_value_of_each_note_in_quarter_notes);
            value = sequence_info.rhythmic_value_of_each_note_in_quarter_notes[index_of_smallest];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
