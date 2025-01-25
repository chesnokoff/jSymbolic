package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import mckay.utilities.staticlibraries.MathAndStatsMethods;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector consisting of bin magnitudes of the vertical interval histogram described in the jSymbolic
 * manual. Each of the bins is associated with a different vertical pitch interval, and is labeled with the
 * number of semitones in that corresponding interval. More specifically, these are numbered from 0 (a unison)
 * to 127 (a vertical interval of 127 semitones). The magnitude of each bin is found by going through a
 * recoding MIDI tick by MIDI tick and noting all vertical intervals that are sounding at each tick, as well
 * as the MIDI velocities of the pair of notes involved in each vertical interval. The end result is a
 * histogram that indicates which vertical intervals are present, and how significant these vertical intervals
 * are relative to one another, with a weighting based on both MIDI velocity and the aggregated durations with
 * which each interval is held throughout the piece. Finally, the histogram is normalized.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class VerticalIntervalHistogramFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public int getDimensions() {
        return 128;
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Vertical Interval Histogram";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "C-1";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector consisting of bin magnitudes of the vertical interval histogram described in the jSymbolic manual. Each of the bins is associated with a different vertical pitch interval, and is labeled with the number of semitones in that corresponding interval. More specifically, these are numbered from 0 (a unison) to 127 (a vertical interval of 127 semitones). The magnitude of each bin is found by going through a recoding MIDI tick by MIDI tick and noting all vertical intervals that are sounding at each tick, as well as the MIDI velocities of the pair of notes involved in each vertical interval. The end result is a histogram that indicates which vertical intervals are present, and how significant these vertical intervals are relative to one another, with a weighting based on both MIDI velocity and the aggregated durations with which each interval is held throughout the piece. Finally, the histogram is normalized.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] vertical_interval_histogram = null;
        if (sequence_info != null) {
            // // Initialize the vertical_interval_histogram and get the value for unisons
            vertical_interval_histogram = new double[128];
            vertical_interval_histogram[0] = sequence_info.total_vertical_unison_velocity;
            // Get the pitch_strength_by_tick_chart
            short[][] pitch_strength_by_tick_chart = sequence_info.pitch_strength_by_tick_chart;
            // Fill in vertical_interval_histogram for all intervals other than unisons, up to 127 semitones
            for (int tick = 0; tick < pitch_strength_by_tick_chart.length; tick++) {
                for (int pitch = 0; pitch < pitch_strength_by_tick_chart[tick].length - 1; pitch++) {
                    int pitch_velocity = pitch_strength_by_tick_chart[tick][pitch];
                    if (pitch_velocity != 0) {
                        for (int other_pitch = pitch + 1; other_pitch < pitch_strength_by_tick_chart[tick].length; other_pitch++) {
                            int other_pitch_velocity = pitch_strength_by_tick_chart[tick][other_pitch];
                            if (other_pitch_velocity != 0) {
                                int interval = other_pitch - pitch;
                                int combined_interval_velocity = pitch_velocity + other_pitch_velocity;
                                vertical_interval_histogram[interval] += combined_interval_velocity;
                            }
                        }
                    }
                }
            }
            // Normalize the histogram
            vertical_interval_histogram = MathAndStatsMethods.normalize(vertical_interval_histogram);
        }
        return vertical_interval_histogram;
    }
}
