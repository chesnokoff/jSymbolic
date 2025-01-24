package jsymbolic2.features;

import jsymbolic2.featureutils.Feature;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds whether the piece is major or minor, as indicated by the first encountered
 * major/minor metadata tag in the piece. Set to 0 if the metadata indicates that the piece is major, or set
 * to 1 if the metadata indicates that it is minor. Defaults to 0 if the key signature is unknown.
 *
 * @author Cory McKay
 */
public class MajorOrMinorFeature implements Feature {

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getName() {
        return "Major or Minor";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getCode() {
        return "P-33";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public String getDescription() {
        return "Whether the piece is major or minor, as indicated by the first encountered major/minor metadata tag in the piece. Set to 0 if the metadata indicates that the piece is major, or set to 1 if the metadata indicates that it is minor. Defaults to 0 if the key signature is unknown.";
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception {
        double value;
        if (sequence_info != null)
            value = ((Integer) sequence_info.overall_metadata[0]).doubleValue();
        else
            value = -1.0;
        double[] result = new double[1];
        result[0] = value;
        return result;
    }
}
