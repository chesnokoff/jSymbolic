package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the MIDI pitches of all pitched notes in the
 * piece. Provides a measure of how close the pitches as a whole are to the mean pitch.
 *
 * @author Cory McKay
 */
public class PitchVariabilityFeature implements Feature {

    @Override()
    public String getName() {
        return "Pitch Variability";
    }

    @Override()
    public String getCode() {
        return "P-24";
    }

    @Override()
    public String getDescription() {
        return "Standard deviation of the MIDI pitches of all pitched notes in the piece. Provides a measure of how close the pitches as a whole are to the mean pitch.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] pitches_of_all_note_ons = new double[sequence_info.pitches_of_all_note_ons.length];
            for (int i = 0; i < pitches_of_all_note_ons.length; i++) pitches_of_all_note_ons[i] = (double) sequence_info.pitches_of_all_note_ons[i];
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(pitches_of_all_note_ons);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
