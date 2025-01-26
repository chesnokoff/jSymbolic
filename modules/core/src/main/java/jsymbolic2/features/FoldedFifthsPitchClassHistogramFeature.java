package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

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
public class FoldedFifthsPitchClassHistogramFeature extends Feature {

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
        return "Folded Fifths Pitch Class Histogram";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-3";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector consisting of bin magnitudes of the folded fifths pitch class histogram described in the jSymbolic manual. Each bin corresponds to one of the 12 pitch classes, and the bins are ordered such that adjacent bins are separated by an ascending perfect fifth. Bin 0 corresponds to C. Enharmonic equivalents are assigned the same pitch class number. The magnitude of of each bin is proportional to the the number of times notes occurred at the bin's pitch class in the piece, relative to all other pitch classes in the piece (the histogram is normalized).";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[sequence_info.fifths_pitch_histogram.length];
            System.arraycopy(sequence_info.fifths_pitch_histogram, 0, result, 0, result.length);
        }
        return result;
    }
}
