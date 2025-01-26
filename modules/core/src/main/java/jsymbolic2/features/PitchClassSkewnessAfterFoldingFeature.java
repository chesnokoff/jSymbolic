package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the skewness of the pitch classes after being folded by perfect fifths as
 * described for the folded fifths pitch class histogram. Provides a measure of how asymmetrical the pitch
 * class distribution after folding is to either the left or the right of the mean from a dominant-tonic
 * perspective. A value of zero indicates no skew.
 *
 * @author Cory McKay
 */
public class PitchClassSkewnessAfterFoldingFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Pitch Class Skewness After Folding";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-29";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Skewness of the pitch classes (where 0 corresponds to C, 1 to C#/Db, etc.) of all pitched notes in the piece. Provides a measure of how asymmetrical the pitch class distribution is to either the left or the right of the mean pitch class. A value of zero indicates no skew.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] folded_pitch_classes_of_all_note_ons = new double[sequence_info.pitch_classes_of_all_note_ons.length];
            for (int i = 0; i < folded_pitch_classes_of_all_note_ons.length; i++) {
                double pitch_class = (double) sequence_info.pitch_classes_of_all_note_ons[i];
                folded_pitch_classes_of_all_note_ons[i] = (7 * pitch_class) % 12;
            }
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getMedianSkewness(folded_pitch_classes_of_all_note_ons);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
