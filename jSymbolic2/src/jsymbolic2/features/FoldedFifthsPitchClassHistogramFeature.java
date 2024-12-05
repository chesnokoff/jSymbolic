package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

/**
 * A feature vector consisting of bin magnitudes of the folded fifths pitch class histogram described in the
 * jSymbolic manual. Each bin corresponds to one of the 12 pitch classes, and the bins are ordered such that
 * adjacent bins are separated by an ascending perfect fifth. Bin 0 corresponds to C. Enharmonic equivalents
 * are assigned the same pitch class number. The magnitude of of each bin is proportional to the the number of
 * times notes occurred at the bin's pitch class in the piece, relative to all other pitch classes in the
 * piece (the histogram is normalized).
 *
 * @author Cory McKay
 */
public class FoldedFifthsPitchClassHistogramFeature
        extends MIDIFeatureExtractor {
    /* CONSTRUCTOR ******************************************************************************************/


    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public FoldedFifthsPitchClassHistogramFeature() {
        code = "P-3";
        String name = "Folded Fifths Pitch Class Histogram";
        String description = "A feature vector consisting of bin magnitudes of the folded fifths pitch class histogram described in the jSymbolic manual. Each bin corresponds to one of the 12 pitch classes, and the bins are ordered such that adjacent bins are separated by an ascending perfect fifth. Bin 0 corresponds to C. Enharmonic equivalents are assigned the same pitch class number. The magnitude of of each bin is proportional to the the number of times notes occurred at the bin's pitch class in the piece, relative to all other pitch classes in the piece (the histogram is normalized).";
        boolean is_sequential = true;
        int dimensions = 12;
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
        double[] result = null;
        if (null != sequence_info) {
            result = new double[sequence_info.fifths_pitch_histogram.length];
            System.arraycopy(sequence_info.fifths_pitch_histogram, 0, result, 0, result.length);
        }
        return result;
    }
}