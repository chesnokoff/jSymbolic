package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the loudness of the loudest note in the piece, minus the loudness of the
 * softest note.
 *
 * @author Cory McKay
 */
public class DynamicRangeFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Dynamic Range";
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
        return "D-1";
    }

    @Override()
    public String getDescription() {
        return "Loudness of the loudest note in the piece, minus the loudness of the softest note.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int max = -1;
            int min = 128;
            for (int i = 0; i < sequence_info.note_loudnesses.length; i++) {
                for (int j = 0; j < sequence_info.note_loudnesses[i].length; j++) {
                    if (sequence_info.note_loudnesses[i][j] > max)
                        max = sequence_info.note_loudnesses[i][j];
                    if (sequence_info.note_loudnesses[i][j] < min)
                        min = sequence_info.note_loudnesses[i][j];
                }
            }
            if (max == -1 || min == 128)
                value = 0.0;
            else
                value = ((double) (max - min));
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
