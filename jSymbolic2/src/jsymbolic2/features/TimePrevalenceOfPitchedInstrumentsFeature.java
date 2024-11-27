package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector indicating the fraction of time during which (pitched) notes are being sounded by each of
 * the 128 General MIDI Instrument patches (0 is Acoustic Piano, 40 is Violin, etc.). Has one entry for each
 * of these instruments, and the value of each is set to to the total time in seconds in a piece during which
 * at least one note is being sounded with the corresponding MIDI patch, divided by the total length of the
 * piece in seconds.
 *
 * @author Cory McKay
 */
public class TimePrevalenceOfPitchedInstrumentsFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 128;
    }

    @Override()
    public String getName() {
        return "Time Prevalence of Pitched Instruments";
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
        return "I-5";
    }

    @Override()
    public String getDescription() {
        return "A feature vector indicating the fraction of time during which (pitched) notes are being sounded by each of the 128 General MIDI Instrument patches (0 is Acoustic Piano, 40 is Violin, etc.). Has one entry for each of these instruments, and the value of each is set to to the total time in seconds in a piece during which at least one note is being sounded with the corresponding MIDI patch, divided by the total length of the piece in seconds.";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[128];
            if (sequence_info.sequence_duration == 0)
                for (int instrument = 0; instrument < result.length; instrument++) result[instrument] = 0.0;
            else
                for (int instrument = 0; instrument < result.length; instrument++) result[instrument] = sequence_info.pitched_instrument_prevalence[instrument][1] / (double) sequence_info.sequence_duration;
        }
        return result;
    }
}
