package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the kurtosis of the MIDI pitches of all pitched notes in the piece.
 * Provides a measure of how peaked or flat the pitch distribution is. The higher the kurtosis, the more the
 * pitches are clustered near the mean and the fewer outliers there are.
 *
 * @author Cory McKay
 */
public class PitchKurtosisFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Pitch Kurtosis";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-30";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Kurtosis of the MIDI pitches of all pitched notes in the piece. Provides a measure of how peaked or flat the pitch distribution is. The higher the kurtosis, the more the pitches are clustered near the mean and the fewer outliers there are.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getSampleExcessKurtosis(sequence_info.pitches_of_all_note_ons);
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
