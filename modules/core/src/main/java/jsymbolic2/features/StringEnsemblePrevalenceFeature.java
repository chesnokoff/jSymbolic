package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all Note Ons played by orchestral string ensemble
 * instruments (General MIDI patches 49 to 52).
 *
 * @author Cory McKay
 */
public class StringEnsemblePrevalenceFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "String Ensemble Prevalence";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "I-19";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Fraction of all Note Ons played by orchestral string ensemble instruments (General MIDI patches 49 to 52).";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int[] instruments = { 48, 49, 50, 51 };
            value = MIDIIntermediateRepresentations.calculateInstrumentGroupFrequency(instruments, sequence_info);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
