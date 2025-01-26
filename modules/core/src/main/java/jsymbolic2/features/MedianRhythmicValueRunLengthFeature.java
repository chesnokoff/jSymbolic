package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the median number of notes of the same rhythmic value that occur
 * consecutively (either vertically or horizontally) in the same voice (MIDI channel and track). This
 * calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization and not
 * influenced by neither tempo nor dynamics.
 * *
 *
 * @author Cory McKay
 */
public class MedianRhythmicValueRunLengthFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Median Rhythmic Value Run Length";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-35";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Median number of notes of the same rhythmic value that occur consecutively (either vertically or horizontally) in the same voice (MIDI channel and track). This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization and not influenced by neither tempo nor dynamics.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Access the runs
            LinkedList<Integer>[] runs_of_same_rhythmic_value = sequence_info.runs_of_same_rhythmic_value;
            LinkedList<Integer> collapsed_runs_of_same_rhythmic_value = new LinkedList<>();
            for (int i = 0; i < runs_of_same_rhythmic_value.length; i++) for (int j = 0; j < runs_of_same_rhythmic_value[i].size(); j++) collapsed_runs_of_same_rhythmic_value.add(runs_of_same_rhythmic_value[i].get(j));
            double[] array_of_all_runs_of_same_rhythmic_value = new double[collapsed_runs_of_same_rhythmic_value.size()];
            for (int i = 0; i < array_of_all_runs_of_same_rhythmic_value.length; i++) array_of_all_runs_of_same_rhythmic_value[i] = (double) collapsed_runs_of_same_rhythmic_value.get(i);
            // Calculate the final value
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getMedianValue(array_of_all_runs_of_same_rhythmic_value);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
