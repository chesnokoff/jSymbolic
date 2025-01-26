package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import javax.sound.midi.Sequence;
import java.util.LinkedList;

/**
 * A feature calculator that finds the number of pitched notes that are each associated with exactly one MIDI
 * Pitch Bend message, divided by the total number of pitched Note Ons in the piece. Set to 0 if there are no
 * pitched Note Ons in the piece.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class MicrotonePrevalenceFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Microtone Prevalence";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-41";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Number of pitched notes that are each associated with exactly one MIDI Pitch Bend message, divided by the total number of pitched Note Ons in the piece. Set to 0 if there are no pitched Note Ons in the piece.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null) {
            // A list of lists of pitch bends associated with pitched (i.e. not Channel 10) notes. Each entry
            // of the root list corresponds to a note that has at least one pitch bend message associated with
            // it. Each such entry contains a list of all pitchbend messages (second MIDI data byte stored as
            // an Integer) associated with the note, in the order that they occurred.
            LinkedList pitch_bends_list = sequence_info.pitch_bends_list;
            if (// if there are no pitched notes
            // if there are no pitchbend messages
            sequence_info.total_number_pitched_note_ons == 0 || pitch_bends_list.isEmpty())
                value = 0.0;
            else {
                double number_single_pitchbend_notes = 0;
                for (Object pitch_bend_note : pitch_bends_list) {
                    LinkedList bends_associated_with_this_note = (LinkedList) pitch_bend_note;
                    // Only count if a single pitch bend is associated with this Note On
                    if (bends_associated_with_this_note.size() == 1)
                        number_single_pitchbend_notes++;
                }
                value = number_single_pitchbend_notes / sequence_info.total_number_pitched_note_ons;
            }
        } else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
