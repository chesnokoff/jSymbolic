package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature extractor that calculates a normalized feature vector that indicates, for each rhythmic value,
 * the normalized median number of times that notes with that rhythmic value occur consecutively (either
 * vertically or horizontally) in the same voice (MIDI channel and track). Each bin corresponds to a different
 * rhythmic value, and they are numbered as follows: thirty second notes (or less) [0], sixteenth notes [1],
 * eighth notes [2], dotted eighth notes [3], quarter notes [4], dotted quarter notes [5], half notes [6],
 * dotted half notes [7], whole notes [8], dotted whole notes [9], double whole notes [10] and dotted double
 * whole notes (or more ) [11]. Both pitched and unpitched notes are included in this histogram. Tempo is, of
 * course, not relevant to this histogram. Notes with durations not precisely matching one of these rhythmic
 * note values are mapped to the closest note value (to filter out the effects of rubato or uneven human
 * rhythmic performances, for example). This histogram is calculated without regard to dynamics.
 *
 * @author Cory McKay
 */
public class RhythmicValueMedianRunLengthsHistogramFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 12;
    }

    @Override()
    public String getName() {
        return "Rhythmic Value Median Run Lengths Histogram";
    }

    @Override()
    public String getCode() {
        return "R-33";
    }

    @Override()
    public String getDescription() {
        return "A normalized feature vector that indicates, for each rhythmic value, the normalized median number of times that notes with that rhythmic value occur consecutively (either vertically or horizontally) in the same voice (MIDI channel and track). Each bin corresponds to a different rhythmic value, and they are numbered as follows: thirty second notes (or less) [0], sixteenth notes [1], eighth notes [2], dotted eighth notes [3], quarter notes [4], dotted quarter notes [5], half notes [6], dotted half notes [7], whole notes [8], dotted whole notes [9], double whole notes [10] and dotted double whole notes (or more ) [11]. Both pitched and unpitched notes are included in this histogram. Tempo is, of course, not relevant to this histogram. Notes with durations not precisely matching one of these rhythmic note values are mapped to the closest note value (to filter out the effects of rubato or uneven human rhythmic performances, for example). This histogram is calculated without regard to dynamics.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            // Initialize result
            result = new double[sequence_info.runs_of_same_rhythmic_value.length];
            // Access the runs
            LinkedList<Integer>[] runs_of_same_rhythmic_value = sequence_info.runs_of_same_rhythmic_value;
            // Calculate the values of the histogram bin by bin
            for (int i = 0; i < runs_of_same_rhythmic_value.length; i++) {
                double[] run_lengths_for_this_rhythmic_value = new double[runs_of_same_rhythmic_value[i].size()];
                for (int j = 0; j < run_lengths_for_this_rhythmic_value.length; j++) run_lengths_for_this_rhythmic_value[j] = (double) runs_of_same_rhythmic_value[i].get(j);
                result[i] = mckay.utilities.staticlibraries.MathAndStatsMethods.getMedianValue(run_lengths_for_this_rhythmic_value);
            }
            // Normalize the histogram
            result = mckay.utilities.staticlibraries.MathAndStatsMethods.normalize(result);
        }
        return result;
    }
}
