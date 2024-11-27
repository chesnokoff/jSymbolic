package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.stream.DoubleStream;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of the music during which no pitched notes are sounding on any
 * MIDI channel. Non-pitched (MIDI channel 10) notes are not considered in this calculation.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class CompleteRestsFractionFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Complete Rests Fraction";
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
        return "R-41";
    }

    @Override()
    public String getDescription() {
        return "Fraction of the music during which no pitched notes are sounding on any MIDI channel. Non-pitched (MIDI channel 10) notes are not considered in this calculation.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Get information from sequence_info
            short[][] pitch_strength_by_tick_chart = sequence_info.pitch_strength_by_tick_chart;
            double[] seconds_per_tick = sequence_info.duration_of_ticks_in_seconds;
            // The number of ticks to examine (the minus 1 is because Java doesn't count the last tick
            int ticks_to_test = pitch_strength_by_tick_chart.length - 1;
            // Find the durations of complete rests, tick by tick
            double[] seconds_of_rest_per_tick = new double[ticks_to_test];
            for (int tick = 0; tick < ticks_to_test; tick++) {
                short[] pitch_velocities = pitch_strength_by_tick_chart[tick];
                if (mckay.utilities.staticlibraries.ArrayMethods.doesArrayContainOnlyThisValue(pitch_velocities, 0))
                    seconds_of_rest_per_tick[tick] = seconds_per_tick[tick];
            }
            // Add up the durations of all the complete rests
            double total_complete_rests = DoubleStream.of(seconds_of_rest_per_tick).sum();
            // Divide by the length of the piece
            if (sequence_info.sequence_duration_precise == 0.0)
                value = 0.0;
            else
                value = total_complete_rests / sequence_info.sequence_duration_precise;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
