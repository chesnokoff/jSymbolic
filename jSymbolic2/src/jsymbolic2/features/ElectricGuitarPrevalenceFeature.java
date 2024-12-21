package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all Note Ons played by electric guitar instruments (General
 * MIDI patches 27 to 32).
 *
 * @author Cory McKay
 */
public class ElectricGuitarPrevalenceFeature implements Feature {

    @Override()
    public String getName() {
        return "Electric Guitar Prevalence";
    }

    @Override()
    public String getCode() {
        return "I-13";
    }

    @Override()
    public String getDescription() {
        return "Fraction of all Note Ons played by electric guitar instruments (General MIDI patches 27 to 32).";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int[] instruments = { 26, 27, 28, 29, 30, 31 };
            value = MIDIIntermediateRepresentations.calculateInstrumentGroupFrequency(instruments, sequence_info);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
