package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the pitch class that occurs most frequently compared to other pitch
 * classes. A value of 0 corresponds to C, and pitches increase chromatically by semitone in integer untis
 * (e.g. a value of 2 would mean that D is the most common pitch class). Enharmonic equivalents are treated as
 * a single pitch class.
 *
 * @author Cory McKay
 */
public class MostCommonPitchClassFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Most Common Pitch Class";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-17";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "The pitch class that occurs most frequently compared to other pitch classes. A value of 0 corresponds to C, and pitches increase chromatically by semitone in integer untis (e.g. a value of 2 would mean that D is the most common pitch class). Enharmonic equivalents are treated as a single pitch class.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int max_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.pitch_class_histogram);
            value = (double) max_index;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
