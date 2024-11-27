package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of melodic intervals that are rising in pitch. Set to zero if
 * no rising or falling melodic intervals are found.
 *
 * @author Cory McKay
 */
public class DirectionOfMelodicMotionFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Direction of Melodic Motion";
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
        return "M-22";
    }

    @Override()
    public String getDescription() {
        return "Fraction of melodic intervals that are rising in pitch. Set to zero if no rising or falling melodic intervals are found.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            int ups = 0;
            int downs = 0;
            for (int track = 0; track < sequence_info.melodic_intervals_by_track_and_channel.size(); track++) {
                LinkedList<Integer>[] melodic_intervals_by_channel = sequence_info.melodic_intervals_by_track_and_channel.get(track);
                for (int chan = 0; chan < melodic_intervals_by_channel.length; chan++) {
                    if (// Note Channel 10 unpitched instruments
                    chan != (10 - 1)) {
                        // Convert to array
                        Object[] list_contents = melodic_intervals_by_channel[chan].toArray();
                        int[] intervals = new int[list_contents.length];
                        for (int i = 0; i < intervals.length; i++) intervals[i] = ((Integer) list_contents[i]).intValue();
                        // Find amount of upper and downward motion
                        for (int i = 0; i < intervals.length; i++) {
                            if (intervals[i] > 0)
                                ups++;
                            else if (intervals[i] < 0)
                                downs++;
                        }
                    }
                }
            }
            // Calculate the feature value
            if ((ups + downs) == 0)
                value = 0.0;
            else
                value = (double) ups / ((double) (ups + downs));
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
