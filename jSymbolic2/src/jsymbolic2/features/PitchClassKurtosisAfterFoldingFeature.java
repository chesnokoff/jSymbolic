package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the kurtosis of the pitch classes after being folded by perfect fifths as
 * described for the folded fifths pitch class histogram. Provides a measure of how peaked or flat the pitch
 * classes are from a dominant-tonic perspective.
 *
 * @author Cory McKay
 */
public class PitchClassKurtosisAfterFoldingFeature implements Feature {

    @Override()
    public String getName() {
        return "Pitch Class Kurtosis After Folding";
    }

    @Override()
    public String getCode() {
        return "P-32";
    }

    @Override()
    public String getDescription() {
        return "Kurtosis of the pitch classes after being folded by perfect fifths as described for the folded fifths pitch class histogram. Provides a measure of how peaked or flat the pitch classes are from a dominant-tonic perspective.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] folded_pitch_classes_of_all_note_ons = new double[sequence_info.pitch_classes_of_all_note_ons.length];
            for (int i = 0; i < folded_pitch_classes_of_all_note_ons.length; i++) {
                double pitch_class = (double) sequence_info.pitch_classes_of_all_note_ons[i];
                folded_pitch_classes_of_all_note_ons[i] = (7 * pitch_class) % 12;
            }
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getSampleExcessKurtosis(folded_pitch_classes_of_all_note_ons);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
