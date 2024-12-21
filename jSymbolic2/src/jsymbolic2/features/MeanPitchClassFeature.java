package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the mean pitch class value, averaged across all pitched notes in the piece.
 * A value of 0 corresponds to a mean pitch class of C, and pitches increase chromatically by semitone in
 * integer units from there (e.g. a value of 2 would mean that D is the mean pitch class). Enharmonic
 * equivalents are treated as a single pitch class.
 *
 * @author Cory McKay
 */
public class MeanPitchClassFeature implements Feature {

    @Override()
    public String getName() {
        return "Mean Pitch Class";
    }

    @Override()
    public String getCode() {
        return "P-15";
    }

    @Override()
    public String getDescription() {
        return "Mean pitch class value, averaged across all pitched notes in the piece. A value of 0 corresponds to a mean pitch class of C, and pitches increase chromatically by semitone in integer units from there (e.g. a value of 2 would mean that D is the mean pitch class). Enharmonic equivalents are treated as a single pitch class.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] pitch_classes_of_all_note_ons = new double[sequence_info.pitch_classes_of_all_note_ons.length];
            for (int i = 0; i < pitch_classes_of_all_note_ons.length; i++) pitch_classes_of_all_note_ons[i] = (double) sequence_info.pitch_classes_of_all_note_ons[i];
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(pitch_classes_of_all_note_ons);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
