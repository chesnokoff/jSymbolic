package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of all Note Ons played by (unpitched) MIDI Percussion Key Map
 * instruments. It should be noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they
 * are the ones that meet the official standard.
 *
 * @author Cory McKay
 */
public class UnpitchedPercussionInstrumentPrevalenceFeature implements Feature {

    @Override()
    public String getName() {
        return "Unpitched Percussion Instrument Prevalence";
    }

    @Override()
    public String getCode() {
        return "I-10";
    }

    @Override()
    public String getDescription() {
        return "Fraction of all Note Ons played by (unpitched) MIDI Percussion Key Map instruments. It should be noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they are the ones that meet the official standard.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int total_number_note_ons = 0;
            for (int channel = 0; channel < sequence_info.channel_statistics.length; channel++) total_number_note_ons += sequence_info.channel_statistics[channel][0];
            int number_unpitched_note_ons = sequence_info.channel_statistics[10 - 1][0];
            if (total_number_note_ons == 0)
                value = 0.0;
            else
                value = (double) number_unpitched_note_ons / (double) total_number_note_ons;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
