package jsymbolic2.features;

import jsymbolic2.features.MicrotonePrevalenceFeature;
import jsymbolic2.featureutils.Feature;
import jsymbolic2.processing.MIDIIntermediateRepresentations;
import org.junit.jupiter.api.Test;

import javax.sound.midi.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Created by dinamix on 7/29/16.
 */
public class PrevalenceOfMicroTonesFeatureTest {
    @Test
    public void extractFeature() throws Exception {
        File britten = new File("src/test/java/jsymbolic2/features/resources/Britten_-_Serenade_prologue.midi");
        Sequence test_tracks = MidiSystem.getSequence(britten);
        MIDIIntermediateRepresentations inter = new MIDIIntermediateRepresentations(test_tracks);

        Feature common_interval_feature = new MicrotonePrevalenceFeature();
        double[] actual_prevalence = common_interval_feature.extractFeature(test_tracks, inter, null);
        double[] expected_prevalence = {0.5273};
        assertArrayEquals(expected_prevalence, actual_prevalence, 0.0001);
    }

}