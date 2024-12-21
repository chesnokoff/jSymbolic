package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of melodic intervals that are major or minor thirds.
 *
 * @author Cory McKay
 */
public class MelodicThirdsFeature implements Feature {

    @Override()
    public String getName() {
        return "Melodic Thirds";
    }

    @Override()
    public String getCode() {
        return "M-12";
    }

    @Override()
    public String getDescription() {
        return "Fraction of melodic intervals that are major or minor thirds.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = sequence_info.melodic_interval_histogram[3] + sequence_info.melodic_interval_histogram[4];
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
