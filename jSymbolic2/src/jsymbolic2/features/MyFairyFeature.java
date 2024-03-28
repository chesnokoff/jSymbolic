package jsymbolic2.features;

import ace.datatypes.FeatureDefinition;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

import javax.sound.midi.Sequence;

import static jsymbolic2.features.MyFairyFeature.*;

public interface MyFairyFeature extends MyFairyFeatureExtractor {
    public default FeatureDefinition createFeatureDefinition(String code, FeatureDefinition definition, String[] dependencies, int[] offsets) {
        return new FeatureDefinition(.., this::extractFeature);
    };




    //FeatureDefiniton.isMeiFeature()
    //FeatureDefinition.extractFeature(...)

    public static interface MyFairyFeatureExtractor{
        public double[] extractFeature(FeatureDefinition,  Sequence sequence,
                                       MIDIIntermediateRepresentations sequence_info,
                                       double[][] other_feature_values )
                throws Exception;
    }
}
