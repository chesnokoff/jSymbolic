package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature vector indicating which unpitched instruments are present. Has one entry for each of the 47 MIDI
 * Percussion Key Map instruments. Each value is set to 1 if at least one note is played using the
 * corresponding instrument, or to 0 if that instrument is never used. It should be noted that only MIDI
 * Channel 10 instruments 35 to 81 are included here, as they are the ones that meet the official standard
 * (they are correspondingly indexed in this feature vector from 0 to 46, such that index 0 corresponds to
 * Acoustic Bass Drum, index 4 corresponds to Hand Clap, etc.).
 *
 * @author Cory McKay
 */
public class UnpitchedInstrumentsPresentFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 47;
    }

    @Override()
    public String getName() {
        return "Unpitched Instruments Present";
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
        return "I-2";
    }

    @Override()
    public String getDescription() {
        return "A feature vector indicating which unpitched instruments are present. Has one entry for each of the 47 MIDI Percussion Key Map instruments. Each value is set to 1 if at least one note is played using the corresponding instrument, or to 0 if that instrument is never used. It should be noted that only MIDI Channel 10 instruments 35 to 81 are included here, as they are the ones that meet the official standard (they are correspondingly indexed in this feature vector from 0 to 46, such that index 0 corresponds to Acoustic Bass Drum, index 4 corresponds to Hand Clap, etc.).";
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
                if (sequence_info.non_pitched_instrument_prevalence[instrument] > 0)
                    result[instrument - 35] = 1.0;
                else
                    result[instrument - 35] = 0.0;
            }
        }
        return result;
    }
}
