package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of pitched MIDI Note Ons that have at least one MIDI Pitch Bend
 * associated with them, divided by the total number of pitched Note Ons in the piece.
 *
 * @author Cory McKay
 */
public class GlissandoPrevalenceFeature implements Feature {

    @Override()
    public String getName() {
        return "Glissando Prevalence";
    }

    @Override()
    public String getCode() {
        return "P-38";
    }

    @Override()
    public String getDescription() {
        return "Number of pitched MIDI Note Ons that have at least one MIDI Pitch Bend associated with them, divided by the total number of pitched Note Ons in the piece.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            if (sequence_info.total_number_pitched_note_ons == 0)
                value = 0.0;
            else
                value = (double) sequence_info.pitch_bends_list.size() / (double) sequence_info.total_number_pitched_note_ons;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
