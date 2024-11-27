package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the kurtosis of the pitch classes (where 0 corresponds to C, 1 to C#/Db,
 * etc.) of all pitched notes in the piece. Provides a measure of how peaked or flat the pitch class
 * distribution is. The higher the kurtosis, the more the pitch classes are clustered near the mean and the
 * fewer outliers there are.
 *
 * @author Cory McKay
 */
public class PitchClassKurtosisFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Pitch Class Kurtosis";
    }

    @Override()
    public String[] getDependencies() {
        return null;
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "P-31";
    }

    @Override()
    public String getDescription() {
        return "Kurtosis of the pitch classes (where 0 corresponds to C, 1 to C#/Db, etc.) of all pitched notes in the piece. Provides a measure of how peaked or flat the pitch class distribution is. The higher the kurtosis, the more the pitch classes are clustered near the mean and the fewer outliers there are.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getSampleExcessKurtosis(sequence_info.pitch_classes_of_all_note_ons);
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
