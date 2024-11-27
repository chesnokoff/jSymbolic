package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the fraction of total notes in the piece played
 * by each (pitched) General MIDI Instrument patch that is used to play at least one note.
 *
 * @author Cory McKay
 */
public class VariabilityOfNotePrevalenceOfPitchedInstrumentsFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Variability of Note Prevalence of Pitched Instruments";
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
        return "I-6";
    }

    @Override()
    public String getDescription() {
        return "Standard deviation of the fraction of total notes in the piece played by each (pitched) General MIDI Instrument patch that is used to play at least one note.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of pitched instruments used to play at least one note
            int instruments_present = 0;
            for (int instrument = 0; instrument < sequence_info.pitched_instrument_prevalence.length; instrument++) {
                if (sequence_info.pitched_instrument_prevalence[instrument][0] != 0) {
                    instruments_present++;
                }
            }
            // Calculate the feature value
            double[] instrument_frequencies = new double[instruments_present];
            int count = 0;
            for (int instrument = 0; instrument < sequence_info.pitched_instrument_prevalence.length; instrument++) {
                if (sequence_info.pitched_instrument_prevalence[instrument][0] != 0) {
                    instrument_frequencies[count] = (double) sequence_info.pitched_instrument_prevalence[instrument][0];
                    count++;
                }
            }
            if (instrument_frequencies == null || instrument_frequencies.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(instrument_frequencies);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
