package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the mean average (in semitones) of the intervals involved in each of the
 * melodic intervals in the piece.
 *
 * @author Cory McKay
 */
public class MeanMelodicIntervalFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Mean Melodic Interval";
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
        return "M-3";
    }

    @Override()
    public String getDescription() {
        return "Mean average (in semitones) of the intervals involved in each of the melodic intervals in the piece.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            value = 0.0;
            for (int i = 0; i < sequence_info.melodic_interval_histogram.length; i++) value += (double) i * sequence_info.melodic_interval_histogram[i];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
