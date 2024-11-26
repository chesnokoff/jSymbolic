package jsymbolic2.featureutils;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

public interface Feature {
    /**
     * @return Name.
     */
    String getName();

    /**
     * @return Description.
     */
    String getDescription();

    /**
     * @return Is sequential.
     */
    boolean isSequential();

    /**
     * @return Dimensions.
     */
    int getDimensions();

    /**
     * Returns unique code identifying a feature (see the HTML manual for identifying codes).
     *
     * @return The unique code identifying this particular feature.
     */
    String getCode();

    /**
     * Returns meta-data about a feature that extends this class. Includes the feature's unique getName; a
     * getDescription of what information the feature represents and how it is calculated; whether the feature
     * can be extracted from sequential windows of data; and its getDimensions (the number of values an extracted
     * feature will consist of). Note that a value of 0 in the returned getDimensions of the FeatureDefinition
     * implies that the feature getDimensions are variable, and depend on the analyzed data.
     *
     * @return The definition of this particular feature.
     */
    default FeatureDefinition getFeatureDefinition() {
        return new FeatureDefinition(getName(), getDescription(), isSequential(), getDimensions());
    }


    /**
     * Returns the names of other features that are needed in order to extract this feature. Will return null
     * if no other features are needed.
     *
     * @return The dependencies of this particular feature.
     */
    String[] getDependencies();


    /**
     * Returns the offsets of other features that are needed in order to extract this feature. Will return
     * null if no other features are needed.
     *
     * <p>The offset is in windows, and the indice of the retuned array corresponds to the indice of the array
     * returned by the getDependencies method. An offset of -1, for example, means that the feature returned
     * by getDependencies with the same indice should be provided to this class's extractFeature method with a
     * value that corresponds to the window prior to the window corresponding to this feature.</p>
     *
     * @return The dependency offsets of this particular feature.
     */
    int[] getDependencyOffsets();

    /**
     * The prototype method that classes extending this class will override in order to extract their
     * feature from a sequence of MIDI data.
     *
     * @param sequence             The MIDI data to extract the feature from.
     * @param sequence_info        Additional data already extracted from the the MIDI sequence.
     * @param other_feature_values The values of other features that may be needed to calculate this feature.
     *                             The order and offsets of these features must be the same as those returned
     *                             by this class' getDependencies and getDependencyOffsets methods,
     *                             respectively. The first indice indicates the feature/window, and the
     *                             second indicates the value.
     * @return The extracted feature value(s).
     * @throws Exception Throws an informative exception if the feature cannot be calculated.
     */
    double[] extractFeature(Sequence sequence, MIDIIntermediateRepresentations sequence_info, double[][] other_feature_values) throws Exception;
}
