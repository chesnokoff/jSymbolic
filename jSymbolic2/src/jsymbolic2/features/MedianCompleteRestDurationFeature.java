package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that calculates the median duration of complete rests in the piece, expressed as a
 * fraction of the duration of a quarter note. A complete rest is defined as a period in which no pitched
 * notes are sounding on any MIDI channel. Non-pitched (MIDI channel 10) notes are not considered in this
 * calculation. Rests shorter than 0.1 of a quarter note are ignored in this calculation.
 *
 * @author Cory McKay
 */
public class MedianCompleteRestDurationFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Median Complete Rest Duration";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-48";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Median duration of complete rests in the piece, expressed as a fraction of the duration of a quarter note. A complete rest is defined as a period in which no pitched notes are sounding on any MIDI channel. Non-pitched (MIDI channel 10) notes are not considered in this calculation. Rests shorter than 0.1 of a quarter note are ignored in this calculation.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            if (sequence_info.complete_rest_durations == null)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getMedianValue(sequence_info.complete_rest_durations);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
