package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the total duration (in seconds) of the piece.
 *
 * @author Cory McKay
 */
public class DurationInSecondsFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Duration in Seconds";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-4";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Total duration (in seconds) of the piece.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = new double[1];
        result[0] = (double) (sequence.getMicrosecondLength() / 1000000.0);
        return result;
    }
}
