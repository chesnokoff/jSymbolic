package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;
import java.util.LinkedList;

/**
 * A feature calculator that finds the fraction of melodic intervals that are rising in pitch. Set to zero if
 * no rising or falling melodic intervals are found.
 *
 * @author Cory McKay
 */
public class DirectionOfMelodicMotionFeature
        extends MIDIFeatureExtractor {
    /* CONSTRUCTOR ******************************************************************************************/


    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public DirectionOfMelodicMotionFeature() {
        code = "M-22";
        String name = "Direction of Melodic Motion";
        String description = "Fraction of melodic intervals that are rising in pitch. Set to zero if no rising or falling melodic intervals are found.";
        boolean is_sequential = true;
        int dimensions = 1;
        definition = new FeatureDefinition(name, description, is_sequential, dimensions);
        dependencies = null;
        offsets = null;
    }


    /* PUBLIC METHODS ***************************************************************************************/


    /**
     * Extract this feature from the given sequence of MIDI data and its associated information.
     *
     * @param sequence             The MIDI data to extract the feature from.
     * @param sequence_info        Additional data already extracted from the the MIDI sequence.
     * @param other_feature_values The values of other features that may be needed to calculate this feature.
     *                             The order and offsets of these features must be the same as those returned
     *                             by this class' getDependencies and getDependencyOffsets methods,
     *                             respectively. The first indice indicates the feature/window, and the
     *                             second indicates the value.
     * @throws Exception Throws an informative exception if the feature cannot be calculated.
     * @return The extracted feature value(s).
     */
    @Override
    public double[] extractFeature(Sequence sequence,
                                   MIDIIntermediateRepresentations sequence_info,
                                   double[][] other_feature_values)
            throws Exception {
        double value;
        if (null != sequence_info) {
            int ups = 0;
            int downs = 0;
            for (int track = 0; track < sequence_info.melodic_intervals_by_track_and_channel.size(); track++) {
                LinkedList<Integer>[] melodic_intervals_by_channel = sequence_info.melodic_intervals_by_track_and_channel.get(track);

                for (int chan = 0; chan < melodic_intervals_by_channel.length; chan++) {
                    if ((10 - 1) != chan) // Note Channel 10 unpitched instruments
                    {
                        // Convert to array
                        Object[] list_contents = melodic_intervals_by_channel[chan].toArray();
                        int[] intervals = new int[list_contents.length];
                        for (int i = 0; i < intervals.length; i++)
                            intervals[i] = ((Integer) list_contents[i]).intValue();

                        // Find amount of upper and downward motion
                        for (int i = 0; i < intervals.length; i++) {
                            if (0 < intervals[i])
                                ups++;
                            else if (0 > intervals[i])
                                downs++;
                        }
                    }
                }
            }

            // Calculate the feature value
            if (0 == (ups + downs))
                value = 0.0;
            else
                value = (double) ups / (ups + downs);
        } else value = -1.0;

        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}