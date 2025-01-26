package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the total number of notes, including both pitched and unpitched notes.
 *
 * @author Cory McKay
 */
public class TotalNumberOfNotesFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Total Number of Notes";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "R-9";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Total number of notes, including both pitched and unpitched notes.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = new double[1];
        result[0] = (double) sequence_info.total_number_note_ons;
        return result;
    }
}
