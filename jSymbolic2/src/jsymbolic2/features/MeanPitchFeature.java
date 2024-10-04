package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the mean MIDI pitch value, averaged across all pitched notes in the piece.
 * Set to 0 if there are no pitched notes.
 * 
 * @author Cory McKay
 */
public class MeanPitchFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public MeanPitchFeature()
	{
		code = "P-14";
		String name = "Mean Pitch";
		String description = "Mean MIDI pitch value, averaged across all pitched notes in the piece. Set to 0 if there are no pitched notes.";
		boolean is_sequential = true;
		int dimensions = 1;
		definition = new FeatureDefinition(name, description, is_sequential, dimensions);
		dependencies = null;
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
		double value;
		if (sequence_info != null)
		{
			// Find the total pitch
			double cumulative_pitch_values = 0;
			for (int chan = 0; chan < sequence_info.getChannel_statistics().length; chan++)
			{
				if (chan != (10 - 1)) // Exclude the unpitched percussion Channel 10
				{
					cumulative_pitch_values += sequence_info.getChannel_statistics()[chan][6]
							* sequence_info.getChannel_statistics()[chan][0];
				}
			}

			// Calculate the feature value
			if (sequence_info.getTotal_number_pitched_note_ons() == 0)
				value = 0.0;
			else 
				value = cumulative_pitch_values / (double) sequence_info.getTotal_number_pitched_note_ons();
		}
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}