package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the number of different quantized rhythmic values (e.g. quarter notes,
 * dotted quarter notes, half notes, etc.) that represent at least 15% of the rhythmic values in the music.
 * This is found by finding all non-zero entries in the Rhythmic Value Histogram.
 *
 * @author Cory McKay
 */
public class NumberOfCommonRhythmicValuesPresentFeature implements Feature {

    @Override()
    public String getName() {
        return "Number of Common Rhythmic Values Present";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Rhythmic Value Histogram" };
    }

    @Override()
    public String getCode() {
        return "R-16";
    }

    @Override()
    public String getDescription() {
        return "Number of different quantized rhythmic values (e.g. quarter notes, dotted quarter notes, half notes, etc.) that represent at least 15% of the rhythmic values in the music. This is found by finding all non-zero entries in the Rhythmic Value Histogram.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] rhythmic_value_histogram = other_feature_values[0];
            int count = 0;
            for (int bin = 0; bin < rhythmic_value_histogram.length; bin++) if (rhythmic_value_histogram[bin] >= 0.15)
                count++;
            value = (double) count;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
