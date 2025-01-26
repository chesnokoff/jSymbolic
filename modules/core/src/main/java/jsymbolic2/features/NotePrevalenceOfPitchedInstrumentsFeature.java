package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector indicating the fraction of (pitched) notes played with each of the 128 General MIDI
 * Instrument patches (0 is Acoustic Piano, 40 is Violin, etc.). Has one entry for each of these instruments,
 * and the value of each is set to the number of Note Ons played with the corresponding MIDI patch, divided by
 * the total number of Note Ons in the piece.
 *
 * @author Cory McKay
 */
public class NotePrevalenceOfPitchedInstrumentsFeature extends Feature {

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
        return "Note Prevalence of Pitched Instruments";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "I-3";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector indicating the fraction of (pitched) notes played with each of the 128 General MIDI Instrument patches (0 is Acoustic Piano, 40 is Violin, etc.). Has one entry for each of these instruments, and the value of each is set to the number of Note Ons played with the corresponding MIDI patch, divided by the total number of Note Ons in the piece.";
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
                if (sequence_info.total_number_note_ons == 0)
                    result[instrument] = 0.0;
                else
                    result[instrument] = sequence_info.pitched_instrument_prevalence[instrument][0] / (double) sequence_info.total_number_note_ons;
            }
        }
        return result;
    }
}
