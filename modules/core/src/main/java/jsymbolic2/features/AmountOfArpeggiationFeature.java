package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of melodic intervals that are repeated notes, minor thirds,
 * major thirds, perfect fifths, minor sevenths, major sevenths, octaves, minor tenths or major tenths. This
 * is only a very approximate measure of the amount of arpeggiation in the music, of course.
 *
 * @author Cory McKay
 */
public class AmountOfArpeggiationFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Amount of Arpeggiation";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "M-8";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Fraction of melodic intervals that are repeated notes, minor thirds, major thirds, perfect fifths, minor sevenths, major sevenths, octaves, minor tenths or major tenths. This is only a very approximate measure of the amount of arpeggiation in the music, of course.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            value = sequence_info.melodic_interval_histogram[0] + sequence_info.melodic_interval_histogram[3] + sequence_info.melodic_interval_histogram[4] + sequence_info.melodic_interval_histogram[7] + sequence_info.melodic_interval_histogram[10] + sequence_info.melodic_interval_histogram[11] + sequence_info.melodic_interval_histogram[12] + sequence_info.melodic_interval_histogram[15] + sequence_info.melodic_interval_histogram[16];
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
