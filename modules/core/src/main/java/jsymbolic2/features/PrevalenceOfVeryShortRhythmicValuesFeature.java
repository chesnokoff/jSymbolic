package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all notes in the music that have a quantized rhythmic value
 * less than an eighth note. This includes both pitched and unpitched notes, is calculated after rhythmic
 * quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or
 * instrument of any given note.
 *
 * @author Cory McKay
 */
public class PrevalenceOfVeryShortRhythmicValuesFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Prevalence of Very Short Rhythmic Values";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String[] getDependencies() {
        return new String[] { "Rhythmic Value Histogram" };
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-17";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Fraction of all notes in the music that have a quantized rhythmic value less than an eighth note. This includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] rhythmic_value_histogram = other_feature_values[0];
            value = rhythmic_value_histogram[0] + rhythmic_value_histogram[1];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
