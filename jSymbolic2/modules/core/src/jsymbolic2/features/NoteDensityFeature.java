package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average number of notes per second. Takes into account all notes in all
 * voices, including both pitched and unpitched notes.
 *
 * @author Cory McKay
 */
public class NoteDensityFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Note Density";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-5";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average number of notes per second. Takes into account all notes in all voices, including both pitched and unpitched notes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Find the total number of note ons
            int count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) count += sequence_info.channel_statistics[chan][0];
            // Calculate the feature value
            if (sequence_info.sequence_duration == 0)
                value = 0.0;
            else
                value = (double) count / sequence_info.sequence_duration_precise;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
