package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MEIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;
import javax.sound.midi.Sequence;

/**
 * A feature calculator that finds the total number of grace notes in a piece (i.e. the number of notes
 * indicated as grace notes in the MEI encoding) divided by the total number of pitched notes in the music.
 *
 * <p>Since this is an MEI-specific feature, the feature information is obtained from the
 * {@link MeiSpecificStorage} class. The associated code can be found
 * <a href="https://github.com/DDMAL/jMei2Midi">jMei2Midi</a>, which is included as a dependency of
 * jSymbolic.</p>
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class NumberOfGraceNotesMeiFeature extends MEIFeatureExtractor {

    /* CONSTRUCTOR ******************************************************************************************/
    /**
     * Basic constructor that sets the values of the fields inherited from this class' superclass.
     */
    public NumberOfGraceNotesMeiFeature() {
        code = "S-1";
        String name = "Number of Grace Notes";
        String description = "The total number of grace notes in a piece (i.e. the number of notes indicated as grace notes in the MEI encoding) divided by the total number of pitched notes in the music.";
        boolean is_sequential = true;
        int dimensions = 1;
        definition = new FeatureDefinition(name, description, is_sequential, dimensions);
        dependencies = null;
        offsets = null;
    }

    /* PUBLIC METHODS ***************************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public double[] extractMEIFeature(MeiSpecificStorage meiSpecificStorage, Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (null != sequence_info) {
            double total_number_pitched_note_ons = sequence_info.total_number_pitched_note_ons;
            double number_of_grace_notes = meiSpecificStorage.getGraceNoteList().size();
            if (0.0 == total_number_pitched_note_ons || 0.0 == number_of_grace_notes)
                value = 0.0;
            else
                value = number_of_grace_notes / total_number_pitched_note_ons;
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
