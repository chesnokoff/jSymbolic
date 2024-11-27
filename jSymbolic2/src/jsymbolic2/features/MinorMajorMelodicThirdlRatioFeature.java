package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the combined fraction of all melodic intervals that are minor thirds,
 * divided by the combined fraction of all melodic intervals that are major thirds. Set to 0 if there are no
 * melodic minor thirds or melodic major thirds.
 *
 * @author Cory McKay
 */
public class MinorMajorMelodicThirdlRatioFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Minor Major Melodic Third Ratio";
    }

    @Override()
    public String[] getDependencies() {
        return null;
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "M-20";
    }

    @Override()
    public String getDescription() {
        return "Combined fraction of all melodic intervals that are minor thirds, divided by the combined fraction of all melodic intervals that are major thirds. Set to 0 if there are no melodic minor thirds or melodic major thirds.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value = 0.0;
        if (sequence_info != null) {
            if (sequence_info.melodic_interval_histogram[3] != 0 && sequence_info.melodic_interval_histogram[4] != 0)
                value = sequence_info.melodic_interval_histogram[3] / sequence_info.melodic_interval_histogram[4];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
