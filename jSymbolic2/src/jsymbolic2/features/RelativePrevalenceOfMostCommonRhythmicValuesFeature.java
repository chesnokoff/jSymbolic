package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the relative frequency of the of the second most common rhythmic value in
 * the piece, divided by the relative frequency of the most common rhythmic value. This calculation includes
 * both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo,
 * and is calculated without regard to the dynamics, voice or instrument of any given note.
 *
 * @author Cory McKay
 */
public class RelativePrevalenceOfMostCommonRhythmicValuesFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Relative Prevalence of Most Common Rhythmic Values";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Rhythmic Value Histogram" };
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "R-28";
    }

    @Override()
    public String getDescription() {
        return "Relative frequency of the of the second most common rhythmic value in the piece, divided by the relative frequency of the most common rhythmic value. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] rhythmic_value_histogram = other_feature_values[0];
            int most_common_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(rhythmic_value_histogram);
            int second_most_common_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfSecondLargest(rhythmic_value_histogram);
            if (rhythmic_value_histogram[most_common_index] == 0.0)
                value = 0.0;
            else
                value = rhythmic_value_histogram[second_most_common_index] / rhythmic_value_histogram[most_common_index];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
