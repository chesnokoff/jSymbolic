package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the mean pitch class value, averaged across all pitched notes in the piece.
 * A value of 0 corresponds to a mean pitch class of C, and pitches increase chromatically by semitone in
 * integer units from there (e.g. a value of 2 would mean that D is the mean pitch class). Enharmonic
 * equivalents are treated as a single pitch class.
 * 
 * @author Cory McKay
 */
public class MeanPitchClassFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public MeanPitchClassFeature()
	{
		code = "P-15";
		name = "Mean Pitch Class";
		description = "Mean pitch class value, averaged across all pitched notes in the piece. A value of 0 corresponds to a mean pitch class of C, and pitches increase chromatically by semitone in integer units from there (e.g. a value of 2 would mean that D is the mean pitch class). Enharmonic equivalents are treated as a single pitch class.";

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
			double[] pitch_classes_of_all_note_ons = new double[sequence_info.pitch_classes_of_all_note_ons.length];
			for (int i = 0; i < pitch_classes_of_all_note_ons.length; i++)
				pitch_classes_of_all_note_ons[i] = (double) sequence_info.pitch_classes_of_all_note_ons[i];
			value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(pitch_classes_of_all_note_ons);
		}
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}