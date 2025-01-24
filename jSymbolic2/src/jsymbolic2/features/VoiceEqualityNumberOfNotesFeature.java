package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the total number of Note Ons in each channel that
 * contains at least one note.
 *
 * @author Cory McKay
 */
public class VoiceEqualityNumberOfNotesFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Voice Equality - Number of Notes";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-4";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Standard deviation of the total number of Note Ons in each channel that contains at least one note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the number of channels with no note ons
            int silent_count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] == 0)
                    silent_count++;
            }
            // Store the number of note ons in each channel with note ons
            double[] number_note_ons = new double[sequence_info.channel_statistics.length - silent_count];
            int count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] != 0) {
                    number_note_ons[count] = (double) sequence_info.channel_statistics[chan][0];
                    count++;
                }
            }
            // Calculate the standard deviation
            if (number_note_ons == null || number_note_ons.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(number_note_ons);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
