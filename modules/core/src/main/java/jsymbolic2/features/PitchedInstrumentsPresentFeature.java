package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature extractor that calculates a feature vector indicating which pitched instruments are present. Has
 * one entry for each of the 128 General MIDI Instrument patches (0 is Acoustic Piano, 40 is Violin, etc.).
 * Each value is set to 1 if at least one note is played using the corresponding patch, or to 0 if that patch
 * is never used.
 *
 * @author Cory McKay
 */
public class PitchedInstrumentsPresentFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public int getDimensions() {
        return 128;
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Pitched Instruments Present";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "I-1";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector indicating which pitched instruments are present. Has one entry for each of the 128 General MIDI Instrument patches (0 is Acoustic Piano, 40 is Violin, etc.). Each value is set to 1 if at least one note is played using the corresponding patch, or to 0 if that patch is never used.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[128];
            for (int instrument = 0; instrument < result.length; instrument++) {
                if (sequence_info.pitched_instrument_prevalence[instrument][0] > 0)
                    result[instrument] = 1.0;
                else
                    result[instrument] = 0.0;
            }
        }
        return result;
    }
}
