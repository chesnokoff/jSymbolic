package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the pitch class that occurs most frequently compared to other pitch
 * classes. A value of 0 corresponds to C, and pitches increase chromatically by semitone in integer untis
 * (e.g. a value of 2 would mean that D is the most common pitch class). Enharmonic equivalents are treated as
 * a single pitch class.
 *
 * @author Cory McKay
 */
public class MostCommonPitchClassFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public MostCommonPitchClassFeature()
	{
		code = "P-17";
		name = "Most Common Pitch Class";
		description = "The pitch class that occurs most frequently compared to other pitch classes. A value of 0 corresponds to C, and pitches increase chromatically by semitone in integer untis (e.g. a value of 2 would mean that D is the most common pitch class). Enharmonic equivalents are treated as a single pitch class.";

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
		if (sequence_info != null)
		{
			int max_index = mckay.utilities.staticlibraries.MathAndStatsMethods.getIndexOfLargest(sequence_info.pitch_class_histogram);
			value = (double) max_index;
		} 
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}