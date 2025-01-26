package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature extractor that calculates a feature vector consisting of the bin magnitudes of the beat histogram
 * described in the jSymbolic manual. The first 40 bins are not included in this feature vector, however. Each
 * bin corresponds to a different beats per minute periodicity, with tempo increasing with the bin index. The
 * magnitude of each bin is proportional to the cumulative loudness (MIDI velocity) of the notes that occur at
 * that bin's rhythmic periodicity. The histogram is normalized.
 *
 * @author Cory McKay
 */
public class BeatHistogramFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public int getDimensions() {
        return 161;
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Beat Histogram";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-16";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector consisting of the bin magnitudes of the beat histogram described in the jSymbolic manual. The first 40 bins are not included in this feature vector, however. Each bin corresponds to a different beats per minute periodicity, with tempo increasing with the bin index. The magnitude of each bin is proportional to the cumulative loudness (MIDI velocity) of the notes that occur at that bin's rhythmic periodicity. The histogram is normalized.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[sequence_info.beat_histogram.length - 40];
            System.arraycopy(sequence_info.beat_histogram, 40, result, 0, result.length);
        }
        return result;
    }
}
