package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that is set to 1 if the initial meter is a standard complex meter (i.e. if the
 * numerator of the time signature is 5, 7, 9, 11, 13, 15, 22 or 25) and to 0 otherwise.
 *
 * @author Cory McKay
 */
public class ComplexInitialMeterFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public ComplexInitialMeterFeature()
	{
		code = "R-4";
		name = "Complex Initial Meter";
		description = "Set to 1 if the initial meter is a standard complex meter (i.e. if the numerator of the time signature is 5, 7, 9, 11, 13, 15, 22 or 25) and to 0 otherwise.";
		dependencies = new String[] { "Initial Time Signature" };
		offsets = null;
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
		double value = 0.0;
		if (sequence_info != null)
		{
			// Default to simple meter
			value = 0.0;
			
			// Get the numerator of the time signature
			double initial_time_signature_numerator = other_feature_values[0][0];
			
			// Set to compound meter if appropriate
			if ( initial_time_signature_numerator == 5.0 || initial_time_signature_numerator == 7.0 ||
			     initial_time_signature_numerator == 9.0 || initial_time_signature_numerator == 11.0 ||
			     initial_time_signature_numerator == 13.0 || initial_time_signature_numerator == 15.0 ||
			     initial_time_signature_numerator == 22.0 || initial_time_signature_numerator == 25.0 )
				value = 1.0;
		}

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}