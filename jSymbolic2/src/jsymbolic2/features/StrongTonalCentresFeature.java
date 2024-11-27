package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the Number of isolated peaks in the fifths pitch histogram that each
 * individually account for at least 9% of all notes in the piece.
 *
 * @author Cory McKay
 */
public class StrongTonalCentresFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Strong Tonal Centres";
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
        return "P-13";
    }

    @Override()
    public String getDescription() {
        return "Number of isolated peaks in the fifths pitch histogram that each individually account for at least 9% of all notes in the piece.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Check all peaks
            int peaks = 0;
            for (int bin = 0; bin < sequence_info.fifths_pitch_histogram.length; bin++) {
                if (sequence_info.fifths_pitch_histogram[bin] >= 0.09) {
                    int left = bin - 1;
                    int right = bin + 1;
                    // Account for wrap around
                    if (right == sequence_info.fifths_pitch_histogram.length)
                        right = 0;
                    if (left == -1)
                        left = sequence_info.fifths_pitch_histogram.length - 1;
                    // Check if is a peak
                    if (sequence_info.fifths_pitch_histogram[bin] > sequence_info.fifths_pitch_histogram[left] && sequence_info.fifths_pitch_histogram[bin] > sequence_info.fifths_pitch_histogram[right]) {
                        peaks++;
                    }
                }
            }
            // Calculate the value
            value = (double) peaks;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
