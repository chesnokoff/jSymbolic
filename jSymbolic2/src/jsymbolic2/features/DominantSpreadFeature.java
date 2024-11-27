package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the largest number of consecutive pitch classes separated by perfect 5ths
 * that each individually account for at least 9% of the total notes in the piece.
 *
 * @author Cory McKay
 */
public class DominantSpreadFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Dominant Spread";
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
        return "P-12";
    }

    @Override()
    public String getDescription() {
        return "Largest number of consecutive pitch classes separated by perfect 5ths that each individually account for at least 9% of the total notes in the piece.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Check all streaks
            int max_count = 0;
            for (int bin = 0; bin < sequence_info.fifths_pitch_histogram.length; bin++) {
                if (sequence_info.fifths_pitch_histogram[bin] >= 0.09) {
                    boolean done = false;
                    int count = 1;
                    int i = bin + 1;
                    while (!done) {
                        // Wrap around
                        if (i == sequence_info.fifths_pitch_histogram.length)
                            i = 0;
                        // If rereach starting point
                        if (i == bin)
                            done = true;
                        else // Increment if sufficient fraction of notes are present in bin
                        {
                            if (sequence_info.fifths_pitch_histogram[i] >= 0.09) {
                                count++;
                                i++;
                            } else
                                // End if fraction of notes in bin is inusfficient
                                done = true;
                        }
                    }
                    // Record the largest streak
                    if (count > max_count)
                        max_count = count;
                }
            }
            // Convert the feature value
            value = (double) max_count;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
