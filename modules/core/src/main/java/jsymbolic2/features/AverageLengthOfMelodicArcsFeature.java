package jsymbolic2.features;

import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average number of notes that separate melodic peaks and troughs.
 * Similar assumptions are made in the calculation of this feature as for the Melodic Interval Histogram. Set
 * to 0 if no melodic arcs are found.
 *
 * @author Cory McKay
 */
public class AverageLengthOfMelodicArcsFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public AverageLengthOfMelodicArcsFeature()
	{
		code = "M-23";
		name = "Average Length of Melodic Arcs";
		description = "Average number of notes that separate melodic peaks and troughs. Similar assumptions are made in the calculation of this feature as for the Melodic Interval Histogram. Set to 0 if no melodic arcs are found.";

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
			int total_number_intervening_intervals = 0;
			int number_arcs = 0;
			
			for (int track = 0; track < sequence_info.melodic_intervals_by_track_and_channel.size(); track++)
			{
				LinkedList<Integer>[] melodic_intervals_by_channel = sequence_info.melodic_intervals_by_track_and_channel.get(track);
			
				for (int chan = 0; chan < melodic_intervals_by_channel.length; chan++)
				{
					if (chan != (10 - 1))
					{
						// Convert list of melodic intervals in this channel to an array
						Object[] list_contents = melodic_intervals_by_channel[chan].toArray();
						int[] intervals = new int[list_contents.length];
						for (int i = 0; i < intervals.length; i++)
							intervals[i] = ((Integer) list_contents[i]).intValue();

						// Find the number of arcs
						int direction = 0;
						for (int i = 0; i < intervals.length; i++)
						{
							// If arc is currently decending
							if (direction == -1)
							{
								if (intervals[i] < 0)
									total_number_intervening_intervals++;
								else if (intervals[i] > 0)
								{
									total_number_intervening_intervals++;
									number_arcs++;
									direction = 1;
								}
							}

							// If arc is currently ascending
							else if (direction == 1)
							{
								if (intervals[i] > 0)
									total_number_intervening_intervals++;
								else if (intervals[i] < 0)
								{
									total_number_intervening_intervals++;
									number_arcs++;
									direction = -1;
								}
							}

							// If arc is currently stationary
							else if (direction == 0)
							{
								if (intervals[i] > 0)
								{
									direction = 1;
									total_number_intervening_intervals++;
								}
								if (intervals[i] < 0)
								{
									direction = -1;
									total_number_intervening_intervals++;
								}
							}
						}
					}
				}
			}
			
			// Calculate the value
			if (number_arcs == 0)
				value = 0.0;
			else
				value = (double) total_number_intervening_intervals / (double) number_arcs;
		} 
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}