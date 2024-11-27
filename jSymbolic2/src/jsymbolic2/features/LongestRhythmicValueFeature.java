package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.apache.commons.lang3.ArrayUtils;

/**
 * A feature calculator that finds the rhythmic value of the longest note in the piece, expressed as a
 * fraction of a quarter note. For example, a value of 2 indicates that the longest note is a half note. This
 * calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not
 * influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given
 * note.
 *
 * @author Cory McKay
 */
public class LongestRhythmicValueFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Longest Rhythmic Value";
    }

    @Override()
    public String[] getDependencies() {
        return null;
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "R-24";
    }

    @Override()
    public String getDescription() {
        return "Rhythmic value of the longest note in the piece, expressed as a fraction of a quarter note. For example, a value of 2 indicates that the longest note is a half note. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null && ArrayUtils.isNotEmpty(sequence_info.rhythmic_value_of_each_note_in_quarter_notes)) {
            int index_of_largest = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.rhythmic_value_of_each_note_in_quarter_notes);
            value = sequence_info.rhythmic_value_of_each_note_in_quarter_notes[index_of_largest];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
