package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of notes with a duration less than 0.1 seconds, divided by the
 * total number of notes in the piece.
 *
 * @author Cory McKay
 */
public class AmountOfStaccatoFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Amount of Staccato";
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
        return "RT-15";
    }

    @Override()
    public String getDescription() {
        return "Number of notes with a duration less than 0.1 seconds, divided by the total number of notes in the piece.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Put durations in an array
            Object[] durations_obj = sequence_info.note_durations.toArray();
            double[] durations = new double[durations_obj.length];
            for (int i = 0; i < durations.length; i++) durations[i] = ((Double) durations_obj[i]).doubleValue();
            // Find the number of notes with short durations
            int short_count = 0;
            for (int i = 0; i < durations.length; i++) if (durations[i] < 0.1)
                short_count++;
            // Find the total number of note ons
            int count = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) count += sequence_info.channel_statistics[chan][0];
            // Calculate feautre value
            if (short_count == 0.0 || count == 0)
                value = 0.0;
            else
                value = (double) short_count / (double) count;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
