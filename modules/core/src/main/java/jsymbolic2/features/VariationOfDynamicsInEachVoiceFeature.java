package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviations of note loudness levels within each MIDI channel,
 * averaged across all channels. Only channels that contain at least one note are included in this
 * calculation.
 *
 * @author Cory McKay
 */
public class VariationOfDynamicsInEachVoiceFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Variation of Dynamics In Each Voice";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "D-3";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Standard deviations of note loudness levels within each MIDI channel, averaged across all channels. Only channels that contain at least one note are included in this calculation.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find number of channels with notes
            int number_with_notes = 0;
            for (int i = 0; i < sequence_info.channel_statistics.length; i++) if (sequence_info.channel_statistics[i][0] != 0)
                number_with_notes++;
            // Calculate the standard deviations
            double[] standard_deviations = new double[number_with_notes];
            int count = 0;
            for (int i = 0; i < sequence_info.note_loudnesses.length; i++) {
                if (sequence_info.note_loudnesses[i].length > 0) {
                    double[] loudnesses = new double[sequence_info.note_loudnesses[i].length];
                    for (int j = 0; j < sequence_info.note_loudnesses[i].length; j++) loudnesses[j] = (double) sequence_info.note_loudnesses[i][j];
                    standard_deviations[count] = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(loudnesses);
                    count++;
                }
            }
            // Calculate the average standard deviation
            if (standard_deviations == null || standard_deviations.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(standard_deviations);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
