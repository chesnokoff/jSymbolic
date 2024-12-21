package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector consisting of bin magnitudes of the basic pitch histogram described in the jSymbolic
 * manual. Each bin corresponds to one of the 128 MIDI pitches, ordered from lowest to highest, and with an
 * interval of a semitone between each (enharmonic equivalents are assigned the same pitch number). Bin 60
 * corresponds to middle C. The magnitude of of each bin is proportional to the the number of times notes
 * occurred at the bin's pitch in the piece, relative to all other pitches in the piece (the histogram is
 * normalized).
 *
 * @author Cory McKay
 */
public class BasicPitchHistogramFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 128;
    }

    @Override()
    public String getName() {
        return "Basic Pitch Histogram";
    }

    @Override()
    public String getCode() {
        return "P-1";
    }

    @Override()
    public String getDescription() {
        return "A feature vector consisting of bin magnitudes of the basic pitch histogram described in the jSymbolic manual. Each bin corresponds to one of the 128 MIDI pitches, ordered from lowest to highest, and with an interval of a semitone between each (enharmonic equivalents are assigned the same pitch number). Bin 60 corresponds to middle C. The magnitude of of each bin is proportional to the the number of times notes occurred at the bin's pitch in the piece, relative to all other pitches in the piece (the histogram is normalized).";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[sequence_info.basic_pitch_histogram.length];
            System.arraycopy(sequence_info.basic_pitch_histogram, 0, result, 0, result.length);
        }
        return result;
    }
}
