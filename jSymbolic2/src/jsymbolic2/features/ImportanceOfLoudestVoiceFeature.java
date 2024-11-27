package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the difference between the average loudness (MIDI velocity) of the loudest
 * channel and the average loudness of the other channels that contain at least one note.
 *
 * @author Cory McKay
 */
public class ImportanceOfLoudestVoiceFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Importance of Loudest Voice";
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
        return "T-9";
    }

    @Override()
    public String getDescription() {
        return "Difference between the average loudness (MIDI velocity) of the loudest channel and the average loudness of the other channels that contain at least one note.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

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
            // Find the loudest channel
            int max_so_far = 0;
            int loudest_chan = 0;
            for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                if (sequence_info.channel_statistics[chan][0] != 0) {
                    if (sequence_info.channel_statistics[chan][2] > max_so_far) {
                        max_so_far = sequence_info.channel_statistics[chan][2];
                        loudest_chan = chan;
                    }
                }
            }
            double loudest_average = (double) max_so_far;
            // Find the average of the other channels and set value
            int number_voices = sequence_info.channel_statistics.length - silent_count;
            if (number_voices < 2)
                value = 0.0;
            else {
                double[] other_averages = new double[number_voices - 1];
                int count = 0;
                for (int chan = 0; chan < sequence_info.channel_statistics.length; chan++) {
                    if (sequence_info.channel_statistics[chan][0] != 0 && chan != loudest_chan) {
                        other_averages[count] = (double) sequence_info.channel_statistics[chan][2];
                        count++;
                    }
                }
                double average = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(other_averages);
                value = loudest_average - average;
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
