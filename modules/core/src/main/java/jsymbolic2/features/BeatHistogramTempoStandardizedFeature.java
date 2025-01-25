package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature extractor that calculates a feature vector consisting of the bin magnitudes of the beat histogram
 * described in the jSymbolic manual. However, the tempo of the music is standardized to 120 BPM throughout
 * the piece before this histogram is calculated. This means that variations in tempo within a single piece
 * are in effect eliminated for the purposes of this histogram. The tempo-independent beat histograms of
 * different pieces can also be compared in a way that is independent of potential tempo differences between
 * the pieces. Rubato and dynamics do still influence the tempo-independent beat histogram, however. Also, the
 * first 40 bins are not included in this feature vector, as is the case with the basic beat histogram. Each
 * bin corresponds to a different beats per minute periodicity, with tempo increasing with the bin index. The
 * magnitude of each bin is proportional to the cumulative loudness (MIDI velocity) of the notes that occur at
 * that bin's rhythmic periodicity. The histogram is normalized.
 *
 * @author Cory McKay
 */
public class BeatHistogramTempoStandardizedFeature implements Feature {

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
        return "Beat Histogram Tempo Standardized";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-53";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector consisting of the bin magnitudes of the beat histogram described in the jSymbolic manual. However, the tempo of the music is standardized to 120 BPM throughout the piece before this histogram is calculated. This means that variations in tempo within a single piece are in effect eliminated for the purposes of this histogram. The tempo-independent beat histograms of different pieces can also be compared in a way that is independent of potential tempo differences between the pieces. Rubato and dynamics do still influence the tempo-independent beat histogram, however. Also, the first 40 bins are not included in this feature vector, as is the case with the basic beat histogram. Each bin corresponds to a different beats per minute periodicity, with tempo increasing with the bin index. The magnitude of each bin is proportional to the cumulative loudness (MIDI velocity) of the notes that occur at that bin's rhythmic periodicity. The histogram is normalized.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[sequence_info.beat_histogram_120_bpm_standardized.length - 40];
            System.arraycopy(sequence_info.beat_histogram_120_bpm_standardized, 40, result, 0, result.length);
        }
        return result;
    }
}
