package jsymbolic2.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that finds the average number of note onsets per unit of time corresponding to an
 * idealized quarter note, divided by the total number of voices present (i.e. the number of MIDI channels
 * that contain one or more notes in the piece). Takes into account all notes in all voices, including both
 * pitched and unpitched notes.
 *
 * @author Cory McKay
 */
public class NoteDensityPerQuarterNotePerVoiceFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public NoteDensityPerQuarterNotePerVoiceFeature()
	{
		code = "R-11";
		name = "Note Density per Quarter Note per Voice";
		description = "Average number of note onsets per unit of time corresponding to an idealized quarter note, divided by the total number of voices present (i.e. the number of MIDI channels that contain one or more notes in the piece). Takes into account all notes in all voices, including both pitched and unpitched notes.";
		dependencies = new String[1];
		dependencies[0] = "Note Density per Quarter Note";
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
			double note_density_per_quarter_note = other_feature_values[0][0];
			value = note_density_per_quarter_note / sequence_info.number_of_active_voices;
		}
		else value = -1.0;

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}