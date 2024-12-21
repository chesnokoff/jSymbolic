package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all Note Ons played by orchestral string instruments
 * (General MIDI patches 41 to 47).
 *
 * @author Cory McKay
 */
public class OrchestralStringsPrevalenceFeature implements Feature {

    @Override()
    public String getName() {
        return "Orchestral Strings Prevalence";
    }

    @Override()
    public String getCode() {
        return "I-18";
    }

    @Override()
    public String getDescription() {
        return "Fraction of all Note Ons played by orchestral string instruments (General MIDI patches 41 to 47).";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int[] instruments = { 40, 41, 42, 43, 44, 45, 46 };
            value = MIDIIntermediateRepresentations.calculateInstrumentGroupFrequency(instruments, sequence_info);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
