package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the kurtosis of the pitch classes (where 0 corresponds to C, 1 to C#/Db,
 * etc.) of all pitched notes in the piece. Provides a measure of how peaked or flat the pitch class 
 * distribution is. The higher the kurtosis, the more the pitch classes are clustered near the mean and the
 * fewer outliers there are.
 *
 * @author Cory McKay
 */
public class PitchClassKurtosisFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public PitchClassKurtosisFeature()
	{
		code = "P-31";
		name = "Pitch Class Kurtosis";
		description = "Kurtosis of the pitch classes (where 0 corresponds to C, 1 to C#/Db, etc.) of all pitched notes in the piece. Provides a measure of how peaked or flat the pitch class distribution is. The higher the kurtosis, the more the pitch classes are clustered near the mean and the fewer outliers there are.";

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
			value = mckay.utilities.staticlibraries.MathAndStatsMethods.getSampleExcessKurtosis(sequence_info.pitch_classes_of_all_note_ons);
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}