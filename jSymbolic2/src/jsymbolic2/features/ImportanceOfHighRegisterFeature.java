package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of notes between MIDI pitches 73 and 127.
 *
 * @author Cory McKay
 */
public class ImportanceOfHighRegisterFeature implements Feature {

    @Override()
    public String getName() {
        return "Importance of High Register";
    }

    @Override()
    public String getCode() {
        return "P-11";
    }

    @Override()
    public String getDescription() {
        return "Fraction of notes between MIDI pitches 73 and 127.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            value = 0.0;
            for (int bin = 73; bin < sequence_info.basic_pitch_histogram.length; bin++) value += sequence_info.basic_pitch_histogram[bin];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
