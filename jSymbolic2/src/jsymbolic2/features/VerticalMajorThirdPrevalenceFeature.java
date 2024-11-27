package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of the music by time where at least one wrapped vertical major
 * third is sounding (regardless of whatever other vertical intervals may or may not be sounding at the same
 * time). Only that part of the music where one or more pitched notes is sounding is included in this
 * calculation (rests and sections containing only unpitched notes are ignored).
 *
 * @author Cory McKay and Tristano Tenaglia
 */
public class VerticalMajorThirdPrevalenceFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Vertical Major Third Prevalence";
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
        return "C-26";
    }

    @Override()
    public String getDescription() {
        return "Fraction of the music by time where at least one wrapped vertical major third is sounding (regardless of whatever other vertical intervals may or may not be sounding at the same time). Only that part of the music where one or more pitched notes is sounding is included in this calculation (rests and sections containing only unpitched notes are ignored).";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // All MIDI pitches (NOT including Channel 10 unpitched notes sounding at each MIDI tick, with
            // ticks with no sounding notes excluded.
            short[][] pitches_present_by_tick_excluding_rests = sequence_info.pitches_present_by_tick_excluding_rests;
            double ticks_with_major_third = 0.0;
            for (int tick = 0; tick < pitches_present_by_tick_excluding_rests.length; tick++) {
                // The MIDI pitch numbers of all pitches found this tick
                short[] pitches_this_tick = pitches_present_by_tick_excluding_rests[tick];
                // Update ticks_with_major_third to find the number of ticks that contain a wrapped minor
                // third
                if (pitches_this_tick.length > 1) {
                    for (int i = 0; i < pitches_this_tick.length; i++) {
                        for (int j = 0; j < i; j++) {
                            if ((pitches_this_tick[i] - pitches_this_tick[j]) % 12 == 4) {
                                ticks_with_major_third++;
                                i = pitches_this_tick.length;
                                j = i;
                            }
                        }
                    }
                }
            }
            // Calculate the fraction
            if (pitches_present_by_tick_excluding_rests.length == 0)
                value = 0.0;
            else
                value = ticks_with_major_third / (double) pitches_present_by_tick_excluding_rests.length;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
