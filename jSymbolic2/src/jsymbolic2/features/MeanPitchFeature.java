package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the mean MIDI pitch value, averaged across all pitched notes in the piece.
 * Set to 0 if there are no pitched notes.
 *
 * @author Cory McKay
 */
public class MeanPitchFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Mean Pitch";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-14";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Mean MIDI pitch value, averaged across all pitched notes in the piece. Set to 0 if there are no pitched notes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the total pitch
            double cumulative_pitch_values = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (// Exclude the unpitched percussion Channel 10
                chan != (10 - 1)) {
                    cumulative_pitch_values += sequence_info.channel_statistics[chan][6] * sequence_info.channel_statistics[chan][0];
                }
            }
            // Calculate the feature value
            if (sequence_info.total_number_pitched_note_ons == 0)
                value = 0.0;
            else
                value = cumulative_pitch_values / (double) sequence_info.total_number_pitched_note_ons;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
