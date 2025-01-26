package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the standard deviation of the offsets of note durations of notes from the
 * idealized durations of each of their nearest quantized rhythmic values, expressed as a fraction of the
 * duration of an idealized quantized quarter note. Offsets are treated as absolute values, so offsets that
 * are longer or shorter than each idealized duration are both treated as identical positive numbers in this
 * calculation. This feature provides an indication of how much these offsets vary or, expressed slightly
 * differently, how rhythmically consistent note durations are. A higher value indicates greater variety in
 * offsets between different notes. Both pitched and unpitched notes are included, and this is calculated
 * without regard to the dynamics, voice or instrument of any given note.
 *
 * @author Cory McKay
 */
public class VariabilityOfRhythmicValueOffsetsFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Variability of Rhythmic Value Offsets";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-40";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Standard deviation of the offsets of note durations of notes from the idealized durations of each of their nearest quantized rhythmic values, expressed as a fraction of the duration of an idealized quantized quarter note. Offsets are treated as absolute values, so offsets that are longer or shorter than each idealized duration are both treated as identical positive numbers in this calculation. This feature provides an indication of how much these offsets vary or, expressed slightly differently, how rhythmically consistent note durations are. A higher value indicates greater variety in offsets between different notes. Both pitched and unpitched notes are included, and this is calculated without regard to the dynamics, voice or instrument of any given note.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(sequence_info.rhythmic_value_offsets);
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
