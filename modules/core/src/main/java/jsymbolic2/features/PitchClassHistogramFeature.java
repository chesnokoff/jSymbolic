package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector consisting of bin magnitudes of the pitch class histogram described in the jSymbolic
 * manual. Each bin corresponds to one of the 12 pitch classes, ordered in increasing pitch with an interval
 * of a semitone between each (enharmonic equivalents are assigned the same pitch class number). The first bin
 * corresponds to the most common pitch class in the piece under consideration (it does NOT correspond to a
 * set pitch class). The magnitude of of each bin is proportional to the the number of times notes occurred at
 * the bin's pitch class in the piece, relative to all other pitch classes in the piece (the histogram is
 * normalized).
 *
 * @author Cory McKay
 */
public class PitchClassHistogramFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public int getDimensions() {
        return 12;
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Pitch Class Histogram";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-2";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector consisting of bin magnitudes of the pitch class histogram described in the jSymbolic manual. Each bin corresponds to one of the 12 pitch classes, ordered in increasing pitch with an interval of a semitone between each (enharmonic equivalents are assigned the same pitch class number). The first bin corresponds to the most common pitch class in the piece under consideration (it does NOT correspond to a set pitch class). The magnitude of of each bin is proportional to the the number of times notes occurred at the bin's pitch class in the piece, relative to all other pitch classes in the piece (the histogram is normalized).";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            // Find index of bin with highest frequency
            int index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.pitch_class_histogram);
            // Construct new histogram starting with the bin of the most common pitch class
            result = new double[sequence_info.pitch_class_histogram.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = sequence_info.pitch_class_histogram[index];
                index++;
                // Wrap around
                if (index == result.length)
                    index = 0;
            }
        }
        return result;
    }
}
