package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import java.util.List;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import mckay.utilities.sound.midi.MIDIMethods;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that calculates how much the note density (average number of notes per second) varies
 * throughout the piece. Takes into account all notes in all voices, including both pitched and unpitched
 * notes. In order to calculate this, the piece is broken into windows of 5 second duration, and the note
 * density of each window is calculated. The final value of this feature is then found by calculating the
 * standard deviation of the note densities of these windows. Set to 0 if there is insufficient music for more
 * than one window.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class NoteDensityVariabilityFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Note Density Variability";
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
        return "RT-6";
    }

    @Override()
    public String getDescription() {
        return "How much the note density (average number of notes per second) varies throughout the piece.  Takes into account all notes in all voices, including both pitched and unpitched notes. In order to calculate this, the piece is broken into windows of 5 second duration, and the note density of each window is calculated. The final value of this feature is then found by calculating the standard deviation of the note densities of these windows. Set to 0 if there is insufficient music for more than one window.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // Break MIDI sequence into 5 second MIDI windows
            double window_size = 5.0;
            double window_overlap = 0.0;
            double[] seconds_per_tick = MIDIMethods.getSecondsPerTick(sequence);
            List<int[]> startEndTickArrays = MIDIMethods.getStartEndTickArrays(sequence, window_size, window_overlap, seconds_per_tick);
            int[] start_ticks = startEndTickArrays.get(0);
            int[] end_ticks = startEndTickArrays.get(1);
            Sequence[] windows = MIDIMethods.breakSequenceIntoWindows(sequence, window_size, window_overlap, start_ticks, end_ticks);
            // Compute the note density for each window, using the NoteDensityFeature class
            double[] note_density_of_each_window = new double[windows.length];
            for (int window = 0; window < windows.length; window++) {
                Sequence this_window = windows[window];
                MIDIIntermediateRepresentations window_info = new MIDIIntermediateRepresentations(this_window);
                note_density_of_each_window[window] = new NoteDensityFeature().extractFeature(this_window, window_info, null)[0];
            }
            // Compute the standard deviation of the note densities
            if (windows.length < 2)
                value = 0.0;
            else
                value = mckay.utilities.staticlibraries.MathAndStatsMethods.getStandardDeviation(note_density_of_each_window);
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
