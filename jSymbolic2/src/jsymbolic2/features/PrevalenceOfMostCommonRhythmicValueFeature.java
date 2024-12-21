package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all notes that have a rhythmic value corresponding to the
 * most common rhythmic value in the music. This calculation includes both pitched and unpitched notes, is
 * calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the
 * dynamics, voice or instrument of any given note.
 *
 * @author Cory McKay
 */
public class PrevalenceOfMostCommonRhythmicValueFeature implements Feature {

    @Override()
    public String getName() {
        return "Prevalence of Most Common Rhythmic Value";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Rhythmic Value Histogram" };
    }

    @Override()
    public String getCode() {
        return "R-27";
    }

    @Override()
    public String getDescription() {
        return "The fraction of all notes that have a rhythmic value corresponding to the most common rhythmic value in the music. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] rhythmic_value_histogram = other_feature_values[0];
            int most_common_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(rhythmic_value_histogram);
            value = rhythmic_value_histogram[most_common_index];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
