package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the total number of (unpitched) MIDI Percussion Key Map instruments that
 * are used to play at least one note. It should be noted that only MIDI Channel 10 instruments 35 to 81 are
 * included here, as they are the ones that meet the official standard.
 *
 * @author Cory McKay
 */
public class NumberOfUnpitchedInstrumentsFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Number of Unpitched Instruments";
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
        return "I-9";
    }

    @Override()
    public String getDescription() {
        return "Total number of (unpitched) MIDI Percussion Key Map instruments that are used to play at least one note. It should be noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they are the ones that meet the official standard.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int total_number_instruments_played = 0;
            for (int instrument = 35; instrument < 82; instrument++) {
                if (sequence_info.non_pitched_instrument_prevalence[instrument] > 0)
                    total_number_instruments_played++;
            }
            value = (double) total_number_instruments_played;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
