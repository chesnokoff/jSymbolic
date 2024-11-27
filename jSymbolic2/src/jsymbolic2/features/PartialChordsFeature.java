package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.ChordTypeEnum;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of simultaneously sounding pitch groups that consist of only
 * two pitch classes. This is weighted by how long pitch groups are held (e.g. a pitch group lasting a whole
 * note will be weighted four times as strongly as a pitch group lasting a quarter note).
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class PartialChordsFeature implements Feature {

    @Override()
    public int getDimensions() {
        return 1;
    }

    @Override()
    public String getName() {
        return "Partial Chords";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Chord Type Histogram" };
    }

    @Override()
    public int[] getDependencyOffsets() {
        return null;
    }

    @Override()
    public String getCode() {
        return "C-28";
    }

    @Override()
    public String getDescription() {
        return "Fraction of simultaneously sounding pitch groups that consist of only two pitch classes. This is weighted by how long pitch groups are held (e.g. a pitch group lasting a whole note will be weighted four times as strongly as a pitch group lasting a quarter note).";
    }

    @Override()
    public boolean isSequential() {
        return true;
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] chord_type_histogram = other_feature_values[0];
            value = chord_type_histogram[ChordTypeEnum.PARTIAL_CHORD.getChordTypeCode()];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
