package jsymbolic2.featureutils;

import ace.datatypes.FeatureDefinition;
import java.util.Objects;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

/**
 * The prototype class for features designed to extract features from MIDI data. Each class that extends this
 * class will extract a particular feature from some sequence of MIDI data. Such classes do not store feature
 * values, only extract them. Classes that extend this class should have a constructor that sets the four
 * protected fields of this class.
 *
 * @author Cory McKay
 */
public abstract class MIDIFeatureExtractor {
    /* FIELDS ***********************************************************************************************/


    /**
     * The unique code identifying a feature that extends this class (see the HTML manual for identifying
     * codes). Should be one or more letters identifying the feature group the feature belongs to, followed
     * by a hyphen, followed by the number of the feature within that group. For example, a value of I-7
     * would be appropriate for the seventh feature of the Instrumentation feature group.
     */
    protected String code;

    /**
     * Meta-data about a feature that extends this class. Includes the feature's unique name; a description
     * of what information the feature represents and how it is calculated; whether the feature can be
     * extracted from sequential windows of data; and its dimensions (the number of values an extracted
     * feature will consist of). Note that a value of 0 in the returned dimensions of the FeatureDefinition
     * implies that the feature dimensions are variable, and depend on the analyzed data.
     */
    protected FeatureDefinition definition;

    /**
     * The names of other features that are needed in order for a feature to be calculated. Will be null if
     * a feature does not depend on any other features.
     */
    protected String[] dependencies;

    /**
     * The offset in windows of each of the features named in the dependencies field. An offset of -1, for
     * example, means that the feature in dependencies with the same index value should be provided to this
     * class' extractFeature method with a value that corresponds to the window prior to the window
     * corresponding to this feature. Will be null if there are no dependencies. This must be null, 0 or a
     * negative number. Positive numbers are not allowed.
     */
    protected int[] offsets;
    protected String name;
    protected String description;
    protected boolean isSequentional = true;
    protected int dimensions = 1;


    /* PUBLIC METHODS ***************************************************************************************/



    /**
     * Returns meta-data about a feature that extends this class. Includes the feature's unique name; a
     * description of what information the feature represents and how it is calculated; whether the feature
     * can be extracted from sequential windows of data; and its dimensions (the number of values an extracted
     * feature will consist of). Note that a value of 0 in the returned dimensions of the FeatureDefinition
     * implies that the feature dimensions are variable, and depend on the analyzed data.
     *
     * @return The definition of this particular feature.
     */
    public FeatureDefinition getFeatureDefinition() {
        return new FeatureDefinition(getName(), getDescription(), isSequentional(), getDimensions());
    }


    /**
     * Returns the names of other features that are needed in order to extract this feature. Will return null
     * if no other features are needed.
     *
     * @return The dependencies of this particular feature.
     */
    public String[] getDepenedencies() {
        return dependencies;
    }


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
    public int[] getDepenedencyOffsets() {
        return offsets;
    }


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
     * @throws Exception Throws an informative exception if the feature cannot be calculated.
     * @return The extracted feature value(s).
     */
    public abstract double[] extractFeature(Sequence sequence,
                                            MIDIIntermediateRepresentations sequence_info,
                                            double[][] other_feature_values)
            throws Exception;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSequentional() {
        return isSequentional;
    }

    public int getDimensions() {
        return dimensions;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getFeatureDefinition().getFeatureDescription();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MIDIFeatureExtractor midiFeatureExtractor) {
            return Objects.equals(getCode(), midiFeatureExtractor.getCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCode());
    }
}