package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of loudness levels across all notes.
 *
 * @author Cory McKay
 */
public class VariationOfDynamicsFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Variation of Dynamics";
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
        return "D-2";
    }

    @Override()
    public String getDescription() {
        return "Standard deviation of loudness levels across all notes.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] loudnesses = new double[sequence_info.total_number_note_ons];
            int count = 0;
            for (int i = 0; i < sequence_info.note_loudnesses.length; i++) {
                for (int j = 0; j < sequence_info.note_loudnesses[i].length; j++) {
                    loudnesses[count] = (double) sequence_info.note_loudnesses[i][j];
                    count++;
                }
            }
            if (loudnesses == null || loudnesses.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(loudnesses);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
