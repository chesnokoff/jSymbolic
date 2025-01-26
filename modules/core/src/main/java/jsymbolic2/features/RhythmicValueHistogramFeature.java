package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature extractor that calculates a feature vector representing a normalized histogram where the value of
 * each bin specifies the fraction of all notes in the music with a rhythmic value corresponding to that of
 * the given bin. The bins are numbered as follows: thirty second notes (or less) [0], sixteenth notes [1],
 * eighth notes [2], dotted eighth notes [3], quarter notes [4], dotted quarter notes [5], half notes [6],
 * dotted half notes [7], whole notes [8], dotted whole notes [9], double whole notes [10] and dotted double
 * whole notes (or more ) [11]. Both pitched and unpitched notes are included in this histogram. Tempo is, of
 * course, not relevant to this histogram. Notes with durations not precisely matching one of these rhythmic
 * note values are mapped to the closest note value (to filter out the effects of rubato or uneven human
 * rhythmic performances, for example). This histogram is calculated without regard to the dynamics, voice or
 * instrument of any given note.
 *
 * @author Cory McKay
 */
public class RhythmicValueHistogramFeature extends Feature {

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
        return "Rhythmic Value Histogram";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-13";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "A feature vector, representing a normalized histogram where the value of each bin specifies the fraction of all notes in the music with a rhythmic value corresponding to that of the given bin. The bins are numbered as follows: thirty second notes (or less) [0], sixteenth notes [1], eighth notes [2], dotted eighth notes [3], quarter notes [4], dotted quarter notes [5], half notes [6], dotted half notes [7], whole notes [8], dotted whole notes [9], double whole notes [10] and dotted double whole notes (or more ) [11]. Both pitched and unpitched notes are included in this histogram. Tempo is, of course, not relevant to this histogram. Notes with durations not precisely matching one of these rhythmic note values are mapped to the closest note value (to filter out the effects of rubato or uneven human rhythmic performances, for example). This histogram is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[sequence_info.rhythmic_value_histogram.length];
            System.arraycopy(sequence_info.rhythmic_value_histogram, 0, result, 0, result.length);
        }
        return result;
    }
}
