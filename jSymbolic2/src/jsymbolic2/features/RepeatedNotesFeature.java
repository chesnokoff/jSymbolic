package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of melodic intervals that correspond to repeated notes.
 *
 * @author Cory McKay
 */
public class RepeatedNotesFeature implements Feature {

    @Override()
    public String getName() {
        return "Repeated Notes";
    }

    @Override()
    public String getCode() {
        return "M-9";
    }

    @Override()
    public String getDescription() {
        return "Fraction of melodic intervals that correspond to repeated notes.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = sequence_info.melodic_interval_histogram[0];
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
