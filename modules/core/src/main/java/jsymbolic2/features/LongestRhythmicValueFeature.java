package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.apache.commons.lang3.ArrayUtils;

/**
 * A feature calculator that finds the rhythmic value of the longest note in the piece, expressed as a
 * fraction of a quarter note. For example, a value of 2 indicates that the longest note is a half note. This
 * calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not
 * influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given
 * note.
 *
 * @author Cory McKay
 */
public class LongestRhythmicValueFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/


	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public LongestRhythmicValueFeature()
	{
		code = "R-24";
		name = "Longest Rhythmic Value";
		description = "Rhythmic value of the longest note in the piece, expressed as a fraction of a quarter note. For example, a value of 2 indicates that the longest note is a half note. This calculation includes both pitched and unpitched notes, is calculated after rhythmic quantization, is not influenced by tempo, and is calculated without regard to the dynamics, voice or instrument of any given note.";

	}


	/* PUBLIC METHODS ***************************************************************************************/


	/**
	 * Extract this feature from the given sequence of MIDI data and its associated information.
	 *
	 * @param sequence				The MIDI data to extract the feature from.
	 * @param sequence_info			Additional data already extracted from the the MIDI sequence.
	 * @param other_feature_values	The values of other features that may be needed to calculate this feature. 
	 *								The order and offsets of these features must be the same as those returned
	 *								by this class' getDependencies and getDependencyOffsets methods, 
	 *								respectively. The first indice indicates the feature/window, and the 
	 *								second indicates the value.
	 * @return						The extracted feature value(s).
	 * @throws Exception			Throws an informative exception if the feature cannot be calculated.
	 */
	@Override
	public double[] extractFeature( Sequence sequence,
									MIDIIntermediateRepresentations sequence_info,
									double[][] other_feature_values )
			throws Exception
	{
		double value;
		if (sequence_info != null && ArrayUtils.isNotEmpty(sequence_info.rhythmic_value_of_each_note_in_quarter_notes))
		{
			int index_of_largest = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.rhythmic_value_of_each_note_in_quarter_notes);
			value = sequence_info.rhythmic_value_of_each_note_in_quarter_notes[index_of_largest];
		}
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}