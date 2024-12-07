package jsymbolic2.processing;

import ace.datatypes.DataBoard;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;

import javax.sound.midi.Sequence;

public interface MIDIFeatureProcessorForkJoinPool {
    DataBoard generateDataBoard() throws Exception;

    double[][][] getFeatures(Sequence[] windows, MeiSpecificStorage meiSpecificStorage) throws Exception;

    void extractFeaturesFromSequence(String name, Sequence sequence, MeiSpecificStorage meiSpecificStorage)
            throws Exception;

    boolean containsMeiFeatures();

    MIDIFeatureExtractor[] getFinalFeaturesToBeExtracted();
}
