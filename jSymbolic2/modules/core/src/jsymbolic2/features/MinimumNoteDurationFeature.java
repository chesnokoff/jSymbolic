package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the duration of the shortest note in the piece (in seconds). Set to 0 if
 * there are no notes.
 *
 * @author Cory McKay
 */
public class MinimumNoteDurationFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Minimum Note Duration";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-11";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Duration of the shortest note in the piece (in seconds). Set to 0 if there are no notes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Put durations in an array
            Object[] durations_obj = sequence_info.note_durations.toArray();
            double[] durations = new double[durations_obj.length];
            for (int i = 0; i < durations.length; i++) durations[i] = ((Double) durations_obj[i]).doubleValue();
            // Calculate feature value
            if (durations.length == 0)
                value = 0.0;
            else {
                int index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfSmallest(durations);
                value = durations[index];
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
