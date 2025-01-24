package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the tempo in beats per minute at the start of the piece. Set to the default
 * MIDI value (120 BPM) if no tempo is specified explicitly.
 *
 * @author Cory McKay
 */
public class InitialTempoFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Initial Tempo";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "RT-1";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Tempo in beats per minute at the start of the piece. Set to the default MIDI value (120 BPM) if no tempo is specified explicitly.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = ((Integer) sequence_info.overall_metadata[3]).doubleValue();
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
