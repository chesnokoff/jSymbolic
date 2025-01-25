package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the pitch classes (where 0 corresponds to C, 1 to
 * C#/Db, etc.) of all pitched notes in the piece. Provides a measure of how close the pitch classes as a
 * whole are to the mean pitch class.
 *
 * @author Cory McKay
 */
public class PitchClassVariabilityFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Pitch Class Variability";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-25";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Standard deviation of the pitch classes (where 0 corresponds to C, 1 to C#/Db, etc.) of all pitched notes in the piece. Provides a measure of how close the pitch classes as a whole are to the mean pitch class.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] pitch_classes_of_all_note_ons = new double[sequence_info.pitch_classes_of_all_note_ons.length];
            for (int i = 0; i < pitch_classes_of_all_note_ons.length; i++) pitch_classes_of_all_note_ons[i] = (double) sequence_info.pitch_classes_of_all_note_ons[i];
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(pitch_classes_of_all_note_ons);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
