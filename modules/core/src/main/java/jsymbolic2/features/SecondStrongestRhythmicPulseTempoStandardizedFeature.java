package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the bin index of the tempo-standardized beat histogram peak with the second 
 * highest magnitude.
 *
 * @author Cory McKay
 */
public class SecondStrongestRhythmicPulseTempoStandardizedFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public SecondStrongestRhythmicPulseTempoStandardizedFeature()
	{
		code = "R-58";
		name = "Second Strongest Rhythmic Pulse - Tempo Standardized";
		description = "Bin index of the tempo-standardized beat histogram peak with the second highest magnitude.";

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
			// Find the bin with the highest magnitude
			double max = 0.0;
			int max_index = 0;
			for (int bin = 0; bin < sequence_info.beat_histogram_120_bpm_standardized.length; bin++)
			{
				if (sequence_info.beat_histogram_120_bpm_standardized[bin] > max)
				{
					max = sequence_info.beat_histogram_120_bpm_standardized[bin];
					max_index = bin;
				}
			}

			// Find the second highest bin
			double second_highest_magnitude = 0.0;
			int second_highest_index = 0;
			for (int bin = 0; bin < sequence_info.beat_histogram_thresholded_table_120_bpm_standardized.length; bin++)
			{
				if ( sequence_info.beat_histogram_thresholded_table_120_bpm_standardized[bin][1] > second_highest_magnitude &&
				     bin != max_index )
				{
					second_highest_magnitude = sequence_info.beat_histogram_thresholded_table_120_bpm_standardized[bin][1];
					second_highest_index = bin;
				}
			}

			// Calculate the feature value
			value = (double) second_highest_index;
		}
		else value = -1.0;
		
		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}