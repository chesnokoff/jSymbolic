package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector indicating the fraction of (unpitched) notes played with each of the 47 MIDI Percussion
 * Key Map instruments. Has one entry for each of these 47 instruments, and the value of each is set to the
 * number of Note Ons played with the corresponding instrument, divided by the total number of Note Ons in the
 * piece. It should be noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they are the
 * ones that meet the official standard (they are correspondingly indexed in this feature vector from 0 to 46,
 * such that index 0 corresponds to Acoustic Bass Drum, index 4 corresponds to Hand Clap, etc.).
 *
 * @author Cory McKay
 */
public class NotePrevalenceOfUnpitchedInstrumentsFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 47;
    }

    @Override()
    public String getName() {
        return "Note Prevalence of Unpitched Instruments";
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
        return "I-4";
    }

    @Override()
    public String getDescription() {
        return "A feature vector indicating the fraction of (unpitched) notes played with each of the 47 MIDI Percussion Key Map instruments. Has one entry for each of these 47 instruments, and the value of each is set to the number of Note Ons played with the corresponding instrument, divided by the total number of Note Ons in the piece. It should be noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they are the ones that meet the official standard (they are correspondingly indexed in this feature vector from 0 to 46, such that index 0 corresponds to Acoustic Bass Drum, index 4 corresponds to Hand Clap, etc.).";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = null;
        if (sequence_info != null) {
            result = new double[47];
            for (int instrument = 35; instrument < 82; instrument++) {
                if (sequence_info.total_number_note_ons == 0)
                    result[instrument - 35] = 0.0;
                else
                    result[instrument - 35] = sequence_info.non_pitched_instrument_prevalence[instrument] / (double) sequence_info.total_number_note_ons;
            }
        }
        return result;
    }
}
