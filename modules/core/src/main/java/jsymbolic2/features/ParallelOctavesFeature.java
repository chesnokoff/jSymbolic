package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of movements between voices that consist of parallel octaves
 * (the fraction is calculated relative to the total amount of qualifying transitions, including all parallel,
 * similar, contrary and oblique transitions). If more than two voices are involved in a given pitch
 * transition, then each possible pair of voices comprising the transition is included in the calculation.
 * Note that only transitions from one set of pitches to another set of pitches comprising the same number of
 * pitches as the first are included in this calculation, although a brief lookahead is performed in order to
 * accommodate small rhythmic desynchronizations (e.g. if a MIDI file is a transcription of a human
 * performance). Only unique pitches are included in this calculation (unisons are treated as a single pitch).
 * All pitches present are considered, regardless of their MIDI channel or track; this has the advantage of
 * accommodating polyphonic instruments such as piano or guitar, but the consequence is that this feature does
 * not incorporate an awareness of voice crossing.
 *
 * @author Cory McKay
 */
public class ParallelOctavesFeature extends Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Parallel Octaves";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "T-24";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Fraction of movements between voices that consist of parallel octaves (the fraction is calculated relative to the total amount of qualifying transitions, including all parallel, similar, contrary and oblique transitions). If more than two voices are involved in a given pitch transition, then each possible pair of voices comprising the transition is included in the calculation. Note that only transitions from one set of pitches to another set of pitches comprising the same number of pitches as the first are included in this calculation, although a brief lookahead is performed in order to accommodate small rhythmic desynchronizations (e.g. if a MIDI file is a transcription of a human performance). Only unique pitches are included in this calculation (unisons are treated as a single pitch). All pitches present are considered, regardless of their MIDI channel or track; this has the advantage of accommodating polyphonic instruments such as piano or guitar, but the consequence is that this feature does not incorporate an awareness of voice crossing.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double[] result = new double[1];
        result[0] = sequence_info.parallel_octaves_fraction;
        return result;
    }
}
