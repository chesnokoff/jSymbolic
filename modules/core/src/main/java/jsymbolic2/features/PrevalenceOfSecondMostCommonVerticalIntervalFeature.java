package jsymbolic2.features;

import javax.sound.midi.Sequence;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the fraction of vertical intervals on the wrapped vertical interval
 * histogram corresponding to the second most common vertical interval.
 *
 * @author Tristano Tenaglia and Cory McKay
 */
public class PrevalenceOfSecondMostCommonVerticalIntervalFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public PrevalenceOfSecondMostCommonVerticalIntervalFeature()
	{
		code = "C-12";
		name = "Prevalence of Second Most Common Vertical Interval";
		description = "Fraction of vertical intervals on the wrapped vertical interval histogram corresponding to the second most common vertical interval.";
		dependencies = new String[] { "Wrapped Vertical Interval Histogram", "Second Most Common Vertical Interval"	};
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
			double[] wrapped_vertical_interval_histogram = other_feature_values[0];
			int second_most_common_vertical_interval = (int) Math.round(other_feature_values[1][0]);
			value = wrapped_vertical_interval_histogram[second_most_common_vertical_interval];
		}
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}