package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector consisting of the bin magnitudes of the melodic interval histogram described above. Each
 * bin corresponds to a melodic interval, and the bin index indicates the number of semitones comprising the
 * interval associated with the bin (there are 128 bins in all). For example, bin 0 corresponds to repeated
 * pitches, bin 1 to a melodic interval of one semitone, bin 2 to a melodic interval of 2 semitones, etc. The
 * magnitude of each bin is proportional to the fraction of melodic intervals in the piece that are of the
 * kind associated with the bin (this histogram is normalized). Rising and falling intervals are treated as
 * identical. Melodies are assumed to be contained within individual MIDI tracks and channels, so melodic
 * intervals are found separately for each track and channel before being combined in this histogram. It is
 * also assumed that there is only one melody at a time per MIDI channel (if multiple notes occur
 * simultaneously on the same MIDI tick on the same MIDI track and channel, then all notes but the first note
 * on that tick are ignored). Other than this, all notes on the same track and the same channel are treated as
 * if they are part of a single melody. It is also assumed that melodies do not cross MIDI tracks or channels
 * (i.e. that they are each separately contained in their own track and channel). Only pitched notes are
 * considered, so all notes on the unpitched MIDI Channel 10 are ignored.
 *
 * @author Cory McKay
 */
public class MelodicIntervalHistogramFeature implements Feature {

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
        return "Melodic Interval Histogram";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "M-1";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector consisting of the bin magnitudes of the melodic interval histogram described above. Each bin corresponds to a melodic interval, and the bin index indicates the number of semitones comprising the interval associated with the bin (there are 128 bins in all). For example, bin 0 corresponds to repeated pitches, bin 1 to a melodic interval of one semitone, bin 2 to a melodic interval of 2 semitones, etc. The magnitude of each bin is proportional to the fraction of melodic intervals in the piece that are of the kind associated with the bin (this histogram is normalized). Rising and falling intervals are treated as identical. Melodies are assumed to be contained within individual MIDI tracks and channels, so melodic intervals are found separately for each track and channel before being combined in this histogram. It is also assumed that there is only one melody at a time per MIDI channel (if multiple notes occur simultaneously on the same MIDI tick on the same MIDI track and channel, then all notes but the first note on that tick are ignored). Other than this, all notes on the same track and the same channel are treated as if they are part of a single melody. It is also assumed that melodies do not cross MIDI tracks or channels (i.e. that they are each separately contained in their own track and channel). Only pitched notes are considered, so all notes on the unpitched MIDI Channel 10 are ignored.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[sequence_info.melodic_interval_histogram.length];
            System.arraycopy(sequence_info.melodic_interval_histogram, 0, result, 0, result.length);
        }
        return result;
    }
}
