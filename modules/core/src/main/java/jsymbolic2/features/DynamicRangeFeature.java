package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the loudness of the loudest note in the piece, minus the loudness of the
 * softest note.
 *
 * @author Cory McKay
 */
public class DynamicRangeFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public DynamicRangeFeature()
	{
		code = "D-1";
		name = "Dynamic Range";
		description = "Loudness of the loudest note in the piece, minus the loudness of the softest note.";

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
			int max = -1;
			int min = 128;
			for (int i = 0; i < sequence_info.note_loudnesses.length; i++)
			{
				for (int j = 0; j < sequence_info.note_loudnesses[i].length; j++)
				{
					if (sequence_info.note_loudnesses[i][j] > max)
						max = sequence_info.note_loudnesses[i][j];
					if (sequence_info.note_loudnesses[i][j] < min)
						min = sequence_info.note_loudnesses[i][j];
				}
			}
			
			if (max == -1 || min == 128)
				value = 0.0;
			else
				value = ((double) (max - min));
		}
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}