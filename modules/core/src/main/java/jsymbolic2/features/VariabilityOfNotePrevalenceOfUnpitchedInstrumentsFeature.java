package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the fraction of total notes in the piece played
 * by each (unpitched) MIDI Percussion Key Map instrument that is used to play at least one note. It should be
 * noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they are the ones that meet the
 * official standard.
 *
 * @author Cory McKay
 */
public class VariabilityOfNotePrevalenceOfUnpitchedInstrumentsFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Variability of Note Prevalence of Unpitched Instruments";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "I-7";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Standard deviation of the fraction of total notes in the piece played by each (unpitched) MIDI Percussion Key Map instrument that is used to play at least one note. It should be noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they are the ones that meet the official standard.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of unpitched instruments used to play at least one note
            int instruments_present = 0;
            for (int instrument = 35; instrument < 82; instrument++) {
                if (sequence_info.non_pitched_instrument_prevalence[instrument] != 0) {
                    instruments_present++;
                }
            }
            // Calculate the feature value
            double[] instrument_frequencies = new double[instruments_present];
            int count = 0;
            for (int instrument = 35; instrument < 82; instrument++) {
                if (sequence_info.non_pitched_instrument_prevalence[instrument] != 0) {
                    instrument_frequencies[count] = (double) sequence_info.non_pitched_instrument_prevalence[instrument];
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
