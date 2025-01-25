package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature extractor that finds the average number of pitches sounding simultaneously. Rests are excluded
 * from this calculation. Unisons are also excluded from this calculation, but octave multiples are included
 * in it.
 *
 * @author Cory McKay
 */
public class AverageNumberOfSimultaneousPitchesFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Average Number of Simultaneous Pitches";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "C-6";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Average number of pitches sounding simultaneously. Rests are excluded from this calculation. Unisons are also excluded from this calculation, but octave multiples are included in it.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // All MIDI pitches (NOT including Channel 10 unpitched notes sounding at each MIDI tick, with
            // ticks with no sounding notes excluded.
            short[][] pitches_present_by_tick_excluding_rests = sequence_info.pitches_present_by_tick_excluding_rests;
            // Will hold the number of pitches sounding each tick
            short[] number_pitches_by_tick = new short[pitches_present_by_tick_excluding_rests.length];
            // Fill in number_pitches_by_tick tick by tick
            for (int tick = 0; tick < pitches_present_by_tick_excluding_rests.length; tick++) number_pitches_by_tick[tick] = (short) pitches_present_by_tick_excluding_rests[tick].length;
            // Find the average of the number of pitches sounding simultaneously
            if (number_pitches_by_tick == null || number_pitches_by_tick.length == 0)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(number_pitches_by_tick);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
