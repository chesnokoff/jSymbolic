package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.ChordTypeEnum;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the prevalence of minor triads divided by the prevalence of major triads.
 * This is weighted by how long the chords are held (e.g. a chord lasting a whole note will be weighted four
 * times as strongly as a chord lasting a quarter note). Set to 0 if there are no minor triads or if there
 * are no major triads.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class MinorMajorTriadRatioFeature implements Feature {

    @Override()
    public String getName() {
        return "Minor Major Triad Ratio";
    }

    @Override()
    public String[] getDependencies() {
        return new String[] { "Chord Type Histogram" };
    }

    @Override()
    public String getCode() {
        return "C-35";
    }

    @Override()
    public String getDescription() {
        return "The prevalence of minor triads divided by the prevalence of major triads. This is weighted by how long the chords are held (e.g. a chord lasting a whole note will be weighted four times as strongly as a chord lasting a quarter note). Set to 0 if there are no minor triads or if there are no major triads.";
    }

    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            double[] chord_type_histogram = other_feature_values[0];
            if (chord_type_histogram[ChordTypeEnum.MINOR_TRIAD.getChordTypeCode()] == 0.0 || chord_type_histogram[ChordTypeEnum.MAJOR_TRIAD.getChordTypeCode()] == 0.0)
                value = 0.0;
            else
                value = chord_type_histogram[ChordTypeEnum.MINOR_TRIAD.getChordTypeCode()] / chord_type_histogram[ChordTypeEnum.MAJOR_TRIAD.getChordTypeCode()];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
