package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the total number of (pitched) General MIDI instrument patches that are used
 * to play at least one note.
 *
 * @author Cory McKay
 */
public class NumberOfPitchedInstrumentsFeature implements Feature {

    @Override()
    public String getName() {
        return "Number of Pitched Instruments";
    }

    @Override()
    public String getCode() {
        return "I-8";
    }

    @Override()
    public String getDescription() {
        return "Total number of (pitched) General MIDI instrument patches that are used to play at least one note.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int total_number_instruments_played = 0;
            for (int instrument = 0; instrument < sequence_info.pitched_instrument_prevalence.length; instrument++) {
                if (sequence_info.pitched_instrument_prevalence[instrument][0] > 0)
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
